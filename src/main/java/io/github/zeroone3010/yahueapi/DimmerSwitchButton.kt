package io.github.zeroone3010.yahueapi

import java.util.stream.Stream

enum class DimmerSwitchButton constructor(val buttonNumber: Int) {
  ON(1),
  DIM_UP(2),
  DIM_DOWN(3),
  OFF(4);


  companion object {

    fun parseFromButtonEventCode(buttonEvent: Int): DimmerSwitchButton {
      val buttonNumber = buttonEvent / 1000
      return Stream.of(*values())
          .filter { value -> value.buttonNumber == buttonNumber }
          .findFirst()
          .orElseThrow { IllegalArgumentException("Cannot parse button event $buttonEvent") }
    }
  }
}
