import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.{Session, SessionFactory}

import scala.io.Source
import play.api.Logger
import play.api.libs.json.Json
import parser.MessageParser

import utils.Consts.BASE_URL

object Loader extends App {
  val dbURL = sys.env("DATABASE_URL")
  SessionFactory.concreteFactory = Some(() =>
    Session.create(
      java.sql.DriverManager.getConnection(dbURL),
      new PostgreSqlAdapter
    )
  )

  Session.create(
    java.sql.DriverManager.getConnection(dbURL),
    new PostgreSqlAdapter
  ).bindToCurrentThread

  val sinceID = persistence.Message.lastMessage.rowid
  val url = s"$BASE_URL?p=$sinceID"
  val resp = Source.fromURL(url)("utf-8").mkString
  val messages = MessageParser
    .getLines(resp)
    .map(MessageParser.parse)
    .map(persistence.Message.create)

  val json = Json.toJson(messages.map(_.toMap).toArray)
  Logger.debug(s"[${this.getClass.getName}] Fetched $json")
  persistence.Poll.create(url)
}
