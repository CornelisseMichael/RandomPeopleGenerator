package repositories

import models.{Person, PersonTable}

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

object PersonRepository extends BaseRepository {

  /**
   * Returns all people from the database
   *
   * @return All people in the database
   */
  def findAll: Future[Seq[Person]] = {
    peopleTable.result
  }

  /**
   * Returns person with specific id from the database
   *
   * @param id of the user you want to retrieve
   * @return returns the user with specific id
   */
  def findById(id: Int): Future[Person] = {
    peopleTable.filter(_.id === id).result.head
  }

  /**
   * Returns all people with the specified gender from the database
   *
   * @param sex of the people you rant to retrieve
   * @return all the users of the specified sex
   */
  def findAllFromGender(sex: String): Future[Seq[Person]] = {
    peopleTable.filter(_.sex === sex).result
  }

  /**
   * Returns all people from a specific birthYear from the database
   *
   * @param year the birth year of the people you want to retrieve
   * @return all the users of the specified birth year
   */
  def findAllFromYear(year: String): Future[Seq[Person]] = {
    peopleTable.filter(_.birthdate.substring(0, 4) === year).result
  }

  /**
   * Returns all people between given id start and end range, optionally you can provide sex 'F' or 'M',
   * in the params to only get the results for the specified gender
   *
   * @param start Id to start from
   * @param end   Id to end at
   * @param sex   Optionally provide the gender you want to search for
   * @return all the queried people
   */
  def findAllBetweenIdRangeAndGender(start: Int, `end`: Int, sex: Option[String]): Future[Seq[Person]] = {

    if (sex.isDefined) {
      peopleTable.filter(
        people =>
          people.sex === sex
            && people.id >= start
            && people.id <= `end`)
        .sortBy(_.id.asc).result
    } else {
      peopleTable.filter(people =>
        people.id >= start
          && people.id <= `end`)
        .sortBy(_.id.asc).result
    }
  }

  /**
   * Returns all people from and to between given birth years , optionally you can provide sex 'F' or 'M',
   * in the params to only get the results for the specified gender
   *
   * @param from birth year
   * @param to   birth year
   * @param sex  Optionally provide the gender you want to search for
   * @return all the queried people
   */
  def findAllBetweenYearRangeAndGender(from: String, to: String, sex: Option[String]): Future[Seq[Person]] = {
    if (sex.isDefined) {
      peopleTable.filter(
        people =>
          people.sex === sex
            && people.birthdate.substring(0, 4) >= from
            && people.birthdate.substring(0, 4) <= to)
        .sortBy(_.birthdate.asc).result
    } else {
      peopleTable.filter(
        people =>
          people.birthdate.substring(0, 4) >= from
            && people.birthdate.substring(0, 4) <= to)
        .sortBy(_.birthdate.asc).result
    }

  }

  /**
   * Creates an individual person entry in the database
   *
   * @param person creates a person based on specific parameters
   * @return the newly created person
   */
  def create(person: Person): Future[Int] = {
    peopleTable returning peopleTable.map(_.id) += person
  }

  /**
   * Inserts batch of randomly generated people into the database
   *
   * @param people the batch of people we want to create
   * @return the batch of newly created people
   */
  def createPeople(people: Iterable[PersonTable#TableElementType]): Future[Option[Int]] = {
    peopleTable ++= people
  }

  /**
   * Returns a random batch from the database optionally a gender could be provided so get a batch for a specific gender
   *
   * @param amount batch size
   * @param sex    Optionally provide the gender you want to search for
   * @return batch of random people
   */
  def getRandomBatch(amount: Int, sex: Option[String]): Future[Seq[Person]] = {
    val rand = SimpleFunction.nullary[Int]("random")

    if (sex.isDefined) {
      peopleTable.sortBy(_ => rand).filter(_.sex === sex).take(amount).result
    } else {
      peopleTable.sortBy(_ => rand).take(amount).result
    }
  }

  /**
   * Updates an existing person in the database
   *
   * @param newPerson Updated person we want to insert for existing record
   * @param id        the id of the person we want to update
   * @return an new update person object
   */
  def update(newPerson: Person, id: Int): Future[Int] = {
    peopleTable.filter(_.id === id)
      .map(person =>
        (
          person.lastname,
          person.firstname,
          person.sex,
          person.birthdate,
          person.zipcode
        ))
      .update((
        newPerson.lastname,
        newPerson.firstname,
        newPerson.sex,
        newPerson.birthdate,
        newPerson.zipcode
      ))
  }

  /**
   *
   * @param id of the person we want to delete
   * @return deleted person
   */
  def delete(id: Int): Future[Int] = {
    peopleTable.filter(_.id === id).delete
  }

  /**
   * Deletes all users from the table
   *
   * @return deleted list of people
   */
  def deleteAll(): Future[Int] = {
    peopleTable.delete
  }

}

