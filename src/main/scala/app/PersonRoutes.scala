package app

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import app.PersonController._
import models.{GenerationOptions, People, Person}

class PersonRoutes(personController: ActorRef[PersonController.Command])(implicit val system: ActorSystem[_]) {

  //#people-routes-class

  import JsonProtocols._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getPeople: Future[People] =
    personController.ask(GetPeople)

  def getPerson(name: String): Future[GetPersonResponse] =
    personController.ask(GetPerson(name, _))

  def createPerson(person: Person): Future[ActionPerformed] =
    personController.ask(CreatePerson(person, _))

  def generatePeople(totalPeople: GenerationOptions, femalePercent: GenerationOptions,
                     malePercent: GenerationOptions): Future[ActionPerformed] =
    personController.ask(GeneratePeople(totalPeople, femalePercent, malePercent, _))

  def deletePerson(name: String): Future[ActionPerformed] =
    personController.ask(DeletePerson(name, _))

  def removeAllPeople(): Future[ActionPerformed] =
    personController.ask(RemoveAllPeople)

  //#all-routes
  //#people-get-post
  //#people-get-delete

  val personRoutes: Route = {
    concat(
      post {
        entity(as[Person]) { person =>
          onSuccess(createPerson(person)) { performed =>
            complete((StatusCodes.Created, performed))
          }
        }
      },
      path(Segment) { name =>
        concat(
          get {
            //#retrieve-person-info
            rejectEmptyResponse {
              onSuccess(getPerson(name)) { response =>
                complete(response.maybePerson)
              }
            }
            //#retrieve-person-info
          },
          delete {
            //#people-delete-logic
            onSuccess(deletePerson(name)) { performed =>
              complete((StatusCodes.OK, performed))
            }
            //#people-delete-logic
          })
      }
    )
  }

  val peopleRoutes: Route = {
    concat(
      get{
        complete(getPeople)
      },
      post {
        entity(as[GenerationOptions]) { options =>
          onSuccess(generatePeople(options, options, options)) { performed =>
            complete((StatusCodes.Created, performed))
          }
        }
      },
      delete {
        //#people-delete-logic
        onSuccess(removeAllPeople()) { performed =>
          complete((StatusCodes.OK, performed))
        }
      }
    )
  }

  val routes: Route = {
    concat(
      pathPrefix("api") {
        concat(
          pathPrefix("person")(personRoutes),
          path("people")(peopleRoutes)
        )
      }
    )
  }
}
