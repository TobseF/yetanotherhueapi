package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class DeviceTypes {
  @JsonProperty("bridge")
  private val bridge: Boolean = false
  @JsonProperty("lights")
  private val lights: List<Any>? = null
  @JsonProperty("sensors")
  private val sensors: List<Any>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
