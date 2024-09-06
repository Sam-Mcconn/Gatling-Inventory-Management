package inventorymanagement

import inventorymanagement.arbitrary.request.{AddRequestGenerator, AllocateRequestGenerator}
import inventorymanagement.request.CollectRequest
import io.circe.generic.auto._
import io.circe.syntax._
import io.gatling.core.Predef._
import io.gatling.core.feeder.Feeder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.collection.AbstractIterator
import scala.concurrent.duration.DurationInt
import scala.util.Random

class InventoryManagementSimulation extends Simulation {

  // endpoints
  private val ADD = "/add"
  private val ALLOCATE = "/allocate"
  private val COLLECT = "/collect"

  val httpProtocol: HttpProtocolBuilder =
    http
      .baseUrl("https://enwcdi4ev5.execute-api.us-east-1.amazonaws.com/dev")
      .acceptHeader("application/json")
      .contentTypeHeader("application/json")

  val feeder: Feeder[String] = new AbstractIterator[Map[String, String]] {

    override def hasNext: Boolean = true

    override def next(): Map[String, String] = {
      math.abs(Random.nextInt()) % 100 match {
        // Emit an ADD request two thirds of the time
        case seed if seed < 67 =>
          Map(
            "endpoint" -> ADD,
            "body" -> generateAddRequestBody
          )

        // Emit an ALLOCATE and COLLECT request a third of the time
        case _ =>
          val allocateRequest = AllocateRequestGenerator.instance
          val collectRequest = CollectRequest(
            allocateRequest.locationId,
            allocateRequest.orderId,
            allocateRequest.items
          )
          Map(
            "endpoint" -> ALLOCATE,
            "allocateBody" -> allocateRequest.asJson.noSpaces,
            "collectBody" -> collectRequest.asJson.noSpaces
          )
      }
    }
  }

  val request: ScenarioBuilder = scenario("Load Test Requests")
    .feed(feeder)
    .doIf(session => session("endpoint").as[String] == ADD)(
      http("ADD")
        .post(ADD)
        .body(StringBody(session => session("body").as[String]))
    )
    .doIf(session => session("endpoint").as[String] == ALLOCATE)(
      http("ALLOCATE")
        .put(ALLOCATE)
        .body(StringBody(session => session("allocateBody").as[String])),
      pause(10, 20),
      http("COLLECT")
        .delete(COLLECT)
        .body(StringBody(session => session("collectBody").as[String]))
    )

  // @@@@@ THIS IS WHERE YOU DEFINE THE RATE AND DURATION OF THE TEST @@@@@
  setUp(request.inject(constantUsersPerSec(10000).during(60.seconds)))
    .protocols(httpProtocol)

  private def generateAddRequestBody: String = {
    val request = AddRequestGenerator.instance
    request.asJson.noSpaces
  }

}
