package app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import api.PersonApi
import core.config.{Config, MigrationConfig}

import scala.util.{Failure, Success}

object App extends  Config with MigrationConfig{

  //#start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext
    //migrate()
     reloadSchema()

   // PeopleDao.createPeople(PeopleGenerator.generatePeople(GenerationOptions(10,50,50,1990,2000)).get.toSeq)
    // val futureBinding = Http().newServerAt(httpInterface, httpPort).bind(routes)
    val futureBinding = Http().newServerAt(serverInterface, serverPort).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  //#start-http-server
  def main(args: Array[String]): Unit = {
    //#server-bootstrapping
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val routes = new PersonApi
      startHttpServer(routes.routes)(context.system)

      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
    implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
    //#server-bootstrapping
  }
}
