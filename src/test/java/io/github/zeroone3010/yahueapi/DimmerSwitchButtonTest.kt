package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.DimmerSwitchButton.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DimmerSwitchButtonTest {
  @Test
  fun shouldParseOn() {
    assertEquals(ON, DimmerSwitchButton.parseFromButtonEventCode(1000))
  }

  @Test
  fun shouldParseDimUp() {
    assertEquals(DIM_UP, DimmerSwitchButton.parseFromButtonEventCode(2000))
  }

  @Test
  fun shouldParseDimDown() {
    assertEquals(DIM_DOWN, DimmerSwitchButton.parseFromButtonEventCode(3002))
  }

  @Test
  fun shouldParseOff() {
    assertEquals(OFF, DimmerSwitchButton.parseFromButtonEventCode(4003))
  }
}
