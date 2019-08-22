package io.github.zeroone3010.yahueapi.domain

class ApiInitializationSuccess {
  val username: String? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
