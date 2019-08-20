package io.github.zeroone3010.yahueapi

import java.time.ZonedDateTime

interface Sensor {
  /**
   * Returns the human readable name of the sensor.
   *
   * @return Name of the sensor.
   */
  val name: String?

  /**
   * Returns the id of the sensor, as assigned by the Bridge.
   *
   * @return The sensor id.
   */
  val id: String

  /**
   * Returns the type of the sensor.
   *
   * @return The type of the sensor as a `SensorType` enumeration value.
   */
  val type: SensorType

  /**
   * Returns the last time the sensor status has been updated.
   *
   * @return The time when the sensor status was last updated.
   */
  val lastUpdated: ZonedDateTime
}
