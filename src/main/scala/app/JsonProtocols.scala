package app

import app.PersonController.ActionPerformed
import models.{GenerationOptions, People, Person}
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

object JsonProtocols  extends DefaultJsonProtocol with SprayJsonSupport{
  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed.apply)
  implicit val personJsonFormat = jsonFormat5(Person.apply)
  implicit val peopleJsonFormat = jsonFormat1(People.apply)
  implicit val generationOptionsFormat = jsonFormat3(GenerationOptions.apply)
}

