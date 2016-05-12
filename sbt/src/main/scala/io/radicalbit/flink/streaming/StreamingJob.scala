package io.radicalbit.flink.streaming

import org.apache.flink.streaming.api.scala._

object StreamingJob {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val words = env.fromElements("hello", "flink")

    val lengths = words.map(word => word.length)

    lengths.print()

    env.execute()

  }

}
