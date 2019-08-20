package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class RuleCondition {
  @JsonProperty("address")
  val address: String? = null
  @JsonProperty("operator")
  val operator: String? = null
  @JsonProperty("value")
  val value: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
