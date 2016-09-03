package persistence

import java.sql.Timestamp

import org.squeryl.PrimitiveTypeMode._
import org.squeryl._

case class Message(
                    id: String, // by convention this is the PK
                    rowid: Int,
                    ts: Timestamp,
                    mycall: String,
                    rpt1: String,
                    qsostarted: Boolean,
                    rpt2: String,
                    urcall: String,
                    flags: String,
                    myradio: String,
                    dest: String,
                    txstats: String
                  ) extends KeyedEntity[String] {

  def toMap: Map[String, String] = Map(
    "id" -> id,
    "rowID" -> rowid.toString,
    "ts" -> ts.toString,
    "myCall" -> mycall,
    "rpt1" -> rpt1,
    "qsoStarted" -> qsostarted.toString,
    "rpt2" -> rpt2,
    "urCall" -> urcall,
    "flags" -> flags,
    "myRadio" -> myradio,
    "dest" -> dest,
    "txStats" -> txstats
  )
}

object Message {

  /*
  // returns existing user by email address, or null if not exists
  def apply(email: String): Message = {
    inTransaction {
      // findByEmailQ(email).headOption.getOrElse(null)
    }
  }
  */

  def allQ: Query[Message] = from(Database.messagesTable) { message => select(message) }
  def findAll(): List[Message] = inTransaction { allQ.toList }
  def create(message: Message) = inTransaction { Database.messagesTable.insert(message) }

}
