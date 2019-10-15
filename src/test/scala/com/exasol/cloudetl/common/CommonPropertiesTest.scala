package com.exasol.cloudetl.common

import org.scalatest.FunSuite

class CommonPropertiesTest extends FunSuite {

  test("mapFromString returns key-value map") {
    val testData = Map(
      ";" -> Map.empty[String, String],
      "a -> 1;b -> 2;c -> 3" -> Map("a" -> "1", "b" -> "2", "c" -> "3")
    )
    testData.foreach {
      case (given, expected) =>
        assert(CommonProperties.mapFromString(given) === expected)
    }
  }

  test("mapFromString throws if input is not properly separated") {
    val thrown = intercept[IllegalArgumentException] {
      CommonProperties.mapFromString("")
    }
    assert(thrown.getMessage === s"The input string is not separated by ';'!")
  }

  private[this] object CommonProperties extends CommonProperties

}
