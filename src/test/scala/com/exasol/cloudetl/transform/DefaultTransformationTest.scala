package com.exasol.cloudetl.transform

import com.exasol.cloudetl.constants.Constants.TRUNCATE_STRING
import com.exasol.cloudetl.helper.StringGenerator.getRandomString
import com.exasol.cloudetl.storage.StorageProperties

import org.scalatest.funsuite.AnyFunSuite

class DefaultTransformationTest extends AnyFunSuite {

  val longString = getRandomString(2000010)
  val values = Array[Any](1, 3.14, "abc", longString).map(_.asInstanceOf[Object])

  test("truncates strings larger than maximum varchar size") {
    val properties = StorageProperties(Map(TRUNCATE_STRING -> "true"))
    val expected = Array[Any](1, 3.14, "abc", longString.substring(0, 2000000)).map(_.asInstanceOf[Object])
    assert(new DefaultTransformation(properties).transform(Array.from(values)) === expected)
  }

  test("passes if there is no strings larger than maximum varchar size") {
    val properties = StorageProperties(Map(TRUNCATE_STRING -> "true"))
    val input = Array("abc", getRandomString(255), getRandomString(2000000)).map(_.asInstanceOf[Object])
    assert(new DefaultTransformation(properties).transform(input) === input)
  }

  test("throws strings larger than maximum varchar size if no truncate string parameter") {
    val properties = StorageProperties(Map.empty[String, String])
    val thrown = intercept[IllegalStateException] {
      new DefaultTransformation(properties).transform(Array.from(values))
    }
    assert(thrown.getMessage().startsWith("E-CSE-29"))
  }

  test("throws strings larger than maximum varchar size if truncate string parameter is false") {
    val properties = StorageProperties(Map(TRUNCATE_STRING -> "false"))
    val thrown = intercept[IllegalStateException] {
      new DefaultTransformation(properties).transform(Array.from(values))
    }
    assert(thrown.getMessage().startsWith("E-CSE-29"))
  }

}
