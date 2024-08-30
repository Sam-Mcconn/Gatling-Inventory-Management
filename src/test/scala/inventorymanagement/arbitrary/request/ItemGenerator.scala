package inventorymanagement.arbitrary.request

import inventorymanagement.request.{Expiry, Item}
import org.scalacheck.Gen

import scala.annotation.tailrec
import scala.util.Random

object ItemGenerator extends TestGenerator[Item] {

  private val MAX_QUANTITY = 4
  private val MIN_QUANTITY = 1

  private val NUM_EXPIRIES = 2

  // Creates a pool of unique expiries (of size NUM_EXPIRIES)
  // WARNING: this could enter an infinite loop if NUM_EXPIRIES exceeds the total possible unique expiries
  //          Furthermore, if NUM_EXPIRIES is very close in size to the total possible unique expiries then the runtime
  //          of the generation of this value will increase exponentially
  private val EXPIRIES = (1 to NUM_EXPIRIES).foldLeft(Set[Expiry]()) {
    case (expiries, nextSize) =>
      @tailrec
      def addExpiry(): Set[Expiry] = {
        val nextSet = expiries + ExpiryGenerator.instance
        if (nextSet.size == nextSize) {
          nextSet
        } else {
          addExpiry()
        }
      }
      addExpiry()
  }

  override val generator: Gen[Item] = for {
    itemId <- itemIdGenerator
    expiry <- Gen.oneOf(EXPIRIES)
    quantity = Random.between(MIN_QUANTITY, MAX_QUANTITY + 1)
  } yield {
    inventorymanagement.request.Item(itemId, expiry, quantity)
  }

  def itemsGenerator(
      amountToGen: Int
  ): Gen[Seq[Item]] =
    Gen.listOfN(amountToGen, generator)

  // 10 elements. Chosen so that the test has simple, consistent, and easy to read itemIds
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
    "Neon"
  )

}
