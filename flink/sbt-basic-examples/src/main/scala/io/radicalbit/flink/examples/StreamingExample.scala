package io.radicalbit.flink.examples

import org.apache.flink.streaming.api.scala._

object StreamingExample {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val words = env.fromElements("hello", "flink")

    val lengths = words.map(word => word.length)

    lengths.print()

    env.execute()

  }

}
