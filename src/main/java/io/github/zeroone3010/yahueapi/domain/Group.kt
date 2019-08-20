package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Group {

  @JsonProperty("name")
  var name: String? = null
  @JsonProperty("lights")
  var lights: List<String>? = null
  @JsonProperty("type")
  var type: String? = null
  @JsonProperty("state")
  var state: GroupState? = null
  @JsonProperty("recycle")
  var isRecycle: Boolean = false
  @JsonProperty("class")
  var roomClass: String? = null
  @JsonProperty("action")
  var action: Action? = null
  @JsonProperty("stream")
  var stream: EntertainmentStream? = null
  @JsonProperty("locations")
  var locations: Map<String, List<Int>>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
