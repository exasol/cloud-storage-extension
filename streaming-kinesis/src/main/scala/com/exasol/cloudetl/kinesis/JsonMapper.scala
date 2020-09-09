package com.exasol.cloudetl.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
object JsonMapper {
  private[this] val mapper = new ObjectMapper with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def toJson[T](value: T): String = mapper.writeValueAsString(value)

  def parseJson[T: Manifest](jsonString: String): T =
    mapper.readValue[T](jsonString)
}
