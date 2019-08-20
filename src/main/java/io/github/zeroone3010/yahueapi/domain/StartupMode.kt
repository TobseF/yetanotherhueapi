package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonValue

enum class StartupMode(val apiValue: String) {
  BRIGHT_LIGHT("safety"),
  KEEP_STATE("powerfail"),
  LAST_ON_STATE("lastonstate"),
  CUSTOM("custom"),
  UNKNOWN("unknown");

  @JsonValue
  fun jsonValue(): String {
    return apiValue
  }
}
