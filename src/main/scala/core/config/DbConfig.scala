package core.config

import slick.jdbc.PostgresProfile.api._
trait DbConfig extends Config {
  // for the other way uncommented these lines and comment top import
  // val driver = slick.jdbc.PostgresProfile
  // import driver.api._

 // def db = Database.forConfig("database")

  val db = Database.forURL(url = dbUrl, user = dbUser, password = dbPassword, driver = "org.postgresql.Driver")

  implicit val session: Session = db.createSession()

//  def closeDb(): Unit = db.close()

}
/*
trait DbConfig extends Config{
  val driver = slick.jdbc.PostgresProfile

  import driver.api._

  def db = Database.forConfig("my-app.db")

  implicit val session: Session = db.createSession()
}

 */
