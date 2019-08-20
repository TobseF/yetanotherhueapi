package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class PortalState {
  @JsonProperty("signedon")
  var isSignedOn: Boolean = false
  @JsonProperty("incoming")
  var isIncoming: Boolean = false
  @JsonProperty("outgoing")
  var isOutgoing: Boolean = false
  @JsonProperty("communication")
  var communication: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
