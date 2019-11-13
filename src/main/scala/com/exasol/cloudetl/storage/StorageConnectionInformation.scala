package com.exasol.cloudetl.storage

import com.exasol.ExaConnectionInformation
import com.exasol.ExaConnectionInformation.ConnectionType

final case class StorageConnectionInformation(user: String, password: String)
    extends ExaConnectionInformation {
  override def getAddress(): String = ""
  override def getPassword(): String = password
  override def getUser(): String = user
  override def getType(): ConnectionType = ConnectionType.PASSWORD
}
