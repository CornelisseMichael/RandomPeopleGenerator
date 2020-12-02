package api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.JsonProtocols
import core.utils.PeopleGenerator
import models.GenerationOptions
import repositories.PersonRepository
import spray.json.enrichAny

import scala.concurrent.ExecutionContext.Implicits.global

trait PeopleRoutes extends JsonProtocols {

  val peopleRoutes: Route = {
    concat(
      post {
        entity(as[GenerationOptions]) { options =>
          onSuccess(PersonRepository.createPeople(PeopleGenerator.generatePeople(options).get).map(_.toJson))
          { performed =>
            complete((StatusCodes.Created, performed))
          }
        }
      } ~ get {
        complete(PersonRepository.findAll.map(_.toJson))
      } ~ delete {
        onSuccess(PersonRepository.deleteAll().map(_.toJson)) { performed =>
          complete(("All people deleted", performed))
        }
      }
    )
  }
}
