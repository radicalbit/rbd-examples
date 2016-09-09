name := "akka-connector-example"

version := "1.0"

organization := "io.radicalbit"

scalaVersion in ThisBuild := "2.11.7"

resolvers in ThisBuild ++= Seq(Resolver.mavenLocal, "Radicalbit Public Maven Repository" at "https://public-repo.radicalbit.io/maven/repository/internal/")

val flinkVersion = "rbd-1.0-flink-1.1.1"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-connector-akka" % flinkVersion,
  "com.typesafe.akka" %% "akka-remote" % "2.3.7")

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )

// make run command include the provided dependencies
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
