name := "JavaGame"

version := "1.0-SNAPSHOT"

description := "A game in Java"

fork in run := true

javaSource in Compile := baseDirectory.value / "src/main/com/redomar/game"

javaSource in Test := baseDirectory.value / "src/test"

autoScalaLibrary := false

resourceDirectory in Compile := baseDirectory.value / "res"
resourceDirectory in Test := baseDirectory.value / "src/test/res"

libraryDependencies ++= Seq(
	  "org.apache.commons" % "commons-lang3" % "3.4",
	  "com.googlecode.soundlibs" % "mp3spi" % "1.9.5.4",
	  "com.googlecode.soundlibs" % "jlayer" % "1.0.1.4",
	  "junit" % "junit" % "4.12",
		"com.novocode" % "junit-interface" % "0.10" % "test"
).map(_.exclude("junit", "junit"))

mainClass in (Compile, run) := Some("com.redomar.game.Launcher")
