package io.cloudslang.content.google.utils.action

import io.cloudslang.content.constants.BooleanValues.TRUE
import io.cloudslang.content.google.utils.Constants.COMMA

/**
  * Created by victor on 28.02.2017.
  */
object DefaultValues {
  final val DEFAULT_CONSOLE_PORT = "1"
  final val DEFAULT_START_INDEX = "0"
  final val DEFAULT_PROXY_PORT = "8080"
  final val DEFAULT_PRETTY_PRINT = TRUE
  final val DEFAULT_AUTO_CREATE_SUBNETWORKS = TRUE
  final val DEFAULT_LICENSES_DELIMITER = COMMA
  final val DEFAULT_SCOPES_DELIMITER = COMMA
  final val DEFAULT_ITEMS_DELIMITER = COMMA
  final val DEFAULT_TAGS_DELIMITER = COMMA
  final val DEFAULT_TIMEOUT = "600"
  final val DEFAULT_DISK_SIZE = "10"
  final val DEFAULT_SYNC_TIMEOUT = "30"
  final val DEFAULT_VOLUME_MOUNT_TYPE = "PERSISTENT"
  final val DEFAULT_VOLUME_MOUNT_MODE = "READ_WRITE"
  final val DEFAULT_ACCESS_CONFIG_TYPE = "ONE_TO_ONE_NAT"
  final val DEFAULT_INTERFACE = "SCSI"
}
