package persistence

import java.sql.Timestamp

import org.squeryl.PrimitiveTypeMode._
import org.squeryl._
import org.postgresql.util.PSQLException
import play.api.Logger

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
  def lastMessage: Message =
    inTransaction {
      from(Database.messagesTable) {
        message => select(message).orderBy(message.ts desc)
      }.page(0, 1).head
    }

  def allQ: Query[Message] = from(Database.messagesTable) { message => select(message) }

  def findAll: List[Message] = inTransaction { allQ.toList }

  def getStatus: Map[String, String] = Map("count" -> inTransaction {
    from(Database.messagesTable) {
      message => compute(countDistinct(message.id))
    }.single.measures.toString
  }
  )

  def create(message: Message) = {
    try {
      inTransaction {
        // TODO(delyan): can we get UPSERT / insertOrUpdate to work here instead?
        Database.messagesTable.deleteWhere(msg => msg.id === message.id)
        Database.messagesTable.insert(message)
      }
    } catch {
      case e: PSQLException => Logger.warn(s"Message with id=${message.id} already exists. Trying an update instead.")
      case e: Throwable => Logger.error(s"PSQL Error --> $e")
    }
  }

}
