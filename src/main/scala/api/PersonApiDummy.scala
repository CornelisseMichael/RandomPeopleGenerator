package api

import scala.concurrent.ExecutionContext.Implicits.global
import app.JsonProtocols
import models.{GenerationOptions, Person}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import core.PeopleGenerator
import repositories.PersonRepository
import spray.json._

class PersonApiDummy {
  import JsonProtocols._

  val peopleApi: Route =
    (path("people") & get ) {
      complete (PersonRepository.findAll.map(_.toJson))
    }~
      (path("person") & get) { parameter("id") { (id) =>
        complete(PersonRepository.findById(id.toInt).map(_.toJson))
      }}~
      (path("person") & post) { entity(as[Person]) { user =>
        complete (PersonRepository.create(user).map(_.toJson))
      }
      }~
      (path("person"/IntNumber) & put) { id => entity(as[Person]) { user =>
        complete (PersonRepository.update(user, id).map(_.toJson))
      }
      }~
      (path("person"/IntNumber) & delete) { userId =>
        complete (PersonRepository.delete(userId).map(_.toJson))
      }~
    (path("person") & post) {
      entity(as[GenerationOptions]) { options =>
        complete(PersonRepository.createPeople(PeopleGenerator.generatePeople(options).get).map(_.toJson))
      }
    }

}
