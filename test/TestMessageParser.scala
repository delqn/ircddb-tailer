import org.specs2.mutable._
import parser.MessageParser

class MessageParserTest extends Specification {

  "MesasgeParser" should {
    "split and filter lines" in {
      val lines = MessageParser.getLines(TestData.msg)
      lines.length must equalTo(11)
    }

    "ignore certain messages" in {
      var lines = MessageParser.getLines(TestData.msg)

      MessageParser.isInvalid(lines.head, "553379") must equalTo(true)

      MessageParser.isInvalid("too-short", "553379") must equalTo(true)

      MessageParser.isInvalid(lines(10), "553379") must equalTo(true)

      MessageParser.isInvalid(lines(8), "553379") must equalTo(false)

      MessageParser.isInvalid(lines(9), "553379") must equalTo(false)
    }

    "parse message with a dest field" in {
      val lines = MessageParser.getLines(TestData.msg)
      val m = MessageParser.parse(lines.head)
      m.rowid must equalTo(553379)
      m.ts.toString must equalTo("2016-09-02 17:51:03.0")
      m.mycall must equalTo("N0DEC")
      m.qsostarted must equalTo(true)
      m.dest must equalTo("destx")
    }

    "parse message" in {
      val lines = MessageParser.getLines(TestData.msg)
      val m = MessageParser.parse(lines(1))
      m.rowid must equalTo(553380)
      m.ts.toString must equalTo("2016-09-02 17:51:05.0")
      m.mycall must equalTo("IZ6FGP")
      m.rpt1 must equalTo("IR6UCI B")
      m.qsostarted must equalTo(false)
      m.rpt2 must equalTo("IR6UCI G")
      m.urcall must equalTo("CQCQCQ")
      m.flags must equalTo("000000")
      m.myradio must equalTo("AUTO")
      m.dest must equalTo("")
      m.txstats must equalTo("0.3s S:53% E:0.0%")
      m.id must equalTo("IZ6FGP__IR6UCI_B1IR6UCI_GCQCQCQ__")
    }

    "make unique key" in {
      val lines = MessageParser.getLines(TestData.msg)
      val m = MessageParser.parse(lines(1))
      val messages = "317470:20151223192301N0DEC___WW6BAY_B0WW6BAY_G/WW6BAYB000000D___01________"
      val key = MessageParser.makeKey(messages)
      key must equalTo("N0DEC___WW6BAY_B1WW6BAY_G/WW6BAYB")
    }

    "toString constructs correct JSON representation" in {
      val lines = MessageParser.getLines(TestData.msg)
      val m = MessageParser.parse(lines(1))
      // TODO(delyan): convert when to a UNIX timestamp
      m.toString must equalTo("""{
                                |"rowID":553380,
                                |"when":"2016-09-02 17:51:05.0",
                                |"myCall":"IZ6FGP",
                                |"rpt1":"IR6UCI B",
                                |"qsoStarted":false,
                                |"rpt2":"IR6UCI G",
                                |"urCall":"CQCQCQ",
                                |"flags":"000000",
                                |"myRadio":"AUTO",
                                |"dest":"",
                                |"txStats":"0.3s S:53% E:0.0%",
                                |"uniqueKey":"IZ6FGP__IR6UCI_B1IR6UCI_GCQCQCQ__",
                                |}""".stripMargin)
    }
  }
}

object TestData {
  val msg ="""
            |553379:20160902175103N0DEC___WW6BAY_B0WW6BAY_G/WW6BAYB000000D___01___destx
            |553380:20160902175105IZ6FGP__IR6UCI_B1IR6UCI_GCQCQCQ__000000AUTO00________0.3s_S:53%_E:0.0%___
            |553381:20160902175109AF9W____WA7VC__B0WA7VC__GCQCQCQ__000000ID5100REF029_ABob_North_Bend,_WA__
            |553382:20160902175111AF9W____WA7VC__B1WA7VC__GCQCQCQ__000000ID5100________2.3s_S:0%_E:0.0%____
            |553383:20160902175115LZ1LR___LZ0DAB_B1LZ0DAB_GCQCQCQ__000000006100________10.8s_S:0%_E:0.0%___
            |553384:20160902175115NU7Y____W7AI___B0W7AI___GCQCQCQ__000000ID5100REF029_AQUINTON_CCS_3104465_
            |553385:20160902175117LZ1MVR__LZ0DAB_B0LZ0DAB_GCQCQCQ__000000001600DCS023_BKoko________________
            |553386:20160902175119LZ1MVR__LZ0DAB_B1LZ0DAB_GCQCQCQ__000000001600________1.8s_S:0%_E:0.0%____
            |553387:20160902175120LZ1MVR__LZ0DAB_B0________________000000____00________
            |553388:20160902175121LZ1LR___LZ0DAB_B0LZ0DAB_GCQCQCQ__000000006100DCS023_Bwww.dstar.bg________
            |553389:20160902175123********CQ0DSA_B0CQ0DSA_GCQCQCQ__000000____00DCS012_V____________________
            |
            |""".stripMargin
  }
