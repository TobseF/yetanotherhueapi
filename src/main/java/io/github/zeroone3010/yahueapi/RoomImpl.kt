package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep
import io.github.zeroone3010.yahueapi.domain.Group
import io.github.zeroone3010.yahueapi.domain.GroupState
import java.util.logging.Logger

internal class RoomImpl(group: Group,
                        override val lights: Set<Light>,
                        private val stateProvider: () -> GroupState?,
                        private val stateSetter: (State) -> String) : Room {
  override val name: String?

  override val isAnyOn: Boolean
    get() = groupState.isAnyOn

  override val isAllOn: Boolean
    get() = groupState.isAllOn

  private val groupState: GroupState
    get() = stateProvider.invoke()!!

  init {
    this.name = group.name
  }

  override fun getLightByName(lightName: String): Light? {
    return lights.firstOrNull { light -> light.name == lightName }
  }

  override fun setState(state: State) {
    val result = stateSetter.invoke(state)
    logger.fine(result)
  }

  override fun setBrightness(brightness: Int) {
    setState((State.builder() as BrightnessStep).brightness(brightness).keepCurrentState())
  }

  override fun toString(): String {
    return "Room{" +
        "name='" + name + '\''.toString() +
        '}'.toString()
  }

  companion object {
    private val logger = Logger.getLogger("RoomImpl")
  }
}
