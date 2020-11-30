package repositories

import models.{PersonTable, Person}

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

object PersonRepository extends BaseRepository {

  /**
   *
   * @return All people in the database
   */
  def findAll: Future[Seq[Person]] = {
    peopleTable.result
  }

  /**
   *
   * @param id of the user you want to retrieve
   * @return returns the user with specific id
   */
  def findById(id: Int): Future[Person] = {
    peopleTable.filter(_.id === id).result.head
  }

  /**
   *
   * @param sex of the people you rant to retrieve
   * @return all the users of the specified sex
   */
  def findAllFromGender(sex: Char): Future[Seq[Person]] = {
    peopleTable.filter(_.sex === sex).result
  }

  /**
   *
   * @param year the birth year of the people you want to retrieve
   * @return all the users of the specified birth year
   */
  def findAllFromYear(year: String): Future[Seq[Person]] = {
    peopleTable.filter(_.birthdate.substring(0, 4) === year).result
  }


  def findAllBetweenRange(from: String, to: String): Future[Seq[Person]] = {
    val fromId = Option(1)
    val toId = Option(3)
    peopleTable.filterOpt(fromId)(_.id > _)
      .filterOpt(toId)(_.id<_).result

  }


  def findAllBetweenYearRange(from: String, to: String): Future[Seq[Person]] = {
    val fromYear = Option(from)
    val toYear = Option(to)
    peopleTable.filterOpt(fromYear)(_.birthdate.substring(0, 4) > _)
      .filterOpt(toYear)(_.birthdate.substring(0, 4) < _).result
  }

  //def findAllFromYearBetweenRange(from: String, to:String): Future[Seq[Person]] = peopleTable.filter(_.birthdate.substring(0, 4) >= from).filter(_.birthdate.substring(0, 4) <= to).result//peopleTable.filter(_.birthdate.substring(0, 4) > from).result

  /**
   *
   * @param person creates a person based on specific parameters
   * @return the newly created person
   */
  def create(person: Person): Future[Int] = {
    peopleTable returning peopleTable.map(_.id) += person
  }

  /**
   *
   * @param p the batch of people we want to create
   * @return the batch of newly created people
   */
  def createPeople(p: Iterable[PersonTable#TableElementType]): Future[Option[Int]] = {
    peopleTable ++= p
  }

  /**
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
   * @return deleted list of people
   */
  def deleteAll(): Future[Int] = {
    peopleTable.delete
  }

}

