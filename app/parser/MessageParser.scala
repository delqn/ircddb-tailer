package parser

import java.sql.Timestamp
import java.util.{Calendar, Date}

import utils.Utils.{parseDate, sha256}

class Message(msg: String) {
  // example: 317470:20151223192301N0DEC___WW6BAY_B0WW6BAY_G/WW6BAYB000000D___01________

  val nowDate = new Date().getTime

  var _rowID: Int = 1
  var _when: Timestamp = new Timestamp(nowDate)
  var _myCall: String = ""
  var _rpt1: String = ""
  var _qsoStarted: Boolean = false
  var _rpt2: String = ""
  var _urCall: String = ""
  var _flags: String = ""
  var _myRadio: String = ""
  var _dest: String = ""
  var _txStats: String = ""
  var _uniqueKey: String = ""

  def cleanUp(something: String): String = "[_]+".r.replaceAllIn(something, " ").trim

  def rowID(v: String): Message = { _rowID = v.toInt; this }
  def when(v: String): Message = { _when = parseDate(v); this }
  def myCall(v: String): Message = { _myCall = cleanUp(v); this }
  def rpt1(v: String): Message = { _rpt1 = cleanUp(v); this }
  def qsoStarted(v: String): Message = { _qsoStarted = v == "0"; this }
  def rpt2(v: String): Message = { _rpt2 = cleanUp(v); this }
  def urCall(v: String): Message = { _urCall = cleanUp(v); this }
  def flags(v: String): Message = { _flags = cleanUp(v); this }
  def myRadio(v: String): Message = { _myRadio = cleanUp(v); this }
  def dest(v: String): Message = { _dest = cleanUp(v); this }
  def txStats(v: String): Message = { _txStats = cleanUp(v); this }
  def uniqueKey(v: String): Message = { _uniqueKey = v; this }

  private def extract(regexString: String) =
    regexString.r.findFirstMatchIn(msg) match {
      case Some(theMatch) => theMatch.group(1)
      case _ => ""
    }

  def apply(): Message = {
    rowID(extract("""^(\d*):"""))
    when(extract(""":(\d{14})"""))
    myCall(extract("""^\d*:\d{14}(.{8})"""))
    rpt1(extract("""^\d*:.{22}(.{8})"""))
    qsoStarted(extract("""^\d*:.{30}(\d)"""))
    rpt2(extract("""^\d*:.{31}(.{8})"""))
    urCall(extract("""^\d*:.{39}(.{8})"""))
    flags(extract("""^\d*:.{47}(.{6})"""))
    myRadio(extract("""^\d*:.{53}(.{4})"""))
    dest(extract("""^\d*:.{59}(.{8})"""))
    txStats(extract("""^\d*:.{67}(.{20})"""))
    //uniqueKey(extract("""^\d*:\d{14}(.{33})"""))
    uniqueKey(sha256(msg))
    this
  }

  override def toString: String = {
    s"""{
        |"rowID":${_rowID},
        |"when":${_when},
        |"myCall":"${_myCall}",
        |"rpt1":"${_rpt1}",
        |"qsoStarted":${_qsoStarted},
        |"rpt2":"${_rpt2}",
        |"urCall":"${_urCall}",
        |"flags":"${_flags}",
        |"myRadio":"${_myRadio}",
        |"dest":"${_dest}",
        |"txStats":"${_txStats}",
        |"uniqueKey":"${_uniqueKey}",
        |}""".stripMargin
  }

  def toMap: Map[String, String] = Map(
    //TODO(delyan): should be Map[String, Any]
    "rowID" -> _rowID.toString,
    "when" -> _when.toString,
    "myCall" -> _myCall,
    "rpt1" -> _rpt1,
    "qsoStarted" -> _qsoStarted.toString,
    "rpt2" -> _rpt2,
    "urCall" -> _urCall,
    "flags" -> _flags,
    "myRadio" -> _myRadio,
    "dest" -> _dest,
    "txStats" -> _txStats,
    "uniqueKey" -> _uniqueKey
  )

  def commitToDB(): Message = {
    val dbMessage = new persistence.Message(
      _uniqueKey,
      _rowID,
      _when,
      _myCall,
      _rpt1,
      _qsoStarted,
      _rpt2,
      _urCall,
      _flags,
      _myRadio,
      _dest,
      _txStats
    )
    persistence.Message.create(dbMessage)
    this
  }

}

object MessageParser {

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

  // TODO(delyan): this here needs less java, more scala
  def parse(msg: String): Message = new Message(msg).apply()
}
