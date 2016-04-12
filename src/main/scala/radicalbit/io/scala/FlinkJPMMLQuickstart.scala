package radicalbit.io.scala

import io.radicalbit.flink.pmml.scala.api.JPMMLEvaluationMapOperator
import io.radicalbit.flink.pmml.scala.strategies.{InputPreparationErrorHandlingStrategies, MissingValueStrategies, ResultExtractionStrategies}
import org.apache.flink.streaming.api.scala._
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.Random

object FlinkJPMMLQuickstart {


  private val logger = LoggerFactory.getLogger("FLINK-JPMML")

  def main(args: Array[String]) {


    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val header: Seq[String] = List(
      "petal_width",
      "petal_length",
      "sepal_length",
      "sepal_width")
    val randomData = for (i <- 1 to 100) yield Seq.fill(4)(Random.nextDouble()*8)
    val dataWithHeader = randomData.map(row => (header zip row).toMap[String, Any])

    val input = env.fromCollection(dataWithHeader)

    val source = Source.fromURL(getClass.getResource("/single_iris_kmeans.xml")).mkString

    val pmmlOperator = JPMMLEvaluationMapOperator(source,
      InputPreparationErrorHandlingStrategies.throwExceptionStrategy,
      MissingValueStrategies.delegateToPMML,
      ResultExtractionStrategies.defaultExtractResult
    )

    val predictionResult = input.map(pmmlOperator)

    predictionResult.print()

    env.execute("Flink-JPMML prediction Job")
  }
}
