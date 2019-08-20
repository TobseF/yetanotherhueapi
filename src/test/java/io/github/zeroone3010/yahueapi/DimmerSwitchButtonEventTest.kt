package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.DimmerSwitchButton.DIM_UP
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DimmerSwitchButtonEventTest {
  @Test
  fun testInitializationByButtonEvent() {
    val event = DimmerSwitchButtonEvent(2003)
    assertEquals(DIM_UP, event.button)
    assertEquals(DimmerSwitchAction.LONG_RELEASED, event.action)
  }
}
