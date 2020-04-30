package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class MyFirstPause extends Simulation{
    val httpConf = http.baseUrl(url= "http://localhost:8080/app/")
      .header(name = "Accept", value="application/json")


    val scn = scenario(scenarioName = "My First Test")
      .exec(http(requestName = "Get All Games")
        .get("videogames")
        .check(status.is(200))
        .check(jsonPath("$[1].id").saveAs("gameId")))
        .pause(5)

      .exec(http("Get Specific game")
        .get("videogames/${gameId}")
        .check(status.in(200 to 210))
        .check(jsonPath("$.name").is("Gran Turismo 3")))
        .pause(1,7)

      .exec(http("Get All Games")
        .get("videogames")
        .check(status.not (400 ), status.not (500 ))
        .check(bodyString.saveAs("responseBody")))
        .pause(6.milliseconds)

      .exec{session => println(session("responseBody").as[String]);session}

    setUp(
      scn.inject(atOnceUsers(users =1))
    ).protocols(httpConf)
  }



