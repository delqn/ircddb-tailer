package persistence

import org.squeryl.Schema

object Database extends Schema {
  val messagesTable = table[Message]("messages")
  val pollsTable = table[Poll]("polls")
}
