import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MyFirstTest extends Simulation{
  val httpConf = http.baseUrl(url= "http://localhost:8080/app/")
    .header(name = "Accept", value="application/json")
    .proxy(Proxy("localhost",8888))

  val scn = scenario(scenarioName = "My First Test")
    .exec(http(requestName = "Get All Games")
        .get("videogames"))

  setUp(
    scn.inject(atOnceUsers(users =1))
  ).protocols(httpConf)
}
