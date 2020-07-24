package com.exasol.cloudetl.util

import scala.collection.immutable.HashMap
import scala.collection.mutable

import org.scalatest.funsuite.AnyFunSuite

class JsonMapperTest extends AnyFunSuite {
  test("parseJson parses a String") {
    val jsonString = "{\"sensorId\": 17,\"currentTemperature\": 147,\"status\": \"WARN\"}"
    val values = JsonMapper.parseJson[HashMap[String, Object]](jsonString)
    assert(values === HashMap(("sensorId", 17), ("currentTemperature", 147), ("status", "WARN")))
  }

  test("toJson returns String") {
    val map: Any =
      mutable.LinkedHashMap(("sensorId", 17), ("currentTemperature", 147), ("status", "WARN"))
    val values = JsonMapper.toJson[Any](map)
    assert(values === "{\"sensorId\":17,\"currentTemperature\":147,\"status\":\"WARN\"}")
  }
}
