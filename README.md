Graduate(G), UID - 652811050, dghosh6@uic.edu, Dipankar Ghosh
=============================================================

JavaGame
========

A 2D game built on Java, inspired and taken from [this](https://github.com/redomar/JavaGame) GitHub repository. I have added few additional features such as JUnit tests, and build scripts.


Pre-requisites
--------------

* JDK 8 (with environment variables set)
* IntelliJ IDEA (with JUnit, Gradle & SBT plugins enabled)
* Latest versions of Gradle & SBT


Getting Started
---------------

Clone the repository from BitBucket on your local machine (or download as zip and extract). Open the directory JavaGame from the cloned/extracted directory in IntelliJ IDEA. Both Gradle & SBT builds are inside the same directory. Make sure both Gradle and SBT plugins are working correctly in the IDE, and JVM is set (JAVA_HOME environment variable should be set). Wait for the IDE to gather resources and index the project. The project should also be imported as a SBT project to run the SBT tasks. If it is not imported by default, then in the IDE, open [build.sbt](JavaGame/build.sbt) and as it shows `build.sbt is a SBT build file` on top, click on `Import project`. Also, make sure the project structure (source, resources, test directory) are set properly in the IDE. Once done, you are all good to go!


Testing
-------

I have included 5 JUnit test files inside the [test](JavaGame/src/test/java) directory under the project structure. Each of them test various functionalities of the application. You may run them using Gradle/SBT tasks. Gradle and SBT environment variables must be set.

**Testing with SBT:** Open Bash in the JavaGame root, and run the command `sbt clean` and `sbt test`. It will show details of classes compiled and tests run. You will get a 'success' message in the end which ensures the tests are successful.

**Testing with Gradle:** Open the Gradle Tool Window from the menu bar (View -> Tool Windows -> Gradle). Inside the Gradle Tool Window, go to Tasks -> build -> clean. Wait for the application to complete the task. Once done, go to Tasks -> verification -> test. It will start executing the test files from the test directory, and soon you should see *All 5 tests passed*. Under the Run window, there will be an icon with the tooltip showing *Open Gradle test report*. Click on the icon to obtain a detailed view of the test report. If you want to run the tests again, run the clean task, and then run the test task. Alternatively, you may open Bash in the JavaGame root, and run the command `gradle clean` and `gradle test`.


Instrumention & Test Coverage Report
------------------------------------

I have used JaCoCo plugin for Gradle to instrument the code on-the-fly and generate test reports. For the purpose of generating individual test coverage reports, I have implemented a shell script, [generateTestReports.sh](JavaGame/generateTestReports.sh), which, if you run from Bash in the JavaGame root using command `sh generateTestReports.sh`, it will generate the individual test reports in a folder in the JavaGame root named `testReports`. Now, each directory inside `testReports` will contain the source code coverage report for that particular test. Each directory names in the `testReports` denotes the test names. The HTML files obtained inside these directories as a result of the report generation will be taken as input by our map-reduce code (described broadly in the YouTube video).


Compiling & running the application
-----------------------------------

I have included run tasks in both Gradle & SBT builds, so you may use any one of them. When you start the run task, it will automatically build the application (using [build.gradle](JavaGame/build.gradle) for the Gradle task, and [build.sbt](JavaGame/build.sbt) for the SBT task), compiling all the .java files, resolving the dependencies and paths, finally launching the application from its main class [Launcher](JavaGame/src/main/com/redomar/game/Launcher.java), which is specified inside build.gradle/sbt.

**Launching using SBT:** From Bash in JavaGame root, run the command `sbt run` to launch the application. Alternatively, from the SBT Tool Window, go to javagame (not javagame-build) -> SBT Tasks -> run. The Splash screen will show up in a new window, and will start loading the resources. It will then ask for your name and other character details. So that’s it. You have successfully launched JavaGame! :thumbsup:

**Launching using Gradle:** From the Gradle Tool Window, go to Tasks -> build -> build. Once build is complete, go to Tasks -> application -> run. The Splash screen will show up in a new window, and will start loading the resources. Alternatively, you may use `gradle run` command from Bash to launch the application. It will then ask for your name and other character details. So that’s it. You have successfully launched JavaGame! :thumbsup:

Hadoop Map Reduce
=================

I have included the map-reduce code for the test coverage of the application in [HadoopMapReduce/JavaGameTestLinesMR](HadoopMapReduce/JavaGameTestLinesMR) directory. The [.java file](HadoopMapReduce/JavaGameTestLinesMR/JavaGameTestLinesMR.java) inside it is compiled using `hadoop com.sun.tools.javac.Main JavaGameTestLinesMR.java` command in Bash, and a JAR is created with `jar cf JavaGameTestLinesMR.jar JavaGameTestLinesMR*.class` command. This JAR is uploaded to Amazon S3 bucket which is then accessed by Amazon EMR. The directory mentioned above also contains sample inputs. The sample inputs are used for my deployment on Amazon EMR. Here the sample inputs are few HTML files taken from the `testReports` directory for each test. For the sake of simplicity, I have not taken the HTML files which don't contain any line coverage. Also, since I have parsed the HTML file inputs in mapper to form respective key-value pairs, so it will take a lot of time for Amazon EMR to run the mapper code if I feed all of the HTML files into the mapper. 

For the +5% bonus points (maximum coverage problem), I have used the code inside [HadoopMapReduce/MaxCoverageMR](HadoopMapReduce/MaxCoverageMR). It also contains the sample input (which I created, and you may modify it as required and test the output), and the JAR file (obtained in the same way I mentioned in the last paragraph), which are used for the deployment on Amazon EMR. For this implementation I have referred to links like [set operation](https://stackoverflow.com/questions/43354077/operations-with-3-sets-in-java) and [n choose k](https://stackoverflow.com/questions/46011763/grouping-algorithm-java-n-choose-k). Rest of the logic is build by me.

The steps of my deployment on EMR for both the afore-mentioned map-reduce code are outlined in this YouTube video: [https://youtu.be/IC2nBOgbjq0](https://youtu.be/IC2nBOgbjq0)