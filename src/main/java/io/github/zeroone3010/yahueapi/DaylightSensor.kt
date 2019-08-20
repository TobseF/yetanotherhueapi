package io.github.zeroone3010.yahueapi

interface DaylightSensor : Sensor {
  /**
   * Tells whether the current time is after sunrise but before sunset.
   *
   * @return `true` if it's daylight time, `false` if not.
   */
  val isDaylightTime: Boolean
}
