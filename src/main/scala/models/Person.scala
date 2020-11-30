package models

import java.time.LocalDate

final case class Person(
                         id: Option[Int],
                         lastname: String,
                         firstname: String,
                         sex: Char,
                         birthdate: String,
                         zipcode: String,
                       )
