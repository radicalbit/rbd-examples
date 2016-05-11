package radicalbit.io.scala

import org.apache.flink.connectors.akka.streaming.examples.AkkaReceiverExample._
import org.apache.flink.connectors.akka.streaming.examples.Conf

import scala.tools.nsc.io._

object AkkaReceiverExample extends Conf {

	def main(args: Array[String]): Unit = {

		val filename = args(0)
		val actorSystem = ActorSystem.create("actor-test", conf(4000))
		val actor = actorSystem.actorOf(Props(new ActorReceiver(filename)), "receiver")
	}

	class ActorReceiver(filename: String) extends Actor with ActorLogging {
		override def receive = {
			case l : String =>
				log.debug(s"### element $l")
				File(filename).appendAll(s"$l \n")
		}
	}
}
