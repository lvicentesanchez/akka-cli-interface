package actors

import java.io.InputStream
import java.io.OutputStream

import scala.concurrent.Future
import scala.concurrent.Promise
import scala.sys.process.BasicIO
import scala.sys.process.Process
import scala.sys.process.ProcessIO
import scala.sys.process.ProcessLogger
import scala.sys.process.stringToProcess

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Stash
import akka.actor.Status.Failure
import akka.actor.actorRef2Scala

class AkkaCLIWorker extends Actor with ActorLogging with Stash {
  def executor: Receive = {
    case Mogrify(srd, fmt) => {
      sender ! "received"
    }
  }

  def receive = {
    case CLIReady() =>
      unstashAll()
      context.become(executor)
      log info "gm ready"
    case mess: AkkaCLIMsg =>
      stash()
  }

  // Lifecycle
  ////
  override def postStop() {
    // Stash messages will be sent to deadletters
    ////
    super.postStop()
    ////

    clicmd.destroy
  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    // If there is a message, we have access to the sender
    ////
    message foreach { _ =>
      sender ! Failure(reason)
    }
    ////

    // Stop children and call postStop
    ////
    super.preRestart(reason, message)
    ////
  }

  override def preStart() {
    clicmd
  }
  ////

  override def unhandled(message: Any): Unit = {
    sender ! Failure(new UnsupportedOperationException(message.toString))
  }

  private[this] lazy val logger = BasicIO(true, ProcessLogger(
    m => {
      if (m == prompt) self ! CLIReady()
      if (m == pass) log info "PASSED"
      if (m == fail) log info "FAILED"
    },
    log error _
  ))
  private[this] lazy val (clicmd, stdin, stdout, stderr) =
    run(s"gm batch -prompt $prompt -feedback on -fail $fail -pass $pass")
  
  private[this] def run(command: String): (Process, Future[OutputStream], Future[InputStream], Future[InputStream]) = {
    val promiseStdin = Promise[OutputStream]()
    val promiseStdout = Promise[InputStream]()
    val promiseStderr = Promise[InputStream]()
    val process = command run new ProcessIO(
      (stdin: OutputStream) => promiseStdin.success(stdin),
      (stdout: InputStream) => promiseStdout.success(stdout),
      (stderr: InputStream) => promiseStderr.success(stderr)
    )
    (process, promiseStdin.future, promiseStdout.future, promiseStderr.future)
  }

  private val prompt = "GM>"
  private val fail = "ERR"
  private val pass = "SUC"
}