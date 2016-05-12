import org.apache.flink.api.scala._

object Job {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val words = env.fromElements("hello", "flink")

    val lengths = words.map(word => word.length)

    lengths.print()

  }

}
