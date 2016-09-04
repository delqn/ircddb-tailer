import org.specs2.mutable._
import utils.Utils.{stringDateToTimestamp, sha256}

class TestUtils extends Specification {

  "Utils" should {
    "parse dates" in {
      stringDateToTimestamp("20151223192301").toString must equalTo("2015-12-23 19:23:01.0")
    }

    "sha256" in {
      sha256("blah") must equalTo("8b7df143d91c716ecfa5fc1730022f6b421b05cedee8fd52b1fc65a96030ad52")
    }
  }
}

