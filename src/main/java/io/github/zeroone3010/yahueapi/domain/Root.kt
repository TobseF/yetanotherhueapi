package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class Root {

  @JsonProperty("lights")
  var lights: Map<String, LightDto>? = null
  @JsonProperty("groups")
  var groups: Map<String, Group>? = null
  @JsonProperty("config")
  var config: BridgeConfig? = null
  @JsonIgnore
  var schedules: Map<String, Schedule>? = null
  @JsonIgnore
  var scenes: Map<String, Scene>? = null
  @JsonProperty("rules")
  var rules: Map<String, Rule>? = null
  @JsonProperty("sensors")
  var sensors: Map<String, SensorDto>? = null
  @JsonIgnore
  var resourcelinks: Map<String, ResourceLink>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
