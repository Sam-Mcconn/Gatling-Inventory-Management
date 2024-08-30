package inventorymanagement.arbitrary.request

import org.scalacheck.Gen

import java.time.YearMonth

object ExpiryGenerator extends TestGenerator[inventorymanagement.request.Expiry] {

  private val MIN_YEAR = 2025
  private val MAX_YEAR = 2030

  private val MIN_MONTH = 1
  private val MAX_MONTH = 12


  override val generator: Gen[inventorymanagement.request.Expiry] = for {
    year <- uniformGen(MIN_YEAR, MAX_YEAR)
    month <- uniformGen(MIN_MONTH, MAX_MONTH)
    day <- uniformGen(1, YearMonth.of(year, month).lengthOfMonth())
  } yield {
    inventorymanagement.request.Expiry(year, month, day)
  }

  private def uniformGen(min: Int, max: Int): Gen[Int] = Gen.double.map { ratio =>
    val distance = max+1-min
    (ratio*distance).toInt + min
  }

}
