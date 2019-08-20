package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data transfer object to hold the properties received from the Hue Bridge. Do not try to use this class
 * to change the state of a light, use [io.github.zeroone3010.yahueapi.State] instead.
 */
class LightState {
  /**
   * @param on
   */
  @JsonProperty("on")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var isOn: Boolean = false
  /**
   * @param brightness
   */
  @JsonProperty("bri")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var brightness: Int = 0
  /**
   * @param hue
   */
  @JsonProperty("hue")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var hue: Int = 0
  /**
   * @param saturation
   */
  @JsonProperty("sat")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var saturation: Int = 0
  /**
   * @param effect
   */
  @JsonProperty("effect")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var effect: String? = null
  /**
   * @param xy
   */
  @JsonProperty("xy")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var xy: List<Float>? = null
  /**
   * @param ct
   */
  @JsonProperty("ct")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var ct: Int = 0
  /**
   * @param alert
   */
  @JsonProperty("alert")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var alert: String? = null
  /**
   * @param colorMode
   */
  @JsonProperty("colormode")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var colorMode: String? = null
  /**
   * @param mode
   */
  @JsonProperty("mode")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var mode: String? = null
  /**
   * @param reachable
   */
  @JsonProperty("reachable")
  @set:Deprecated("The state cannot be changed with this class. Use {@link io.github.zeroone3010.yahueapi.State} instead.")
  var isReachable: Boolean = false

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
