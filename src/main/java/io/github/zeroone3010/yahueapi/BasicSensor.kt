package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.domain.SensorDto
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

internal open class BasicSensor(override val id: String, sensor: SensorDto?, protected val baseUrl: URL, private val stateProvider: () -> Map<String, Any>?) : Sensor {
  override val name: String?
  override val type: SensorType

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply { timeZone = TimeZone.getTimeZone("UTC") }

  override val lastUpdated: Date
    get() = dateFormat.parse(readStateValue("lastupdated", String::class.java))

  init {
    if (sensor == null) {
      throw HueApiException("Sensor $id cannot be found.")
    }
    this.name = sensor.name
    this.type = SensorType.parseTypeString(sensor.type)
  }

  protected fun <T> readStateValue(stateValueKey: String, type: Class<T>): T {
    val state = stateProvider.invoke()
    logger.fine(state.toString())
    val currentState = state?.get(stateValueKey)
    return currentState as T
  }

  override fun toString(): String {
    return "Sensor{" +
        "id='" + id + '\''.toString() +
        ", name='" + name + '\''.toString() +
        ", type=" + type +
        '}'.toString()
  }

  companion object {
    private val logger = Logger.getLogger("SensorImpl")
    private val UTC_SUFFIX = "+00:00[UTC]"
  }
}
