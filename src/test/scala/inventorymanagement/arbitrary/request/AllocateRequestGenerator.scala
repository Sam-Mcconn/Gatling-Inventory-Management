package inventorymanagement.arbitrary.request

import inventorymanagement.request.AllocateRequest
import org.scalacheck.Gen
import org.scalacheck.Gen.alphaLowerChar

import scala.util.Random

object AllocateRequestGenerator extends TestGenerator[AllocateRequest] {

  private val MAX_ORDER_SIZE = 2

  override val generator: Gen[AllocateRequest] = for {
    locationId <- Gen.oneOf(LOCATIONS)
    orderId <- Gen.stringOfN(4, alphaLowerChar)
    numItems = Random.between(1, MAX_ORDER_SIZE+1)
    items <- ItemGenerator.itemsGenerator(numItems)
  } yield {
    AllocateRequest(locationId, orderId, items)
  }
}
