package io.github.zeroone3010.yahueapi

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.zeroone3010.yahueapi.domain.SensorDto
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

internal class SensorFactory(private val hue: Hue, private val objectMapper: ObjectMapper) {

  fun buildSensor(id: String, sensor: SensorDto?, bridgeUri: String): Sensor {
    if (sensor == null) {
      throw HueApiException("Sensor $id cannot be found.")
    }

    val url = buildSensorUrl(bridgeUri, id)

    val type = SensorType.parseTypeString(sensor.type)
    val stateProvider = createStateProvider(url, id)
    when (type) {
      SensorType.MOTION -> return MotionSensorImpl(id, sensor, url, stateProvider)
      SensorType.TEMPERATURE -> return TemperatureSensorImpl(id, sensor, url, stateProvider)
      SensorType.DAYLIGHT -> return DaylightSensorImpl(id, sensor, url, stateProvider)
      SensorType.DIMMER_SWITCH -> return DimmerSwitchImpl(id, sensor, url, stateProvider)
      else -> return BasicSensor(id, sensor, url, stateProvider)
    }
  }

  private fun createStateProvider(url: URL, id: String): () -> Map<String, Any>? {
    return { createStateProvider2(url, id) }
  }

  private fun createStateProvider2(url: URL, id: String): Map<String, Any>? {
    if (hue.isCaching) {
      return hue.raw!!.sensors!![id]?.state
    }
    try {
      return objectMapper.readValue(url, SensorDto::class.java).state
    } catch (e: IOException) {
      throw HueApiException(e)
    }
  }

}

private fun buildSensorUrl(bridgeUri: String, sensorId: String): URL {
  try {
    return URL(bridgeUri + "sensors/" + sensorId)
  } catch (e: MalformedURLException) {
    throw HueApiException(e)
  }


}
