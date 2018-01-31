import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.FileStatus;

public class JavaGameTestLinesMR {
    public static class LinesMapper extends Mapper<Object, Text, Text, Text>{
        private Text result = new Text();
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String filePath = ((FileSplit) context.getInputSplit()).getPath().toString();
            String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
            String[] filePathParts = filePath.split("/");

            result.set(filePathParts[filePathParts.length-2]);

            String htmlLine = value.toString();

            if (htmlLine.contains("class=\"fc\"")) {
            	context.write(new Text(fileName.split("[.]")[0] + "_" +  htmlLine.substring(htmlLine.indexOf("id=\"") + 4, htmlLine.indexOf("\">"))), result);
            }
        }
    }

    public static class LinesReducer extends Reducer<Text,Text,Text,Text> {
        private Text result = new Text();
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        	// EMR code

            String fcClass = "class=\"fc\"";
            HashMap<String,Integer> testFcPairs = new HashMap<String,Integer>();
            for (Text val : values) {
                FileSystem fs = FileSystem.get(URI.create("s3://dghosh6.javagametestlinesmr"), context.getConfiguration());
            	FileStatus[] valFilePathList = fs.listStatus(new Path("/input/" + val.toString()));
                int fcCount = 0;
                for (FileStatus file : valFilePathList) {
                    BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(file.getPath())));
                    String line=br.readLine();
                    while (line != null){
                        int lastIndex = 0;
                        while(lastIndex != -1){
                            lastIndex = line.indexOf(fcClass,lastIndex);
                            if(lastIndex != -1){
                                fcCount ++;
                                lastIndex += fcClass.length();
                            }
                        }
                        line=br.readLine();
                    }
                }
                testFcPairs.put(val.toString(), fcCount);
            }

            Object[] testFcPairsArray = testFcPairs.entrySet().toArray();
            Arrays.sort(testFcPairsArray, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue()
                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });
            String sum = "";
            for (int i=0; i<testFcPairsArray.length; i++) {
                sum += ((Map.Entry<String, Integer>) testFcPairsArray[i]).getKey();
                if(i!=testFcPairsArray.length-1) sum += ",";
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "java games test lines mr");
        job.setJarByClass(JavaGameTestLinesMR.class);
       	job.setJar("JavaGameTestLinesMR.jar");
        job.setMapperClass(LinesMapper.class);
        job.setReducerClass(LinesReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPath(job, new Path(args[0]));

        // Delete old output directory if exists while creating new one
        Path outputPath = new Path(args[1]);
        // FileSystem fs = FileSystem.get(conf);
        // fs.delete(outputPath, true);

        FileOutputFormat.setOutputPath(job, outputPath);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
