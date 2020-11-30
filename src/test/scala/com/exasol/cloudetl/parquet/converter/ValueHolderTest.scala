package com.exasol.cloudetl.parquet.converter

import org.scalatest.funsuite.AnyFunSuite

class ValueHolderTest extends AnyFunSuite {

  test("empty value holder does nothing") {
    assert(EmptyValueHolder.reset() === {})
    assert(EmptyValueHolder.getValues() === Seq.empty[Any])
    assert(EmptyValueHolder.put(-1, "string") === {})
  }
}
