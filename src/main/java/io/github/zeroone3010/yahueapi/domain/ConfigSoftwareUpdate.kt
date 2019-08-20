package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ConfigSoftwareUpdate {
  @JsonProperty("updatestate")
  val updateState: Int = 0
  @JsonProperty("checkforupdate")
  val isCheckForUpdate: Boolean = false
  @JsonProperty("devicetypes")
  val deviceTypes: DeviceTypes? = null
  @JsonProperty("url")
  val url: String? = null
  @JsonProperty("text")
  val text: String? = null
  @JsonProperty("notify")
  val isNotify: Boolean = false

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
