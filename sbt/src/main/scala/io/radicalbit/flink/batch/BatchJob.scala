package io.radicalbit.flink.batch

import org.apache.flink.api.scala._

object BatchJob {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val words = env.fromElements("hello", "flink")

    val lengths = words.map(word => word.length)

    lengths.print()

  }

}
