package parser

import java.sql.Timestamp

import persistence.Message
import utils.Utils.stringDateToTimestamp

object MessageParser {

  // example: 317470:20151223192301N0DEC___WW6BAY_B0WW6BAY_G/WW6BAYB000000D___01________

  val MESSAGE_LENGTH = 74

  def isInvalid(msg: String, nextPageId: String): Boolean = {
    val Prefix = s"^$nextPageId:".r

    Prefix.findFirstMatchIn(msg) match {
      case Some(str) => true
      case _ => msg.length < MESSAGE_LENGTH || msg(21) == '*'
    }
  }

  def getLines(messages: String): Seq[String] = {
    messages.split("\n").filter(_.length > 1)
  }

  def extract(regexString: String, msg: String): String =
    regexString.r.findFirstMatchIn(msg) match {
      case Some(theMatch) => theMatch.group(1)
      case _ => ""
    }

  def extractRowId(msg: String): String = extract("""^(\d*):""", msg)
  def extractWhen(msg: String): String = extract(""":(\d{14})""", msg)
  def extractMyCall(msg: String): String = cleanUp(extract("""^\d*:\d{14}(.{8})""", msg))
  def extractRpt1(msg: String): String = cleanUp(extract("""^\d*:.{22}(.{8})""", msg))
  def extractQsoStarted(msg: String): String = extract("""^\d*:.{30}(\d)""", msg)
  def extractRpt2(msg: String): String = cleanUp(extract("""^\d*:.{31}(.{8})""", msg))
  def extractUrCall(msg: String): String = cleanUp(extract("""^\d*:.{39}(.{8})""", msg))
  def extractFlags(msg: String): String = extract("""^\d*:.{47}(.{6})""", msg)
  def extractMyRadio(msg: String): String = extract("""^\d*:.{53}(.{4})""", msg)
  def extractDest(msg: String): String = cleanUp(extract("""^\d*:.{59}(.{8})""", msg))
  def extractTxStats(msg: String): String = cleanUp(extract("""^\d*:.{67}(.{20})""", msg))

  def makeKey(msg: String): String = {
    val key = extract("""^\d*:\d{14}(.{33})""", msg)
    s"${key.slice(0, 16)}1${key.slice(17, key.length)}"
  }

  def cleanUp(something: String): String = "[_]+".r.replaceAllIn(something, " ").trim


  def parse(msg: String): Message = {

    val id: String = makeKey(msg)
    val rowid: Int = extractRowId(msg).toInt
    val ts: Timestamp = stringDateToTimestamp(extractWhen(msg))
    val mycall: String = extractMyCall(msg)
    val rpt1: String = extractRpt1(msg)
    // TODO(delyan): verify the validity of this
    val qsostarted: Boolean = extractQsoStarted(msg) == "0"
    val rpt2: String = extractRpt2(msg)
    val urcall: String = extractUrCall(msg)
    val flags: String = extractFlags(msg)
    val myradio: String = extractMyRadio(msg)
    val dest: String = extractDest(msg)
    val txstats: String = extractTxStats(msg)

    new Message(id, rowid, ts, mycall, rpt1, qsostarted, rpt2, urcall, flags, myradio, dest, txstats)
  }
}
