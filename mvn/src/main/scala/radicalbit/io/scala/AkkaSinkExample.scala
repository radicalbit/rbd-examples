package radicalbit.io.scala

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import org.apache.flink.connectors.akka.streaming.AkkaSink
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object AkkaSinkExample extends Conf {

	val actorReceiverPath = "akka.tcp://actor-test@127.0.0.1:4000/user/receiver"

	def main(args: Array[String]): Unit = {

		val env = StreamExecutionEnvironment.getExecutionEnvironment
		env.setParallelism(1)

		//  AkkaSink implicit timeout
		implicit val timeout = akka.util.Timeout(10L, TimeUnit.SECONDS)

		val stream = env.generateSequence(0, 1000L).map(x => x.toString)
		stream.addSink(new AkkaSink[String]("test", actorReceiverPath, Seq(conf(5000))))

		env.execute("AkkaSinkExample")
	}
}

abstract class Conf {

	def conf(port: Int) = ConfigFactory.parseString {
		s"""
			 |akka {
			 |  actor {
			 |    provider = "akka.remote.RemoteActorRefProvider"
			 |  }
			 |  remote {
			 |    netty.tcp {
			 |      hostname = "127.0.0.1"
			 |      port = $port
			 |    }
			 | }
			 |}
			 |""".stripMargin
	}
}
