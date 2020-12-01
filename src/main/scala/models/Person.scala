package models

/**
 * Main model of the application
 *
 * @param id        Id of the person
 * @param lastname  Persons lastname
 * @param firstname Persons firstname
 * @param sex       Persons gender
 * @param birthdate Persons birthdate
 * @param zipcode   Persons zipcode
 */
final case class Person(
                         id: Option[Int] = None,
                         lastname: String,
                         firstname: String,
                         sex: String,
                         birthdate: String,
                         zipcode: String,
                       )
