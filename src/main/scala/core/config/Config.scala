package core.config

import com.typesafe.config.ConfigFactory
/*
trait Config {
  private val config = ConfigFactory.load()
  lazy val serverInterface: String = config.getString("my-app.http.interface")
  lazy val serverPort: Int = config.getInt("my-app.http.port")

  lazy val dbUrl: String = config.getString("my-app.db.url")
  lazy val dbUser: String = config.getString("my-app.db.user")
  lazy val dbPassword: String = config.getString("my-app.db.password")
}

 */
import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  //private val httpConfig = config.getConfig("my-app.http")
  val serverInterface = config.getString("my-app.http.interface")
  val serverPort = config.getInt("my-app.http.port")

  val dbUrl = config.getString("my-app.db.url")
  val dbUser = config.getString("my-app.db.user")
  val dbPassword = config.getString("my-app.db.password")

}