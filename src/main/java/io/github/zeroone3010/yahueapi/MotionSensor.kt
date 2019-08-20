package io.github.zeroone3010.yahueapi

interface MotionSensor : Sensor {
  /**
   * Whether presence has been detected.
   *
   * @return `true` if presence detected, `false` if not.
   */
  val isPresence: Boolean
}
