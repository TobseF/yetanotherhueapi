package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class EntertainmentStream {
  @JsonProperty("proxymode")
  val proxyMode: String? = null
  @JsonProperty("proxynode")
  val proxyNode: String? = null
  @JsonProperty("active")
  val isActive: Boolean = false
  @JsonProperty("owner")
  val owner: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
