package actors

import akka.actor.{ ActorLogging, Actor, OneForOneStrategy, Props }
import akka.actor.Status.Failure
import akka.actor.SupervisorStrategy.Restart
import akka.routing.RoundRobinRouter

class AkkaCLI extends Actor with ActorLogging {
  def receive = {
    case m: AkkaCLIMsg => workers forward m
  }

  override def unhandled(message: Any): Unit = {
    sender ! Failure(new UnsupportedOperationException(message.toString))
  }

  private[this] val strategy = OneForOneStrategy(maxNrOfRetries = -1) {
    case _: Exception => Restart
  }

  private[this] val workers = context.actorOf(Props[AkkaCLIWorker].withDispatcher("akka.actor.my-dispatcher").withRouter(RoundRobinRouter(8).withSupervisorStrategy(strategy)), "akka-cli-worker")
}