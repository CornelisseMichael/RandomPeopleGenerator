package app

//#person-controller-actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import models.{GenerationOptions, People, Person}
import utility.PeopleGenerator

object PersonController {

  // actor protocol
  sealed trait Command

  final case class GetPeople(replyTo: ActorRef[People]) extends Command

  final case class CreatePerson(person: Person, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class CreatePeople(people: Set[Person], replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GeneratePeople(totalPeople: GenerationOptions, malePercent: GenerationOptions,
                                  femalePercent: GenerationOptions, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetPerson(name: String, replyTo: ActorRef[GetPersonResponse]) extends Command

  final case class DeletePerson(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetPersonResponse(maybePerson: Option[Person])

  final case class ActionPerformed(description: String)


  def apply(): Behavior[Command] = controller(Set.empty)

  private def controller(people: Set[Person]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetPeople(replyTo) =>
        replyTo ! People(people.toSeq)
        Behaviors.same
      case CreatePerson(person, replyTo) =>
        replyTo ! ActionPerformed(s"People ${person.firstname} created.")
        //registry(people.appended(person))
        controller(people + person)
      case GeneratePeople(totalPeople, femalePercent, malePercent, replyTo) =>
        replyTo ! ActionPerformed(s"Total number of people created ${totalPeople.totalPeople}," +
          s" with a female percentage of ${femalePercent.femalePercent}% and a male percentage of ${malePercent.malePercent}%.")
        controller(people ++ PeopleGenerator.generatePeople(totalPeople.totalPeople, femalePercent.femalePercent, malePercent.malePercent).get.toSet)
      case GetPerson(name, replyTo) =>
        replyTo ! GetPersonResponse(people.find(_.firstname == name))
        Behaviors.same
      case DeletePerson(name, replyTo) =>
        replyTo ! ActionPerformed(s"Person $name deleted.")
        controller(people.filterNot(_.firstname == name))
    }
}
