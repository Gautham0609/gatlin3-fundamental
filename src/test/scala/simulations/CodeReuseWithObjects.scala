package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class CodeReuseWithObjects extends Simulation{
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

    val scn = scenario(scenarioName = "My First Test")
      .exec(getAllVideoGames())
      .pause(5)
      .exec(getSpecificGame())
      .pause(1,7)
      .exec(getAllVideoGames())
      .pause(6.milliseconds)

    setUp(
      scn.inject(atOnceUsers(users =1))
    ).protocols(httpConf)
  }



