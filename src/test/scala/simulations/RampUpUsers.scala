package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

import scala.concurrent.duration._;


class RampUpUsers extends Simulation{

  val httpConf = http.baseUrl(url= "http://localhost:8080/app/")
    .header(name = "Accept", value="application/json")

  def getAllVideoGames(): ChainBuilder = {
    exec(http(requestName = "Get All Games")
      .get("videogames")
      .check(status.is(200))
      .check(jsonPath("$[1].id").saveAs("gameId")))
  }

  def getSpecificGame(): ChainBuilder = {
    repeat(5){
      exec(http("Get Specific game")
        .get("videogames/${gameId}")
        .check(status.in(200 to 210))
        .check(jsonPath("$.name").is("Gran Turismo 3")))
    }
  }

  val scn = scenario(scenarioName = "1000 Users Ramp with 10 seconds")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .pause(1,7)
    .exec(getAllVideoGames())


  setUp(
    scn.inject(
      nothingFor(5),
      atOnceUsers(100),
      rampUsers(1000).during(10 seconds)
    )
  ).protocols(httpConf.inferHtmlResources())
}
