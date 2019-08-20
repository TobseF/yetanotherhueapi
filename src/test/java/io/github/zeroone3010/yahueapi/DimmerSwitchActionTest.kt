package io.github.zeroone3010.yahueapi

import io.github.zeroone3010.yahueapi.DimmerSwitchAction.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DimmerSwitchActionTest {
  @Test
  fun shouldParseInitialPress() {
    assertEquals(INITIAL_PRESS, DimmerSwitchAction.parseFromButtonEventCode(1000))
  }

  @Test
  fun shouldParseHold() {
    assertEquals(HOLD, DimmerSwitchAction.parseFromButtonEventCode(1001))
  }

  @Test
  fun shouldParseShortReleased() {
    assertEquals(SHORT_RELEASED, DimmerSwitchAction.parseFromButtonEventCode(2002))
  }

  @Test
  fun shouldParseLongReleased() {
    assertEquals(LONG_RELEASED, DimmerSwitchAction.parseFromButtonEventCode(4003))
  }
}
