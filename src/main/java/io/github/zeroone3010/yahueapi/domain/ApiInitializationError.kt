package io.github.zeroone3010.yahueapi.domain

class ApiInitializationError {
  var type: Int = 0
  var address: String? = null
  var description: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
