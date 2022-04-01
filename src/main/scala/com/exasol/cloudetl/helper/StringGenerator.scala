package com.exasol.cloudetl.helper

import scala.util.Random

/**
 * Helper class that generates string values.
 */
object StringGenerator {
  private[this] val RNG = new Random()

  /**
   * Generates a random string with a given length.
   *
   * @param length length of the final string
   * @return generated string
   */
  def getRandomString(length: Int): String = {
    val builder = new StringBuilder()
    var i = 0
    while (i < length) {
      builder.append(RNG.nextPrintableChar())
      i += 1
    }
    builder.toString()
  }

}
