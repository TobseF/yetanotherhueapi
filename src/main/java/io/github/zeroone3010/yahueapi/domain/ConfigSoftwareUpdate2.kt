package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ConfigSoftwareUpdate2 {
  @JsonProperty("checkforupdate")
  val isCheckForUpdate: Boolean = false
  @JsonProperty("lastchange")
  val lastChange: String? = null
  @JsonProperty("bridge")
  val bridge: ConfigSoftwareUpdate2Bridge? = null
  @JsonProperty("state")
  val state: String? = null
  @JsonProperty("autoinstall")
  val autoInstall: ConfigSoftwareUpdate2AutoInstall? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
