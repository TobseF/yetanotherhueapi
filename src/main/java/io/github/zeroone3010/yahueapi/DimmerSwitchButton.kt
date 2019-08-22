package io.github.zeroone3010.yahueapi


enum class DimmerSwitchButton constructor(val buttonNumber: Int) {
  ON(1),
  DIM_UP(2),
  DIM_DOWN(3),
  OFF(4);

  companion object {

    fun parseFromButtonEventCode(buttonEvent: Int): DimmerSwitchButton {
      val buttonNumber = buttonEvent / 1000
      return values().firstOrNull { it.buttonNumber == buttonNumber } ?: throw IllegalArgumentException("Cannot parse button event $buttonEvent")
    }
  }
}
