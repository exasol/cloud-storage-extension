package com.exasol.cloudetl.orc

import org.apache.orc.TypeDescription
import org.scalatest.FunSuite

class OrcDeserializerTest extends FunSuite {

  test("apply throws if orc type is a list") {
    val orcList = TypeDescription.createList(TypeDescription.createString)
    val thrown = intercept[IllegalArgumentException] {
      OrcDeserializer(orcList)
    }
    assert(thrown.getMessage === "Orc list type is not supported.")
  }

  test("apply throws if orc type is a map") {
    val orcMap =
      TypeDescription.createMap(TypeDescription.createString, TypeDescription.createString)
    val thrown = intercept[IllegalArgumentException] {
      OrcDeserializer(orcMap)
    }
    assert(thrown.getMessage === "Orc map type is not supported.")
  }

  test("apply throws if orc type is a nested struct") {
    val orcStruct =
      TypeDescription.createStruct().addField("col_int", TypeDescription.createInt())
    val thrown = intercept[IllegalArgumentException] {
      OrcDeserializer(orcStruct)
    }
    assert(thrown.getMessage === "Orc nested struct type is not supported.")
  }

  test("apply throws if orc type is unsupported") {
    val orcUnion = TypeDescription.createUnion()
    val thrown = intercept[IllegalArgumentException] {
      OrcDeserializer(orcUnion)
    }
    assert(thrown.getMessage === "Found orc unsupported type, 'UNION'.")
  }

}
