package inventorymanagement.arbitrary.request

import org.scalacheck.Gen

import scala.util.Random

object AddRequest extends TestGenerator[inventorymanagement.request.AddRequest] {

  private val LOCATIONS: Seq[String] = Seq("Montreal", "Ottawa", "Vancouver")
  private val MAX_ITEMS = 4
  private val MIN_ITEMS = 1

  val generator: Gen[inventorymanagement.request.AddRequest] = for {
    locationId <- Gen.oneOf(LOCATIONS)
    numItems = Random.between(MIN_ITEMS, MAX_ITEMS+1)
    items <- Item.itemsGenerator(numItems)
  } yield {
    inventorymanagement.request.AddRequest(locationId, items)
  }

}
