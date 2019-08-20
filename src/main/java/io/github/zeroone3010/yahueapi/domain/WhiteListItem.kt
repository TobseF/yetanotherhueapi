package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class WhiteListItem {
  @JsonProperty("last use date")
  var lastUseDate: String? = null
  @JsonProperty("create date")
  var createDate: String? = null
  @JsonProperty("name")
  var name: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
