package com.exasol.cloudetl.common

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

@SuppressWarnings(Array("org.wartremover.warts.Var"))
class AbstractPropertiesTest extends FunSuite with BeforeAndAfterEach {

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

  test("size returns zero by default") {
    assert(BaseProperties(properties).size() === 0)
  }

  test("size returns the number of entries in properties") {
    properties = Map("key1" -> "value1", "key2" -> "value2")
    assert(BaseProperties(properties).size() === 2)
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

  private[this] case class BaseProperties(val params: Map[String, String])
      extends AbstractProperties(params)

}
