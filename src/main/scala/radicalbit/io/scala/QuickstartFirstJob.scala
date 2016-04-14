package radicalbit.io.scala

import org.apache.flink.streaming.api.scala._

object QuickstartFirstJob {

  def main(args: Array[String]) {

    //Here we create the Streaming Execution environment...
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //...and we set its parallelism to 1 to see the results printed in order
    env.setParallelism(1)

    //We first create a DataStream from a bunch of strings...
    val source = env.fromElements("welcome", "to", "the", "fast-data", "revolution.")

    //...and we turn them to uppercase.
    val processed = source.map(_.toUpperCase)

    //At last we print the result to console. This also implicitly calls the .execute() method.
    processed.print()

    //We call it explicitely anyway to give the Job an unique and recognizable name.
    env.execute("My first Job")
  }
}