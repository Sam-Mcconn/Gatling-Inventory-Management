package inventorymanagement.arbitrary.request

import org.scalacheck.Gen
import org.scalacheck.rng.Seed

trait TestGenerator[T] {

  def generator: Gen[T]

  def instance: T = apply()
  def apply(seed: Long = scala.util.Random.nextLong()): T =
    generator.pureApply(Gen.Parameters.default, Seed(seed))
}
