package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Rule {
  @JsonProperty("name")
  val name: String? = null
  @JsonProperty("owner")
  val owner: String? = null
  @JsonProperty("created")
  val created: String? = null
  @JsonProperty("lasttriggered")
  val lastTriggered: String? = null
  @JsonProperty("timestriggered")
  val timesTriggered: Int = 0
  @JsonProperty("status")
  val status: String? = null
  @JsonProperty("recycle")
  val isRecycle: Boolean = false
  @JsonProperty("conditions")
  val conditions: List<RuleCondition>? = null
  @JsonProperty("actions")
  val actions: List<RuleAction>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
