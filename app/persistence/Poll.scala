package persistence

import java.sql.Timestamp

import org.squeryl.PrimitiveTypeMode._
import org.squeryl._
import org.postgresql.util.PSQLException
import play.api.Logger

case class Poll(id: String, ts: Timestamp, url: String) extends KeyedEntity[String] {
  def toMap: Map[String, String] = Map("id" -> id, "ts" -> ts.toString, "url" -> url)
}

object Poll {
  def allQ: Query[Poll] = from(Database.pollsTable) { Poll => select(Poll) }
  def findAll: List[Poll] = inTransaction { allQ.toList }
  def getStatus: Map[String, String] = Map("count" -> inTransaction {
    from(Database.pollsTable) {
      Poll => compute(countDistinct(Poll.id))
    }.single.measures.toString
  }
  )
  def create(Poll: Poll) = try {
    inTransaction { Database.pollsTable.insert(Poll) }
  } catch {
    case e: PSQLException => Logger.warn(s"Poll with id=${Poll.id} already exists.")
    case e: Throwable => Logger.error(s"PSQL Error --> $e")
  }

}
