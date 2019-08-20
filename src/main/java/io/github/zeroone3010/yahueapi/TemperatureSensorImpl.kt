package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.domain.SensorDto
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.util.logging.Logger

internal class TemperatureSensorImpl(id: String, sensor: SensorDto, url: URL, stateProvider: () -> Map<String, Any>?) : BasicSensor(id, sensor, url, stateProvider), TemperatureSensor {

  override val degreesCelsius: BigDecimal
    get() = convertCenticelsiusToCelsius(readStateValue("temperature", Int::class.java))

  override fun toString(): String {
    return "TemperatureSensor{" +
        "id='" + super.id + '\''.toString() +
        ", name='" + super.name + '\''.toString() +
        ", type=" + super.type +
        '}'.toString()
  }

  companion object {
    private val logger = Logger.getLogger("MotionSensorImpl")

    private fun convertCenticelsiusToCelsius(centicelsius: Int): BigDecimal {
      return BigDecimal.valueOf(centicelsius.toLong()).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP)
    }
  }
}
