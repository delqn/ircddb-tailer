package controllers

import javax.inject.Inject

import parser.MessageParser
import play.api.Logger
import play.api.db.Database
import play.api.http.ContentTypes
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global

import utils.Consts.BASE_URL

class Tailer @Inject() (ws: WSClient, db: Database) extends Controller {

  def index = Action { Ok("hi") }

  def poll = Action.async {
    implicit request =>
      val sinceID = persistence.Message.lastMessage.rowid
      val url = s"$BASE_URL?p=$sinceID"
      ws.url(url).get().map {
        resp =>
          val messages = MessageParser
            .getLines(resp.body)
            .map(MessageParser.parse)
            .map(persistence.Message.create)

          val json = Json.toJson(messages.map(_.toMap).toArray)
          persistence.Poll.create(url)
          Logger.debug(s"Fetched $json")
          Ok(json).as(ContentTypes.JSON)
      }
  }

  def dump = Action {
    val messages = persistence.Message.findAll
    val json = Json.toJson(messages.map(_.toMap))
    Ok(json)
  }

  def stats = Action {
    val status = persistence.Message.getStatus
    val json = Json.toJson(status)
    Ok(json)
  }
}