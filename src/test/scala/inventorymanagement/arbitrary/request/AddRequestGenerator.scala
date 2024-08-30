package inventorymanagement.arbitrary.request

import inventorymanagement.request.AddRequest
import org.scalacheck.Gen

import scala.util.Random

object AddRequestGenerator extends TestGenerator[AddRequest] {

  private val MAX_ITEMS = 4
  private val MIN_ITEMS = 1

  override val generator: Gen[AddRequest] = for {
    locationId <- Gen.oneOf(LOCATIONS)
    numItems = Random.between(MIN_ITEMS, MAX_ITEMS + 1)
    items <- ItemGenerator.itemsGenerator(numItems)
  } yield {
    AddRequest(locationId, items)
  }

}
