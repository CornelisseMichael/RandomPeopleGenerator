package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import spray.json.enrichAny
import core.JsonProtocols
import models.Person
import repositories.PersonRepository

trait PersonRoutes extends JsonProtocols {

  val personRoutes: Route = {
    concat(
      post {
        entity(as[Person]) { person =>
          onSuccess(PersonRepository.create(person).map(_.toJson)) { performed =>
            complete((StatusCodes.Created, performed))
          }
        }
      } ~ parameter("id".as[Int]) { id =>
        concat(
          delete {
            onSuccess(PersonRepository.delete(id).map(_.toJson)) { performed =>
              complete((s"Person with $id deleted", performed))
            }
          } ~ put(
            entity(as[Person]) { person => {
              onSuccess(PersonRepository.update(person, id).map(_.toJson)) { performed =>
                complete((s"Person with $id updated", performed))
              }
            }
            }
          )
        )
      }
    )
  }
}
