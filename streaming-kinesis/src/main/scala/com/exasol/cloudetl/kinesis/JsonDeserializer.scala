package com.exasol.cloudetl.kinesis

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.ScalaObjectMapper

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
object JsonDeserializer {
  private[this] val mapper = new ObjectMapper with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)

  def parseJson[T: Manifest](jsonString: String): T =
    mapper.readValue[T](jsonString)
}
