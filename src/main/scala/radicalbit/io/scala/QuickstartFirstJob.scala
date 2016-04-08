package radicalbit.io.scala

import org.apache.flink.streaming.api.scala._

object QuickstartFirstJob{

  def main(args: Array[String]) {
    val env=StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val words= List("welcome", "to", "the", "fast-data", "revolution.")

    val source=env.fromCollection(words)

    source.map(_.toUpperCase).print()

    env.execute("My first Job")
  }
}