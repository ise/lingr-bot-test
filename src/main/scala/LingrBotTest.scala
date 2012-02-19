import scala.io.Source
import scala.util.Random
import collection.mutable.ArrayBuffer
import xml.XML

import org.scalatra._
import com.mongodb.casbah.Imports._
import net.liftweb.json._


class LingrBotTest extends ScalatraServlet {
  //val mongo = MongoConnection("localhost",27017)("lingr-bot-test")("places")
  val rand = new Random(System.currentTimeMillis());
  
  post("/message") {
    val json = parse(request.body)
    try {
      val msg = (json \\ "events" \ "message").values.asInstanceOf[Map[String,String]]
      //println(msg("text") + " by " + msg("nickname"))

      response.setStatus(200)
      response.addHeader("Content-type","text/plain")
      renderResponse(getResponse(msg("text")))
    } catch {
      case e:Exception => {
        response.setStatus(500)
        response.addHeader("Content-type","text/plain")
        renderResponse("Internal Server Error")
      }
    }
  }

  get("/test") {
    //println(request.body)
    val data = """{
     "status" : "ok",
     "counter":208,
     "events":[
      {"event_id":208,
       "message":
        {"id":82,
         "room":"myroom",
         "public_session_id":"UBDH84",
         "icon_url":"http://example.com/myicon.png",
         "type":"user",
         "speaker_id":"kenn",
         "nickname":"Kenn Ejima",
         "text":"yay!",
         "timestamp":"2011-02-12T08:13:51Z",
         "local_id":"pending-UBDH84-1"}}]}"""
    val json = parse(data)
    val msg = (json \\ "events" \ "message").values.asInstanceOf[Map[String,String]]
    println(msg)

    println(getResponse("お腹空いた"))
    println(getResponse("どっか行きたい"))
    println(getResponse("こんにちは"))
  }
  
  def getResponse(message: String): String = {
    def getRestaurant(): String = {
      val url = "http://search.olp.yahooapis.jp/OpenLocalPlatform/V1/localSearch?appid=cV8qsbmxg67L0Z7MV1B7vtwGTL5uf2wHPQhZPkam8Wfjp_.7SpgzAEn9cID00NXUcpqY&results=10&output=json&gc=01&ac=13"
      val source = Source.fromURL(url, "utf-8")
      val json = parse(source.getLines.mkString)
      var results = new ArrayBuffer[String]
      (json \\ "Feature").children.foreach(f => {
        results += "%s http://loco.yahoo.co.jp/place/%s/".format(
          (f \ "Name").values,
          (f \ "Property" \ "Uid").values
        )
      })
      val i = rand.nextInt(results.length)
      results(i)
    }
    def getEvent(): String = {
      val url = "http://www.tokyoartbeat.com/list/event_searchNear?Latitude=35.671208&Longitude=139.76517&SortOrder=mostpopular&SearchRange=3000m"
      val s = Source.fromURL(url, "utf-8")
      val xml = XML.loadString(s.getLines.mkString)
      var results = new ArrayBuffer[String]
      (xml \ "Event").foreach(event => {
        val eventUrl = (event \ "@href").text
        (event \ "_").foreach(
          _ match {
            case <Name>{name}</Name> => results += "%s %s".format(name, eventUrl)
            case _ => Unit
          })
      })
      val i = rand.nextInt(results.length)
      results(i)
    }
    val restPat = """.*お腹空いた.*""".r
    val eventPat = """.*どっか行きたい.*""".r
    message match {
      case restPat() => getRestaurant() + " とかどうでしょう"
      case eventPat() => getEvent() + " とかどうでしょう"
      case _ => message + "？"
    }
  }

}


