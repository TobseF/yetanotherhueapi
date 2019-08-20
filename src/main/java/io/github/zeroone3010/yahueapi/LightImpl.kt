package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.domain.LightDto
import io.github.zeroone3010.yahueapi.domain.LightState
import java.util.logging.Logger

internal class LightImpl(protected val id: String, light: LightDto?, private val stateProvider: () -> LightState?,
                         private val stateSetter: (State) -> String) : Light {
  override val name: String
  override val type: LightType

  override val isOn: Boolean
    get() = lightState.isOn

  override val isReachable: Boolean
    get() = lightState.isReachable

  private val lightState: LightState
    get() {
      val state = stateProvider.invoke()!!
      logger.fine(state.toString())
      return state
    }

  override var state: State
    get() = State.build(lightState)
    set(state) {
      val result = stateSetter.invoke(state)
      logger.fine(result)
    }

  init {
    if (light == null) {
      throw HueApiException("Light $id cannot be found.")
    }
    this.name = light.name ?: ""
    this.type = LightType.parseTypeString(light.type)
  }

  override fun turnOn() {
    state = (State.builder() as StateBuilderSteps.OnOffStep).on()
  }

  override fun turnOff() {
    state = (State.builder() as StateBuilderSteps.OnOffStep).off()
  }

  override fun setBrightness(brightness: Int) {
    state = (State.builder() as StateBuilderSteps.BrightnessStep).brightness(brightness).keepCurrentState()
  }

  override fun toString(): String {
    return "Light{" +
        "id='" + id + '\''.toString() +
        ", name='" + name + '\''.toString() +
        ", type=" + type +
        '}'.toString()
  }

  companion object {
    private val logger = Logger.getLogger("LightImpl")
  }
}
