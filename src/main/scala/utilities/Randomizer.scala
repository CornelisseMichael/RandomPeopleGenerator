package utilities

import java.time.LocalDate
import scala.util.Random
import java.util.concurrent.ThreadLocalRandom

object Randomizer {

  /**
   * Generates random birthdate
   *
   * @return RandomLocalDate.ToString
   */
   def generateRandomDate(): String = {
    val start = LocalDate.of(1920, 1, 1)
    val end = LocalDate.now
    val startEpochDay = start.toEpochDay
    val endEpochDay = end.toEpochDay
    val randomDay = ThreadLocalRandom.current.nextLong(startEpochDay, endEpochDay)
    LocalDate.ofEpochDay(randomDay).toString
  }

  /**
   * Generates random zipcode
   *
   * @return Random zipcode as String
   */
   def generateRandomZipcode(): String = {
    val numbers = Random.between(1000, 9999)//RandomUtil.randomIntBetween(1000, 9999)
    val letters = randomString(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    s"$numbers$letters"
  }

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
