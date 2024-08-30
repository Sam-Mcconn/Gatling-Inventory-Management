package inventorymanagement.arbitrary.request

import inventorymanagement.request.CollectRequest
import org.scalacheck.Gen
import org.scalacheck.Gen.alphaLowerChar

import scala.util.Random

object CollectRequestGenerator extends TestGenerator[CollectRequest] {

  private val MAX_ORDER_SIZE = 2

  override val generator: Gen[CollectRequest] = for {
    locationId <- Gen.oneOf(LOCATIONS)
    orderId <- Gen.stringOfN(10, alphaLowerChar)
    numItems = Random.between(1, MAX_ORDER_SIZE+1)
    items <- ItemGenerator.itemsGenerator(numItems)
  } yield {
    CollectRequest(locationId, orderId, items)
  }

  def generatorWithOrderId(orderId: String): Gen[CollectRequest] = for {
    locationId <- Gen.oneOf(LOCATIONS)
    numItems = Random.between(1, MAX_ORDER_SIZE+1)
    items <- ItemGenerator.itemsGenerator(numItems)
  } yield {
    CollectRequest(locationId, orderId, items)
  }
}
