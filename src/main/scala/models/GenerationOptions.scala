package models

//final case class TotalPeople(totalPeople: Int)

//final case class GenerationOptions(totalPeople: Int, femalePercent: Int, malePercent: Int)
final case class GenerationOptions(totalPeople: Int,
                                   femalePercent: Int,
                                   malePercent: Int,
                                   from: Int,
                                   to: Int)