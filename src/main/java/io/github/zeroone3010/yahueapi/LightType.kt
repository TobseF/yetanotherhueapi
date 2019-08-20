package io.github.zeroone3010.yahueapi

/**
 * See https://developers.meethue.com/documentation/supported-lights for further specifications.
 */
enum class LightType {
  ON_OFF, DIMMABLE, COLOR_TEMPERATURE, COLOR, EXTENDED_COLOR,
  UNKNOWN;


  companion object {

    internal fun parseTypeString(type: String?): LightType {
      if (type == null) {
        return UNKNOWN
      }
      when (type.toLowerCase()) {
        "on/off light" -> return ON_OFF
        "dimmable light" -> return DIMMABLE
        "color temperature light" -> return COLOR_TEMPERATURE
        "color light" -> return COLOR
        "extended color light" -> return EXTENDED_COLOR
        else -> return UNKNOWN
      }
    }
  }
}
