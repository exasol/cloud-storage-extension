package com.exasol.cloudetl.storage

import com.exasol.ExaConnectionInformation

import org.scalatest.FunSuite

class StorageConnectionInformationTest extends FunSuite {

  test("apply returns correct user and password") {
    val info = StorageConnectionInformation("user", "p@sword")
    assert(info.getUser() === "user")
    assert(info.getPassword() === "p@sword")
    assert(info.getAddress() === "")
    assert(info.getType() === ExaConnectionInformation.ConnectionType.PASSWORD)
  }

}
