package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ConfigSoftwareUpdate2Bridge {
  @JsonProperty("state")
  val state: String? = null
  @JsonProperty("lastinstall")
  val lastInstall: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
