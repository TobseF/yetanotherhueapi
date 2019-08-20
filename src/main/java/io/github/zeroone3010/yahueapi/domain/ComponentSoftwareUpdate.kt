package io.github.zeroone3010.yahueapi.domain

class ComponentSoftwareUpdate {
  var state: String? = null
  var lastinstall: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
