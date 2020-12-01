package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import repositories.PersonRepository
import spray.json.enrichAny
import core.JsonProtocols

trait SearchRoutes extends JsonProtocols {

  val searchRoutes: Route = {
    concat(
      pathEnd {
        parameter("id".as[Int]) { id =>
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findById(id).map(_.toJson)) {
                response =>
                  complete(StatusCodes.Found, response)
              }
            }
          }

        } ~ parameter("sex") { sex =>
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findAllFromGender(sex).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
          }
        } ~ parameter("year") { year =>
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findAllFromYear(year).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
          }
        }
      }
        ~ path("range") {
        parameters("start".as[Int], "end".as[Int], "sex".as[String].optional) { (start, end, sex) =>
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findAllBetweenIdRangeAndGender(start, end, sex).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
          }
        }
      } ~ path("between") {
        parameters("from".as[String], "to".as[String], "sex".as[String].optional) { (from, to, sex) =>
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.findAllBetweenYearRangeAndGender(from, to, sex).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
          }
        }
      } ~ path("random") {
        parameters("amount".as[Int], "sex".as[String].optional) { (amount, sex) =>
          get {
            rejectEmptyResponse {
              onSuccess(PersonRepository.getRandomBatch(amount, sex).map(_.toJson)) { response =>
                complete(StatusCodes.Found, response)
              }
            }
          }
        }
      }
    )
  }

}
