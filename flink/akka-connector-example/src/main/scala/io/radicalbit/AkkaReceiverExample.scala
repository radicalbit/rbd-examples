package io.radicalbit

import akka.actor._
import scala.tools.nsc.io._

object AkkaReceiverExample {

  def main(args: Array[String]): Unit = {

    val filename = args(0)
    val actorSystem = ActorSystem.create("actor-test", ConfigurationUtil.conf(4000))
    val actor = actorSystem.actorOf(Props(new ActorReceiver(filename)), "receiver")
  }

  class ActorReceiver(filename: String) extends Actor with ActorLogging {
    override def receive = {
      case l: String =>
        log.debug(s"### element $l")
        File(filename).appendAll(s"$l \n")
    }
  }

}
