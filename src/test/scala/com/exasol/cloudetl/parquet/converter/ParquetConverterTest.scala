package com.exasol.cloudetl.parquet.converter

import org.apache.parquet.schema.MessageTypeParser
import org.scalatest.funsuite.AnyFunSuite

class ParquetConverterTest extends AnyFunSuite {

  test("array converter throws for illegal index") {
    val schema = MessageTypeParser.parseMessageType(
      """|message parquet_group_list {
         |  optional group names (LIST) {
         |    repeated group list {
         |      required binary name (UTF8);
         |    }
         |  }
         |}
         |""".stripMargin
    )
    Seq(-1, 1, 2, 5).foreach {
      case index =>
        val thrown = intercept[IllegalArgumentException] {
          ArrayGroupConverter(schema.getType(0), -1, EmptyValueHolder).getConverter(index)
        }
        assert(thrown.getMessage().contains(s"Illegal index '$index' to array converter"))
    }
  }

  test("repeated primitive converter throws for illegal index") {
    val schema = MessageTypeParser.parseMessageType(
      """|message parquet_repeated_primitive {
         |  repeated group addresses {
         |    required binary address (UTF8);
         |  }
         |}
         |""".stripMargin
    )
    Seq(-1, 1, 3, 8).foreach {
      case index =>
        val thrown = intercept[IllegalArgumentException] {
          RepeatedPrimitiveConverter(schema.getType(0), -1, EmptyValueHolder).getConverter(index)
        }
        assert(
          thrown.getMessage().contains(s"Illegal index '$index' to repeated primitive converter")
        )
    }
  }

  test("map converter throws for illegal index") {
    val schema = MessageTypeParser.parseMessageType(
      """|message parquet_group_map {
         |  optional group map (MAP) {
         |    repeated group key_value {
         |      required binary key (UTF8);
         |      required binary value (UTF8);
         |    }
         |  }
         |}
         |""".stripMargin
    )
    Seq(-1, 2, 4).foreach {
      case index =>
        val thrown = intercept[IllegalArgumentException] {
          MapConverter(schema.getType(0).asGroupType(), -1, EmptyValueHolder).getConverter(index)
        }
        assert(thrown.getMessage().contains(s"Illegal index '$index' to map converter"))
    }
  }
}
