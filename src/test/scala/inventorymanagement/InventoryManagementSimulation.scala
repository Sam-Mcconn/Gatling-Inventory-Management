package inventorymanagement

import inventorymanagement.arbitrary.request.AddRequest
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.circe.generic.auto._
import io.circe.syntax._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt

class InventoryManagementSimulation extends Simulation {

  val httpProtocol: HttpProtocolBuilder =
    http
      .baseUrl("https://enwcdi4ev5.execute-api.us-east-1.amazonaws.com/dev")
      .acceptHeader("application/json")
      .contentTypeHeader("application/json")

  val simpleAddRequest: ScenarioBuilder = scenario("Add Request").exec(
    http("Request 1")
      .post("/add")
      .body(
        StringBody { _ => generateRequestJSON }
      )
  )

  setUp(simpleAddRequest.inject(constantUsersPerSec(2).during(10.seconds)))
    .protocols(httpProtocol)


  private def generateRequestJSON: String = {
    val request = AddRequest.instance
    request.asJson.noSpaces
  }

}
