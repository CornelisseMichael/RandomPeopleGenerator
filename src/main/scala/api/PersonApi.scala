package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import repositories.PersonRepository
import models.{GenerationOptions, Person}
import spray.json.enrichAny
import core._

class PersonApi {

  import app.JsonProtocols._

  val peopleRoutes: Route = {
    concat(
      get {
        complete(PersonRepository.findAll.map(_.toJson))
      },
      post {
        entity(as[GenerationOptions]) { options =>
          onSuccess(PersonRepository.createPeople(PeopleGenerator.generatePeople(options).get).map(_.toJson)) { performed =>
            complete((StatusCodes.Created, performed))
          }
        }
      },
    )
  }


  val personRoutes: Route = {
    (parameter("id") { id =>
      concat(
        get {
          rejectEmptyResponse {
            onSuccess(PersonRepository.findById(id.toInt).map(_.toJson)) { response =>
              complete(StatusCodes.Found, response)
            }
          }
        },
        delete {
          //#people-delete-logic
          onSuccess(PersonRepository.delete(id.toInt).map(_.toJson)) { performed =>
            complete((StatusCodes.OK, performed))
          }
        }
        //#people-delete-logic
      )
    }).~(parameter("sex") { sex =>
      get {
        rejectEmptyResponse {
          onSuccess(PersonRepository.findAllFromGender(sex.charAt(0)).map(_.toJson)) { response =>
            complete(StatusCodes.Found, response)
          }
        }
      }
    }
  )

  /*
  concat(
    (parameter("id") & get) { id =>
      rejectEmptyResponse {
        onSuccess(PersonRepository.findById(id.toInt).map(_.toJson)) { response =>
          complete(StatusCodes.Found, response)
        }
      }
    },
  )

   */
}

val routes: Route = {
  concat (
  pathPrefix ("api") {
  concat (
  pathPrefix ("people") (peopleRoutes),
  pathPrefix ("person") (personRoutes)
  )
},

  )
}

  /*
    val personRoutes: Route = {
    concat(
      pathPrefix("person") {
        post {
          entity(as[Person]) { person =>
            onSuccess(PersonRepository.create(person).map(_.toJson)) { performed =>
              complete((StatusCodes.Created, performed))
            }
          }
        }
      },
      path(Segment) {
        parameter("id") { (id) =>
          concat(
            get {
              onSuccess(PersonRepository.findById(id.toInt).map(_.toJson)) { performed =>
                complete(StatusCodes.Found, performed)
              }
            },
            delete {
              //#people-delete-logic
                 onSuccess(PersonRepository.delete(id.toInt).map(_.toJson)) { performed =>
                  complete((StatusCodes.OK, performed))
                }
            }
          )
        }
      }
    )
  }

    val personRoutes: Route = {
      concat(
        post {
          entity(as[Person]) { person =>
            onSuccess(PersonRepository.create(person).map(_.toJson)) { performed =>
              complete((StatusCodes.Created, performed))
            }
          }
        },
        path(Segment) { id =>
          concat(
            get {
              //#retrieve-person-info
              rejectEmptyResponse {
                onSuccess(PersonRepository.findById(id.toInt).map(_.toJson)) { response =>
                  complete(StatusCodes.Found, response)
                }
              }
              //#retrieve-person-info
            },
           // delete {
              //#people-delete-logic
           //   onSuccess(PersonRepository.delete(id.toInt).map(_.toJson)) { performed =>
            //    complete((StatusCodes.OK, performed))
            //  }
          //  }
            //#people-delete-logic

          )
        }
      )
    }

    val peopleRoutes: Route = {
      concat(
        get {
          complete(PersonRepository.findAll.map(_.toJson))
        },
        post {
          entity(as[GenerationOptions]) { options =>
            onSuccess(PersonRepository.createPeople(PeopleGenerator.generatePeople(options).get).map(_.toJson)) { performed =>
              complete((StatusCodes.Created, performed))
            }
          }
        },
        delete {
          //#people-delete-logic
          onSuccess(PersonRepository.deleteAll().map(_.toJson)) { performed =>
            complete(("All people deleted", performed))
          }
        },

      )
    }

    val year: Route = {
      path(Segment) { birthyear =>
        concat(
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findAllFromYear(birthyear).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
            //#retrieve-person-info
          },
        )
      }
    }
    val sex: Route = {
      path(Segment) { sex =>
        concat(
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findAllFromGender(sex.charAt(0)).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
            //#retrieve-person-info
          },
        )
      }
    }

   // val between: Route = {

   // }

    val routes: Route = {
      concat(
        pathPrefix("api") {
          concat(
            pathPrefix("person")(personRoutes),
            pathPrefix("people")(peopleRoutes),
            pathPrefix("year")(year),
          //  pathPrefix("between")(between),
            pathPrefix("sex")(sex),
          )
        }
      )
    }

   */

}
