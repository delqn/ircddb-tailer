package persistence

import org.squeryl.{Schema, Table}

object Database extends Schema {
  val messagesTable: Table[Message] = table[Message]("messages")
  val pollsTable: Table[Poll] = table[Poll]("polls")
}
