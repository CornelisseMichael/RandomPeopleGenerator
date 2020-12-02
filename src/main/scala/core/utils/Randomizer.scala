package core.utils

import scala.util.Random

object Randomizer {

  /**
   * Returns a random string of given length
   *
   * @param length of the String
   * @param chars  Characters to use in the String
   * @return Randomly generated String
   */
  def randomString(length: Int, chars: String): String = {
    require(chars.length > 0)
    if (chars.length == 1) {
      chars.take(1) * length
    } else {
      (1 to length).map { _ => randomChoice(chars) }.mkString
    }
  }

  /**
   * Returns random choice from input String sequence
   *
   * @param seq input String sequence
   * @tparam T generic type
   * @return Random choice from String sequence
   */
  def randomChoice[T](seq: IndexedSeq[T]): T = {
    seq(Random.nextInt(seq.length))
  }

  /**
   * Returns random choice from input array
   *
   * @param a input array
   * @tparam T generic type
   * @return Random choice from array
   */
  def randomChoice[T](a: Array[T]): T = {
    a(Random.nextInt(a.length))
  }

}
