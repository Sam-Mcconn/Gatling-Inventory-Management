package inventorymanagement.arbitrary.request

import org.scalacheck.Gen
import org.scalacheck.Gen.alphaLowerChar

import scala.util.Random

object Item extends TestGenerator[inventorymanagement.request.Item] {

  private val MAX_QUANTITY = 10
  private val MIN_QUANTITY = 1

  val generator: Gen[inventorymanagement.request.Item] = for {
    itemId <- itemIdGenerator
    expiry <- Expiry.generator
    quantity = Random.between(MIN_QUANTITY, MAX_QUANTITY + 1)
  } yield {
    inventorymanagement.request.Item(itemId, expiry, quantity)
  }

  def itemsGenerator(
      amountToGen: Int
  ): Gen[Seq[inventorymanagement.request.Item]] =
    Gen.listOfN(amountToGen, generator)

  // 20 elements. Chosen so that the test has simple, consistent, and easy to read itemIds
  private def itemIdGenerator: Gen[String] = Gen.oneOf(
    "Hydrogen",
    "Helium",
    "Lithium",
    "Beryllium",
    "Boron",
    "Carbon",
    "Nitrogen",
    "Oxygen",
    "Fluorine",
    "Neon",
    "Sodium",
    "Magnesium",
    "Aluminum",
    "Silicon",
    "Phosphorus",
    "Sulfur",
    "Chlorine",
    "Argon",
    "Potassium",
    "Calcium"
  )

}
