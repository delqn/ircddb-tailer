package controllers

import javax.inject.Inject

import parser.{Message, MessageParser}
import play.api.Logger
import play.api.http.{ContentTypeOf, ContentTypes}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{Action, Codec, Controller}
import play.api.libs.json._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global


///@Singleton
class Tailer @Inject() (ws: WSClient) extends Controller {

  val BASE_URL = "http://live2.ircddb.net:8080/jj3.yaws"

  def index = Action.async {
    implicit request =>
      val sinceID = 0
      val url = s"$BASE_URL?p=$sinceID"
      val messages = new ArrayBuffer[Message]
      ws.url(url).get().map {
        resp =>
          val messages = MessageParser.getLines(resp.body).map(MessageParser.parse)
          val json = Json.toJson(messages.map(_.toMap).toArray)
          Ok(json).as(ContentTypes.JSON)
      }
  }

}