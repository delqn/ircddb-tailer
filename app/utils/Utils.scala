package utils

import java.security.MessageDigest
import java.sql.Timestamp
import java.util.Date

object Utils {

  def sha256(s: String): String =
    MessageDigest
      .getInstance("SHA-256")
      .digest(s.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString

  def parseDate(v: String): Timestamp = {
    // example: 20151223192301
    val format = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
    new Timestamp(format.parse(v).getTime)
  }

}