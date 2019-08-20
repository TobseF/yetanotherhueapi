package io.github.zeroone3010.yahueapi

interface DimmerSwitch : Sensor {
  /**
   * The latest button event of this switch.
   *
   * @return `true` if presence detected, `false` if not.
   */
  val latestButtonEvent: DimmerSwitchButtonEvent
}
