package utilities

import java.io.IOException

import utilities.PeopleGenerator.getClass

import scala.io.Source
import scala.util.{Failure, Try}

object FileReader {

  /**
   * Reader for txt files containing names
   *
   * @param filename the file name that is getting read
   * @return Array of names
   */
  def readFile(filename: String): Try[Array[String]] = {
    val inputStream = Option(getClass.getResourceAsStream(filename))
    inputStream.map { stream =>
      Try {
        val lines = Source.fromInputStream(stream).getLines.toArray
        stream.close()
        lines
      }
    }
      .getOrElse {
        Failure(new IOException(s"""Can't load resource "$filename""""))
      }
  }

}
