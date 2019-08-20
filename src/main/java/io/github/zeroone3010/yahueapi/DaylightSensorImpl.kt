package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.domain.SensorDto
import java.net.URL

internal class DaylightSensorImpl(id: String, sensor: SensorDto, url: URL, stateProvider: () -> Map<String, Any>?) : BasicSensor(id, sensor, url, stateProvider), DaylightSensor {

  override val isDaylightTime: Boolean
    get() = readStateValue("daylight", Boolean::class.java)

  override fun toString(): String {
    return "DaylightSensor{" +
        "id='" + super.id + '\''.toString() +
        ", name='" + super.name + '\''.toString() +
        ", type=" + super.type +
        '}'.toString()
  }
}
