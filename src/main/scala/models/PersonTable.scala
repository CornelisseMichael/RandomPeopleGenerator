package models

import slick.jdbc.PostgresProfile.api._

class PersonTable(tag: Tag)extends Table[Person](tag, "people") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def lastname = column[String]("lastname")
  def firstname = column[String]("firstname")
  def sex = column[Char]("sex")
  def birthdate = column[String]("birthdate")
  def zipcode = column[String]("zipcode")
  def * =
    (id.?, lastname, firstname, sex, birthdate, zipcode)<> ((Person.apply _).tupled, Person.unapply)
}
