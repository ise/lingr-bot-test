import ch.epfl.lamp.fjbg.JField
import org.scalatra._
import com.mongodb.casbah.Imports._
import scala.util.parsing.json.JSON
import net.liftweb.json._
import net.liftweb.json.JsonAST.JField

class LingrBotTest extends ScalatraServlet {

  //setup Casbah connection
  //val mongo = MongoConnection("localhost",27017)("lingr-bot-test")("places")
  
  post("/message") {
    val json = parse(request.body)
    val text = (json \\ "events" \ "message" \ "text").toString
    val nickname = (json \\ "events" \ "message" \ "nickname").toString
    //パラメータを受け取る
    //json parse
    //お腹空いた => localsearch api
    //今新宿にいる => place保存
    //なんか見たい => tokyo art beat api
    //他 => default
    text + " by " + nickname
  }

  get("/test") {
    //println(request.body)
    val data = """{
     "status" : "ok",
     "counter":208,
     "events":[
      {"event_id":208
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
    val text = (json \\ "events" \ "message" \ "text").toString
    val nickname = (json \\ "events" \ "message" \ "nickname").toString
    println(text)
    println(nickname)
  }

  /*
  post("/msgs") {
    val builder = MongoDBObject.newBuilder
    params.get("body").foreach(msg => {
      builder += ("body" -> msg)
      mongo += builder.result
    })
    redirect("/msgs")
  }
  
  get("/msgs") {
    <body>
      <ul>
        {for (msg <- mongo) yield <li>{msg.get("body")}</li>}
      </ul>
      <form method="POST" action="/msgs">
        <input type="text" name="body"/>
        <input type="submit"/>
      </form>
    </body>  
  }
  */

}


