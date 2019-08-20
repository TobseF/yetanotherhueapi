package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class InternetServices {
  @JsonProperty("internet")
  val internet: String? = null
  @JsonProperty("remoteaccess")
  val remoteAccess: String? = null
  @JsonProperty("time")
  val time: String? = null
  @JsonProperty("swupdate")
  val swupdate: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
