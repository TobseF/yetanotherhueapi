package io.github.zeroone3010.yahueapi

class DimmerSwitchButtonEvent internal constructor(val button: DimmerSwitchButton, val action: DimmerSwitchAction) {

  internal constructor(buttonEventCode: Int) : this(DimmerSwitchButton.parseFromButtonEventCode(buttonEventCode),
      DimmerSwitchAction.parseFromButtonEventCode(buttonEventCode))

  override fun toString(): String {
    return "DimmerSwitchButtonEvent{" +
        "button=" + button +
        ", action=" + action +
        '}'.toString()
  }
}
