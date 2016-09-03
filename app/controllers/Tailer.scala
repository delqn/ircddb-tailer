package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


///@Singleton
class Tailer @Inject() (ws: WSClient) extends Controller {

  val BASE_URL = "http://live2.ircddb.net:8080/jj3.yaws"

  def index = Action {
    request =>
      val sinceID = 0
      val url = s"$BASE_URL?p=$sinceID"
      ws.url(url).get().map {
        resp => println(resp.body)
      }

      Logger.info("hello")
      Ok("hi")
  }

}