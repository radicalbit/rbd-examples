package radicalbit.io.scala

import io.radicalbit.flink.pmml.scala.api.JPMMLEvaluationMapOperator
import io.radicalbit.flink.pmml.scala.strategies._
import org.apache.flink.streaming.api.scala._
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.Random

object FlinkJPMMLQuickstart {

  private val logger = LoggerFactory.getLogger("FLINK-JPMML")

  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //First, we generate some random data
    val header: Seq[String] = List(
      "petal_width",
      "petal_length",
      "sepal_length",
      "sepal_width")
    val randomData = for (i <- 1 to 100) yield Seq.fill(4)(Random.nextDouble() * 8)
    val dataWithHeader = randomData.map(row => (header zip row).toMap[String, Any])
    val input = env.fromCollection(dataWithHeader)

    //Then we read a PMML from the local filesystem
    val source = Source.fromURL(getClass.getResource("/single_iris_kmeans.xml")).mkString

    //Here we create the JPMMLEvaluationMapOperator
    val pmmlOperator = JPMMLEvaluationMapOperator(source,
      InputPreparationErrorHandlingStrategies.throwExceptionStrategy,
      MissingValueStrategies.delegateToPMML,
      ResultExtractionStrategies.extractTargetAndOutputField)

    //To the input DataStream, we connect the Evaluation Operator...
    val predictionResult = input.map(pmmlOperator)

    //...and last we invoke print() to see the results on the console
    predictionResult.print()

    env.execute("Flink-JPMML prediction Job")
  }
}
