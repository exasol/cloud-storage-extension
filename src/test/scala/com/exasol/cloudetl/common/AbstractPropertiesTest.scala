package com.exasol.cloudetl.common

import com.exasol.{ExaConnectionInformation, ExaMetadata}

import org.mockito.Mockito.{times, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class AbstractPropertiesTest extends AnyFunSuite with BeforeAndAfterEach with MockitoSugar {

  private[this] var properties: Map[String, String] = _

  override final def beforeEach(): Unit = {
    properties = Map.empty[String, String]
    ()
  }

  test("isEmpty returns true initially") {
    assert(BaseProperties(properties).isEmpty() === true)
  }

  test("containsKey returns true if key exists") {
    properties = Map("key" -> "value")
    assert(BaseProperties(properties).containsKey("key") === true)
  }

  test("containsKey returns false if key does not exist") {
    assert(BaseProperties(properties).containsKey("key") === false)
  }

  test("isEnabled returns true if key is set to true") {
    properties = Map("isEnabledKey" -> "TruE")
    assert(BaseProperties(properties).isEnabled("isEnabledKey") === true)
  }

  test("isEnabled returns false if key does not exist") {
    assert(BaseProperties(properties).isEnabled("isEnabledKey") === false)
  }

  test("isEnabled returns false if key is set to false") {
    properties = Map("isEnabledKey" -> "false")
    assert(BaseProperties(properties).isEnabled("isEnabledKey") === false)
  }

  test("isEnabled returns false if key is set to null") {
    properties = Map("isEnabledKey" -> null)
    assert(BaseProperties(properties).isEnabled("isEnabledKey") === false)
  }

  test("isNull returns true if key does not exist") {
    assert(BaseProperties(properties).isNull("isNullKey") === true)
  }

  test("isNull returns true if key value is set to null") {
    properties = Map("isNullKey" -> null)
    assert(BaseProperties(properties).isNull("isNullKey") === true)
  }

  test("isNull returns false if key value is not set to null") {
    properties = Map("isNullKey" -> "no")
    assert(BaseProperties(properties).isNull("isNullKey") === false)
  }

  test("get returns None if key does not exist") {
    assert(BaseProperties(properties).get("key") === None)
  }

  test("get returns Option(value) if key exists") {
    properties = Map("key" -> "value")
    assert(BaseProperties(properties).get("key") === Option("value"))
  }

  test("getString returns value as String") {
    properties = Map("key" -> "value")
    assert(BaseProperties(properties).getString("key") === "value")
  }

  test("getString throws if key does not exits") {
    val key = "key"
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getString(key)
    }
    assert(
      thrown.getMessage === s"Please provide a value for the $key property!"
    )
  }

  test("size returns zero by default") {
    assert(BaseProperties(properties).size() === 0)
  }

  test("size returns the number of entries in properties") {
    properties = Map("key1" -> "value1", "key2" -> "value2")
    assert(BaseProperties(properties).size() === 2)
  }

  test("mkString returns empty string by default") {
    val str = BaseProperties(properties).mkString("->", "=")
    assert(str.isEmpty === true)
    assert(str === "")
  }

  test("mkString returns separated key-value property pairs") {
    properties = Map("k1" -> "v1", "k2" -> "v2", "a" -> "1")
    val expected = s"a -> 1;k1 -> v1;k2 -> v2" // sorted
    val str = BaseProperties(properties).mkString(" -> ", ";")
    assert(str.isEmpty === false)
    assert(str === expected)
  }

  test("equals returns true if this and other are equal") {
    properties = Map("key1" -> "value1", "key2" -> "value2")
    val basePropertiesThis = BaseProperties(properties)
    val basePropertiesOther = BaseProperties(properties)
    assert(basePropertiesThis === basePropertiesOther)
  }

  test("equals returns false if this and other properties are not equal") {
    properties = Map("key1" -> "value1")
    val basePropertiesThis = BaseProperties(properties)
    val basePropertiesOther = BaseProperties(Map("key2" -> "value2"))
    assert(basePropertiesThis !== basePropertiesOther)
  }

  test("equals returns false if this and other size are not equal") {
    properties = Map("key1" -> "value1", "key2" -> "value2")
    val basePropertiesThis = BaseProperties(properties)
    val basePropertiesOther = BaseProperties(Map.empty[String, String])
    assert(basePropertiesThis !== basePropertiesOther)
  }

  test("equals returns false if other is not same instance type") {
    properties = Map("key1" -> "value1", "key2" -> "value2")
    val basePropertiesThis = BaseProperties(properties)
    val basePropertiesOther = List("a")
    assert(basePropertiesThis !== basePropertiesOther)
  }

  test("equals returns false if other is null") {
    val basePropertiesThis = BaseProperties(properties)
    assert(basePropertiesThis !== null)
  }

  test("hashCode returns non-zero by default") {
    val baseProperties = BaseProperties(properties)
    assert(baseProperties.hashCode !== 0)
  }

  test("hashCode returns the hascode of properties") {
    properties = Map("a" -> "1", "b" -> "2", "c" -> "3")
    val baseProperties = BaseProperties(properties)
    assert(baseProperties.hashCode === properties.hashCode)
  }

  test("hasNamedConnection returns false by default") {
    assert(BaseProperties(properties).hasNamedConnection() === false)
  }

  test("hasNamedConnection returns true if connection name is set") {
    properties = Map("CONNECTION_NAME" -> "named_connection")
    assert(BaseProperties(properties).hasNamedConnection() === true)
  }

  test("getConnectionInformation throws if Exasol metadata is not provided") {
    val thrown = intercept[IllegalArgumentException] {
      BaseProperties(properties).getConnectionInformation(None)
    }
    assert(thrown.getMessage === "Exasol metadata is None!")
  }

  test("getConnectionInformation returns storage connection information") {
    properties = Map("CONNECTION_NAME" -> "connection_info")
    val metadata = mock[ExaMetadata]
    val connectionInfo = newConnectionInformation("user", "secret")
    when(metadata.getConnection("connection_info")).thenReturn(connectionInfo)
    assert(
      BaseProperties(properties)
        .getConnectionInformation(Option(metadata)) === connectionInfo
    )
    verify(metadata, times(1)).getConnection("connection_info")
  }

  final def newConnectionInformation(
    username: String,
    password: String
  ): ExaConnectionInformation =
    new ExaConnectionInformation() {
      override def getType(): ExaConnectionInformation.ConnectionType =
        ExaConnectionInformation.ConnectionType.PASSWORD
      override def getAddress(): String = ""
      override def getUser(): String = username
      override def getPassword(): String = password
    }

  private[this] case class BaseProperties(val params: Map[String, String])
      extends AbstractProperties(params)
}
