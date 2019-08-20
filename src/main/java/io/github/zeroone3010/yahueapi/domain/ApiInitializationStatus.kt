package io.github.zeroone3010.yahueapi.domain

class ApiInitializationStatus {
  var error: ApiInitializationError? = null
  var success: ApiInitializationSuccess? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
