import actors.{ AkkaCLI, Mogrify }
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object CLIMain extends App {
  val config = ConfigFactory.load()
  val clisys = ActorSystem("CLI", config.getConfig("cli").withFallback(config))
  val master = clisys.actorOf(Props[AkkaCLI], "akka-cli")
  
  master ! Mogrify("image01.png", "jpg")
}