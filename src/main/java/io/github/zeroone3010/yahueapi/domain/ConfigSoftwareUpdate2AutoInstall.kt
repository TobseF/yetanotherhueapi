package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ConfigSoftwareUpdate2AutoInstall {
  @JsonProperty("updatetime")
  val updateTime: String? = null
  @JsonProperty("on")
  val isOn: Boolean = false

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
