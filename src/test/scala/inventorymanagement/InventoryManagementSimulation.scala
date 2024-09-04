package inventorymanagement

import inventorymanagement.arbitrary.request.{
  AddRequestGenerator,
  AllocateRequestGenerator
}
import inventorymanagement.request.{AllocateRequest, CollectRequest}
import io.circe.generic.auto._
import io.circe.syntax._
import io.gatling.core.Predef._
import io.gatling.core.feeder.Feeder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.AbstractIterator
import scala.concurrent.duration.DurationInt
import scala.util.Random

class InventoryManagementSimulation extends Simulation {

  val httpProtocol: HttpProtocolBuilder =
    http
      .baseUrl("https://enwcdi4ev5.execute-api.us-east-1.amazonaws.com/dev")
      .acceptHeader("application/json")
      .contentTypeHeader("application/json")

  val feeder: Feeder[String] = new AbstractIterator[Map[String, String]] {

    private[this] val allocatedOrders =
      new ConcurrentLinkedQueue[AllocateRequest]()

    override def hasNext: Boolean = true

    override def next(): Map[String, String] = {
      math.abs(Random.nextInt()) % 100 match {
        // Emit an ADD request half of the time
        case seed if seed < 100 =>
          Map(
            "endpoint" -> "/add",
            "body" -> generateAddRequestBody
          )

        // Emit an ALLOCATE request a quarter of the time
        case seed if seed < 75 =>
          val allocateRequest = AllocateRequestGenerator.instance
          allocatedOrders.add(allocateRequest)
          Map(
            "endpoint" -> "/allocate",
            "body" -> allocateRequest.asJson.noSpaces
          )

        // Emit a COLLECT request in remaining cases
        case _ =>
          val allocation =
            try {
              allocatedOrders.remove()
            } catch {
              case _: NoSuchElementException =>
                // If there are no allocated orders, just make a COLLECT request based on a non-existent ALLOCATE.
                // The server should simply do nothing with this request
                AllocateRequestGenerator.instance
            }

          val collect = CollectRequest(
            allocation.locationId,
            allocation.orderId,
            allocation.items
          )

          Map(
            "endpoint" -> "/collect",
            "body" -> collect.asJson.noSpaces
          )
      }
    }
  }

  val request: ScenarioBuilder = scenario("Load Test Requests")
    .feed(feeder)
    .doIf(session => session("endpoint").as[String] == "/add")(
      http("ADD")
        .post(session => session("endpoint").as[String])
        .body(StringBody(session => session("body").as[String]))
    )
    .doIf(session => session("endpoint").as[String] == "/allocate")(
      http("ALLOCATE")
        .put(session => session("endpoint").as[String])
        .body(StringBody(session => session("body").as[String]))
    )
    .doIf(session => session("endpoint").as[String] == "/collect")(
      http("COLLECT")
        .delete(session => session("endpoint").as[String])
        .body(StringBody(session => session("body").as[String]))
    )

  // @@@@@ THIS IS WHERE YOU DEFINE THE RATE AND DURATION OF THE TEST @@@@@
  setUp(request.inject(constantUsersPerSec(10000).during(60.seconds)))
    .protocols(httpProtocol)

  private def generateAddRequestBody: String = {
    val request = AddRequestGenerator.instance
    request.asJson.noSpaces
  }

}
