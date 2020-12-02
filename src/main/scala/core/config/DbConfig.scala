package core.config

import slick.jdbc.PostgresProfile.api._

/**
 * Starts database session
 */
trait DbConfig extends Config {

  val db = Database.forURL(
    url = dbUrl,
    user = dbUser,
    password = dbPassword,
    driver = "org.postgresql.Driver")

  implicit val session: Session = db.createSession()

  //  def closeDb(): Unit = db.close()
}


