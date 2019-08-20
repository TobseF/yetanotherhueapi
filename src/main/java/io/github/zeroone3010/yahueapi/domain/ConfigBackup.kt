package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class ConfigBackup {
  @JsonProperty("status")
  val status: String? = null
  @JsonProperty("errorcode")
  val errorCode: Int = 0

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
