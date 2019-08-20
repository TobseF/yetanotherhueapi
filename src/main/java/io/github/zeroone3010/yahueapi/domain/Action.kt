package io.github.zeroone3010.yahueapi.domain

class Action {
  var isOn: Boolean = false
  var bri: Int = 0
  var hue: Int = 0
  var sat: Int = 0
  var effect: String? = null
  var xy: List<Float>? = null
  var ct: Int = 0
  var alert: String? = null
  var colormode: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
