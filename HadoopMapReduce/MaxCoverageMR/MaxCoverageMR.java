import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxCoverageMR {
    public static class LinesMapper extends Mapper<Object, Text, Text, Text>{
        private Text result = new Text();
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            context.write(new Text("key"), value);
        }
    }

    public static class LinesReducer extends Reducer<Text,Text,Text,Text> {
        private Text result = new Text();

        private String tests[];
        private String[][] lines;
        private int len;
        private int[] indices;

        private int[] getSubset(int[] input, int[] subset) {
            int[] result = new int[subset.length];
            for (int i = 0; i < subset.length; i++)
                result[i] = input[subset[i]];
            return result;
        }
        public List<int[]> getCombsList(int k) {
            List<int[]> subsets = new ArrayList<>();

            int[] s = new int[k];                  // here we'll keep indices
            // pointing to elements in input array

            if (k <= len) {
                // first index sequence: 0, 1, 2, ...
                for (int i = 0; (s[i] = i) < k - 1; i++);
                subsets.add(getSubset(indices, s));
                for(;;) {
                    int i;
                    // find position of item that can be incremented
                    for (i = k - 1; i >= 0 && s[i] == indices.length - k + i; i--);
                    if (i < 0) {
                        break;
                    }
                    s[i]++;                    // increment this item
                    for (++i; i < k; i++) {    // fill up remaining items
                        s[i] = s[i - 1] + 1;
                    }
                    subsets.add(getSubset(indices, s));
                }
            }
            return subsets;
        }
        public Set<String> getUnion(int[] t_indices) {
            Set<String>[] sets = new HashSet[t_indices.length];
            Set<String> union = new HashSet<>();
            for(int i=0; i<t_indices.length;i++) {
                sets[i] = new HashSet<>();
                for(int j=0; j<lines[t_indices[i]].length; j++){
                    sets[i].add(lines[t_indices[i]][j]);
                }
                union.addAll(sets[i]);
            }
            return union;
        }

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            List<String> testsList = new ArrayList<>();
            List<String[]> linesList = new ArrayList<>();
            for (Text val : values) {
                String valStr = val.toString();
                String[] valStrParts = valStr.split("=");
                testsList.add(valStrParts[0]);
                linesList.add(valStrParts[1].split(","));
            }
            this.tests = testsList.toArray(new String[0]);
            this.lines = linesList.toArray(new String[0][0]);
            this.len = tests.length;
            this.indices = new int[len];
            for (int i = 0; i < len; i++) {
                this.indices[i] = i;
            }
            int max = 0;
            int[] maxComb=null;
            Set<String> maxUnion = new HashSet<>();
            for (int i=0; i<this.tests.length; i++){
                List<int[]> combsList = this.getCombsList(i+1);
                for (int[] comb : combsList){
                    Set<String> union = this.getUnion(comb);
                    if(union.size() > max) {
                        max = union.size();
                        maxComb = comb;
                        maxUnion = union;
                    }
                }
            }
            String testsKey = "";
            for (int i=maxComb.length-1; i>=0; i--) {
                testsKey += tests[maxComb[i]];
                if(i!=0) testsKey += ", ";
            }
            result.set("[" + testsKey + "] ");
            context.write(result, new Text(maxUnion.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MaxCoverageMR");
        job.setJarByClass(MaxCoverageMR.class);
        job.setJar("MaxCoverageMR.jar");
        job.setMapperClass(LinesMapper.class);
        job.setReducerClass(LinesReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPath(job, new Path(args[0]));

        // Delete old output directory if exists while creating new one
        Path outputPath = new Path(args[1]);
//        FileSystem fs = FileSystem.get(conf);
//        fs.delete(outputPath, true);

        FileOutputFormat.setOutputPath(job, outputPath);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
