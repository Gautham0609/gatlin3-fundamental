package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder

class CsvFeeder extends Simulation{

  val httfConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")

  val csvfeeder = csv("data/csvFeeder.csv").circular

  val idNumbers = (1 to 10).iterator

  val customerFeeder = Iterator.continually(Map("gameId" -> idNumbers.next()))


  def getSpecifVideoGames():ChainBuilder ={
    repeat(10){
      feed(csvfeeder)
      .exec(http("Get Specific game")
        .get("videogames/${gameId}")
        .check(status.in(200 to 210))
        .check(jsonPath("$.name").is("${gameName}")))
    }
  }

  val scn = scenario("Csv feeder get simulation")
    .exec(getSpecifVideoGames())

  setUp(
    scn.inject(atOnceUsers(1))
      .protocols(httfConf)
  )
}
