package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.domain.SensorDto
import java.net.URL

internal class MotionSensorImpl(id: String, sensor: SensorDto, url: URL, stateProvider: () -> Map<String, Any>?) : BasicSensor(id, sensor, url, stateProvider), MotionSensor {

  override val isPresence: Boolean
    get() = readStateValue("presence", Boolean::class.java)

  override fun toString(): String {
    return "MotionSensor{" +
        "id='" + super.id + '\''.toString() +
        ", name='" + super.name + '\''.toString() +
        ", type=" + super.type +
        '}'.toString()
  }
}
