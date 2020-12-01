package core

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.{GenerationOptions, Person}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonProtocols extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val personJsonFormat: RootJsonFormat[Person] = jsonFormat6(Person)
  implicit val generationOptionsFormat: RootJsonFormat[GenerationOptions] = jsonFormat5(GenerationOptions.apply)
}
