package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class RuleAction {
  @JsonProperty("address")
  val address: String? = null
  @JsonProperty("method")
  val method: String? = null
  @JsonProperty("body")
  val body: Map<String, Any>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
