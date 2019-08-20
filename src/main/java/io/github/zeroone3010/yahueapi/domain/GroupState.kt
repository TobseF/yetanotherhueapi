package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class GroupState {
  @JsonProperty("all_on")
  var isAllOn: Boolean = false
  @JsonProperty("any_on")
  var isAnyOn: Boolean = false

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
