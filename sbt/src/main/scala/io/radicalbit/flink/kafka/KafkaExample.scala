package io.radicalbit.test

import java.util.Properties

import scala.collection.JavaConversions.mapAsScalaMap

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer09, FlinkKafkaProducer09}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.slf4j.LoggerFactory

object FlinkKafka {

  private def logger = LoggerFactory.getLogger(FlinkKafka.getClass)

  // This object will serialize and deserialize data to and from Kafka
  private val serdeSchema = new SimpleStringSchema

  // A set of required parameters, all of which must be provided at launch
  // These parameters are:
  // --broker-list:  a comma-separated list of <host:port> pairs pointing to Kafka brokers
  // --source-topic: the Kafkatopic we'll read from
  // --sink-topic:   the Kafka topic we'll write to
  // Both topics must exist before the launch.
  // Furthermore, the optional --secure option will enable communication with a
  // secure Kafka installation if set to true
  private val requiredParameters = Set("broker-list", "sink-topic", "source-topic")

  def main(args: Array[String]) {

    // Initialize Flink
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // Check missing parameters, exit if any are missing
    val params = ParameterTool.fromArgs(args)
    val missingParameters = requiredParameters.filterNot(params.has)
    if (missingParameters.nonEmpty) {
      logger.error("Missing parameters: {}", missingParameters.mkString)
      System.exit(1)
    }

    // Translate input parameters into Kafka-friendly properties
    val (consumerProps, producerProps) = getConsumerAndProducerProps(params)

    // Define the Kafka consumer and producer for Flink using the given parameters
    val kafkaConsumer = new FlinkKafkaConsumer09[String](params.get("source-topic"), serdeSchema, consumerProps)
    val kafkaProducer = new FlinkKafkaProducer09[String](params.get("sink-topic"), serdeSchema, producerProps)

    // Our simple job: append a [processed] tag to each incoming string and write it to the sink
    env.addSource(kafkaConsumer).map(in => s"$in [processed]").addSink(kafkaProducer)

    // Run the job
    env.execute("Secure Flink-Kafka Example")

  }

  private def getConsumerAndProducerProps(params: ParameterTool): (Properties, Properties) = {

    // Consumer properties: put together the broker list and a unique group id
    val bootstrapServers = params.get("broker-list")
    val consumerProps = new Properties
    consumerProps.setProperty("bootstrap.servers", bootstrapServers)
    consumerProps.setProperty("group.id", s"flink-kafka-test-${System.currentTimeMillis}")

    // Producer properties: we just need the broker list
    val producerProps = new Properties
    producerProps.setProperty("bootstrap.servers", bootstrapServers)

    // If the user specified the --secure option as true, enable it by setting the security.protocol
    // property to SASL_PLAINTEXT for both the consumer and the producer
    if (params.get("secure").equals("true")) {
      consumerProps.setProperty("security.protocol", "SASL_PLAINTEXT")
      producerProps.setProperty("security.protocol", "SASL_PLAINTEXT")
    }

    // Return the properties as a pair
    (consumerProps, producerProps)

  }

}
