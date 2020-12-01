package models

/**
 * Model for setting generation options
 *
 * @param totalPeople   total amount of people to generate
 * @param femalePercent female percentage to generate
 * @param malePercent   male percentage to generate
 * @param from          Starting birth year to start generating from
 * @param to            ending birth year to start generating to
 */
final case class GenerationOptions(totalPeople: Int,
                                   femalePercent: Int,
                                   malePercent: Int,
                                   from: Int,
                                   to: Int)