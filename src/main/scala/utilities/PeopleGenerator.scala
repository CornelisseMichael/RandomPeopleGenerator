package utilities

import models.Person

import scala.util.{Failure, Try}

object PeopleGenerator {

  /**
   * Makes new Person object
   *
   * @param lastnames  Random lastname as String
   * @param firstnames Random firstname as String
   * @param sex        Persons gender
   * @return new Person
   */
  private def makePerson(lastnames: Array[String], firstnames: Array[String], sex: Char): Person = {
    Person(
      lastname =  Randomizer.randomChoice(lastnames),
      firstname = Randomizer.randomChoice(firstnames),
      sex = sex,
      birthdate = Randomizer.generateRandomDate(),
      zipcode = Randomizer.generateRandomZipcode()
    )
  }

  /**
   * Generates random people
   *
   * @param totalPeople   total amount of people that need to be generated
   * @param femalePercent female percentage of the generated people
   * @param malePercent   male percentage of the generated people
   * @return LazyList of randomly generated people
   */
  def generatePeople(totalPeople: Int,
                     femalePercent: Int,
                     malePercent: Int): Try[LazyList[Person]] = {

    for {lastNames <- FileReader.readFile("/names/last_names.txt")
         maleFirstNames <- FileReader.readFile("/names/male_first_names.txt")
         femaleFirstNames <- FileReader.readFile("/names/female_first_names.txt")
         people <- generatePeopleStream(
           lastNames,
           femaleFirstNames,
           maleFirstNames,
           totalPeople,
           femalePercent,
           malePercent
         )}
      yield people
  }

  /**
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
                                   malePercent: Int
                                  ):
  Try[LazyList[Person]] = {

    /**
     *
     * @param malesLeft   amount of males that are left to be generated
     * @param femalesLeft amount of females left to be generated
     * @return
     */
    def generate(malesLeft: Int, femalesLeft: Int): LazyList[Person] = {
      if (femalesLeft > 0) {
        makePerson(lastNames, femaleFirstNames, 'F') #::
          generate(malesLeft, femalesLeft - 1)
      }
      else if (malesLeft > 0) {
        makePerson(lastNames,maleFirstNames, 'M') #::
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
}
