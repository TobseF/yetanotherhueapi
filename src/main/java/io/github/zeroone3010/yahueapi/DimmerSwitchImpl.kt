package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.domain.SensorDto
import java.net.URL

internal class DimmerSwitchImpl(id: String, sensor: SensorDto, url: URL, stateProvider: () -> Map<String, Any>?) : BasicSensor(id, sensor, url, stateProvider), DimmerSwitch {

  override val latestButtonEvent: DimmerSwitchButtonEvent
    get() = DimmerSwitchButtonEvent(readStateValue("buttonevent", Int::class.java))

  override fun toString(): String {
    return "DimmerSwitch{" +
        "id='" + super.id + '\''.toString() +
        ", name='" + super.name + '\''.toString() +
        ", type=" + super.type +
        '}'.toString()
  }
}
