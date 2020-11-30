package core

import scala.util.Random

object Randomizer {

  /**
   * Returns a random string of given length
   * @param length of the String
   * @param chars Characters to use in the String
   * @return Randomly generated String
   */
  def randomString(length: Int,
                   chars:  String): String = {
    require(chars.length > 0)
    if (chars.length == 1)
      chars.take(1) * length
    else
      (1 to length).map { _ => randomChoice(chars) }.mkString
  }

  def randomChoice[T](seq: IndexedSeq[T]): T = seq(Random.nextInt(seq.length))

  def randomChoice[T](a: Array[T]): T = a(Random.nextInt(a.length))
}
