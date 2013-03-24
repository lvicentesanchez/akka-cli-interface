package actors

trait AkkaCLIMsg

case class CLIReady() extends AkkaCLIMsg
case class Mogrify(src: String, fmt: String)