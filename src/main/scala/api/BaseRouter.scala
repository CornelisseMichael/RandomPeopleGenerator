package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class BaseRouter extends PersonRoutes with PeopleRoutes with SearchRoutes {

  val routes: Route = {
    concat(
      pathPrefix("api") {
        pathPrefix("person")(personRoutes)~
          pathPrefix("people")(peopleRoutes)~
          pathPrefix("search")(searchRoutes)
      }
    )
  }
}
