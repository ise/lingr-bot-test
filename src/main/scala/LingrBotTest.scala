import org.scalatra._
import com.mongodb.casbah.Imports._

class LingrBotTest extends ScalatraServlet {

  //setup Casbah connection
  //val mongo = MongoConnection("localhost",27017)("lingr-bot-test")("places")
  
  post("/message") {
    println(request.body)
    //パラメータを受け取る
    //json parse
    //お腹空いた => localsearch api
    //今新宿にいる => place保存
    //なんか見たい => tokyo art beat api
    //他 => default
  }

  get("/test") {
    println(request.body)
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
