package io.github.zeroone3010.yahueapi

import java.math.BigDecimal

interface TemperatureSensor : Sensor {
  /**
   * Returns the detected temperature in degrees Celcius.
   *
   * @return A `BigDecimal` with two decimal places, indicating the current temperature.
   */
  val degreesCelsius: BigDecimal
}
