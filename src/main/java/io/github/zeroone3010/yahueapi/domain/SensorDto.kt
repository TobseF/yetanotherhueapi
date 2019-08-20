package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class SensorDto {
  @JsonProperty("name")
  var name: String? = null
  @JsonProperty("type")
  var type: String? = null
  @JsonProperty("state")
  var state: Map<String, Any>? = null
  @JsonProperty("swupdate")
  var softwareUpdate: ComponentSoftwareUpdate? = null
  @JsonProperty("config")
  var config: Map<String, Any>? = null
  @JsonProperty("modelid")
  var modelId: String? = null
  @JsonProperty("manufacturername")
  var manufacturerName: String? = null
  @JsonProperty("swversion")
  var softwareVersion: String? = null
  @JsonProperty("uniqueid")
  var uniqueId: String? = null
  @JsonProperty("recycle")
  var recycle: Boolean? = null
  @JsonProperty("productname")
  var productName: String? = null
  @JsonProperty("capabilities")
  var capabilities: Map<String, Any>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
