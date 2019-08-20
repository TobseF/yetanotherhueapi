package io.github.zeroone3010.yahueapi.domain

class Startup {
  var mode: StartupMode? = null
  var isConfigured: Boolean = false

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
