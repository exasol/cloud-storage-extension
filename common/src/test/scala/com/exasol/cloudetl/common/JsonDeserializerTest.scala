package com.exasol.cloudetl.util

import scala.collection.immutable.HashMap

import org.scalatest.funsuite.AnyFunSuite

class JsonDeserializerTest extends AnyFunSuite {
  test("parseJson parses a String") {
    val jsonString = "{\"sensorId\": 17,\"currentTemperature\": 147,\"status\": \"WARN\"}"
    val values = JsonDeserializer.parseJson[HashMap[String, Object]](jsonString)
    assert(values === HashMap(("sensorId", 17), ("currentTemperature", 147), ("status", "WARN")))
  }
}
