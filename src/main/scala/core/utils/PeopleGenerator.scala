package core.utils

import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom

import Randomizer.randomString
import models.{GenerationOptions, Person}

import scala.util.{Random, Try}

object PeopleGenerator {

  /**
   * Makes new Person object
   *
   * @param lastnames  Random lastname as String
   * @param firstnames Random firstname as String
   * @param sex        Persons gender
   * @return new Person
   */
  private def makePerson(lastnames: Array[String], firstnames: Array[String],
                         sex: String, from: Int, to: Int): Person = {
    Person(
      id = None,
      lastname = Randomizer.randomChoice(lastnames),
      firstname = Randomizer.randomChoice(firstnames),
      sex = sex,
      birthdate = generateRandomDate(from, to),
      zipcode = generateRandomZipcode()
    )
  }

  /**
   * Yields LazyList of randomly generated people
   *
   * @param generationOptions number of people, female percentage, male percentage, from and to year
   * @return
   */
  def generatePeople(generationOptions: GenerationOptions): Try[LazyList[Person]] = {

    for {lastNames <- FileReader.readFile("/names/last_names.txt")
         maleFirstNames <- FileReader.readFile("/names/male_first_names.txt")
         femaleFirstNames <- FileReader.readFile("/names/female_first_names.txt")
         people <- generatePeopleStream(
           lastNames,
           femaleFirstNames,
           maleFirstNames,
           generationOptions.totalPeople,
           generationOptions.femalePercent,
           generationOptions.malePercent,
           generationOptions.from,
           generationOptions.to
         )}
      yield people
  }

  /**
   * Creates stream of randomly generated people
   *
   * @param lastNames        Array of lastNames
   * @param femaleFirstNames Array of femaleFirstnames
   * @param maleFirstNames   Array of maleFirstNames
   * @param totalPeople      Total amount of people we want to generate
   * @param femalePercent    Total percentage of female population
   * @param malePercent      Total percentage of male population
   * @return LazyList of generated people
   */
  private def generatePeopleStream(lastNames: Array[String],
                                   femaleFirstNames: Array[String],
                                   maleFirstNames: Array[String],
                                   totalPeople: Int,
                                   femalePercent: Int,
                                   malePercent: Int,
                                   from: Int,
                                   to: Int
                                  ):
  Try[LazyList[Person]] = {

    /**
     * Divides the percentage of female and male people
     *
     * @param malesLeft   amount of males that are left to be generated
     * @param femalesLeft amount of females left to be generated
     * @return
     */
    def generate(malesLeft: Int, femalesLeft: Int): LazyList[Person] = {
      if (femalesLeft > 0) {
        makePerson(lastNames, femaleFirstNames, "F", from, to) #::
          generate(malesLeft, femalesLeft - 1)
      }
      else if (malesLeft > 0) {
        makePerson(lastNames, maleFirstNames, "M", from, to) #::
          generate(malesLeft - 1, femalesLeft)
      }
      else
        LazyList.empty
    }

    val totalMales = (totalPeople * malePercent) / 100
    val w = (totalPeople * femalePercent) / 100
    val totalFemales = w + math.abs(totalPeople - totalMales - w)

    Try {
      generate(totalMales, totalFemales)
    }
  }

  /**
   * Generates random date
   *
   * @return RandomLocalDate.ToString
   */
  private def generateRandomDate(from: Int, to: Int): String = {

    // Sanity check to make sure user doesn't input start year that exceeds end year
    // If so revert to some generic date
    val start = if (from < to) LocalDate.of(from, 1, 1)
    else LocalDate.of(1920, 1, 1)

    // Sanity check to make sure user doesn't input to year that precedes start year
    // If so revert back to LocalDate.now
    val end = if (to > from) LocalDate.of(to, 2, 1) else LocalDate.now

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
  private def generateRandomZipcode(): String = {
    val numbers = Random.between(1000, 9999)
    val letters = randomString(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    s"$numbers$letters"
  }
}
