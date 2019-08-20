package io.github.zeroone3010.yahueapi.domain

class ApiInitializationSuccess {
  var username: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
