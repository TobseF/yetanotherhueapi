package io.github.zeroone3010.yahueapi

import java.util.stream.Stream

enum class DimmerSwitchAction constructor(val eventCode: Int) {
  INITIAL_PRESS(0),
  HOLD(1),
  SHORT_RELEASED(2),
  LONG_RELEASED(3);


  companion object {

    fun parseFromButtonEventCode(buttonEvent: Int): DimmerSwitchAction {
      val eventCode = buttonEvent % 10
      return Stream.of(*values())
          .filter { value -> value.eventCode == eventCode }
          .findFirst()
          .orElseThrow { IllegalArgumentException("Cannot parse button event $buttonEvent") }
    }
  }
}
