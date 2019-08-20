package io.github.zeroone3010.yahueapi

/**
 * See https://developers.meethue.com/documentation/supported-sensors for further specifications.
 */
enum class SensorType {
  /**
   * A temperature sensor. Either ZLLTemperature or CLIPTemperature.
   */
  TEMPERATURE,

  /**
   * A motion sensor, i.e. either a ZLLPresence or a CLIPPresence sensor.
   */
  MOTION,

  /**
   * A dimmer switch, i.e. a ZLLSwitch sensor.
   */
  DIMMER_SWITCH,

  /**
   * A daylight sensor, i.e. the one in the Bridge.
   */
  DAYLIGHT,

  /**
   * Other kind of a sensor, not recognized by this library.
   */
  UNKNOWN;


  companion object {


    fun parseTypeString(type: String?): SensorType {
      if (type == null) {
        return UNKNOWN
      }
      when (type.toLowerCase()) {
        "zlltemperature" -> return TEMPERATURE
        "cliptemperature" -> return TEMPERATURE
        "zllpresence" -> return MOTION
        "clippresence" -> return MOTION
        "zllswitch" -> return DIMMER_SWITCH
        "daylight" -> return DAYLIGHT
        else -> return UNKNOWN
      }
    }
  }
}
