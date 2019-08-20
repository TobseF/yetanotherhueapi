package io.github.zeroone3010.yahueapi.domain

class LightConfig {
  var archetype: String? = null
  var function: String? = null
  var direction: String? = null
  var startup: Startup? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
