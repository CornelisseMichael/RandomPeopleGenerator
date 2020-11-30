package app

import models.{GenerationOptions, Person}
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

object JsonProtocols extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val personJsonFormat = jsonFormat6(Person)
  implicit val generationOptionsFormat = jsonFormat5(GenerationOptions.apply)
}