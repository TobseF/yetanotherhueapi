package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

internal object JsonStringUtil {
  private val objectMapper = ObjectMapper()

  fun toJsonString(`object`: Any): String {
    try {
      return objectMapper.writeValueAsString(`object`)
    } catch (e: JsonProcessingException) {
      throw RuntimeException(e)
    }

  }
}/* prevent */
