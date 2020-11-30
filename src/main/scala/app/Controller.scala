package app

import repositories._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import core.PeopleGenerator
import models.{GenerationOptions,Person}
import spray.json.{JsValue, enrichAny}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Controller {
  sealed trait Command

  final case class GetPersonResponse(maybePerson: Future[Person])
  final case class GetPeopleResponse(maybePeople: Future[Seq[Person]])

  final case class ActionPerformed(description: String)

  final case class GetPeople(replyTo: ActorRef[Future[JsValue]]) extends Command
  import JsonProtocols._

  final case class CreatePerson(person: Person, replyTo: ActorRef[ActionPerformed]) extends Command
  // final case class CreatePeople(people: Set[Person], replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GeneratePeople(generationOptions: GenerationOptions,
                                  replyTo: ActorRef[ActionPerformed]) extends Command


  final case class GetPerson(id: Int, replyTo: ActorRef[GetPersonResponse]) extends Command

  final case class DeletePerson(id: Int, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class RemoveAllPeople(replyTo: ActorRef[ActionPerformed]) extends Command

  def apply(): Behavior[Command] = controller()

  private def controller(): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetPeople(replyTo) =>
        replyTo ! PersonRepository.findAll.map(_.toJson)
        Behaviors.same
      case GetPerson(id, replyTo) =>
        replyTo ! GetPersonResponse(PersonRepository.findById(id))
        Behaviors.same
      case CreatePerson(person, replyTo) =>
        replyTo ! ActionPerformed(s"Person with name ${person.firstname} created.")
        PersonRepository.create(person)//.map(_.toJson)
        Behaviors.same
      case GeneratePeople(generationOptions, replyTo) =>
        replyTo ! ActionPerformed(s"Total number of people created ${generationOptions.totalPeople}," +
          s" with a female percentage of ${generationOptions.femalePercent}% and a male percentage of ${generationOptions.malePercent}%.")
        PersonRepository.createPeople(PeopleGenerator.generatePeople(generationOptions).get).map(_.toJson)
        Behaviors.same
    }
}
