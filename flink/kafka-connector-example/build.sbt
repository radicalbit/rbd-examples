resolvers in ThisBuild ++= Seq(Resolver.mavenLocal, "Radicalbit Public Maven Repository" at "https://public-repo.radicalbit.io/maven/repository/internal/")

name := "Kafka Connector Example"

version := "0.1"

organization := "io.radicalbit"

scalaVersion in ThisBuild := "2.11.7"

val flinkVersion = "rbd-1.0-flink-1.1.1"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-connector-kafka-0.9" % flinkVersion)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )

mainClass in assembly := Some("io.radicalbit.flink.examples.KafkaConnectorExample")

// make run command include the provided dependencies
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
