package io.github.zeroone3010.yahueapi

import common.util.Color
import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.OnOffStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.SaturationStep
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StateTest {

  @Test
  fun hexColorAndColorBuildersShouldYieldSameValues() {
    val hexColorRed = State.builder().color("FF0000").on()
    val colorRed = State.builder().color(Color.RED).on()
    assertEquals(hexColorRed, colorRed)

    val hexColorBlue = State.builder().color("0000FF").on()
    val colorBlue = State.builder().color(Color.BLUE).on()
    assertEquals(hexColorBlue, colorBlue)
  }

  @Test
  fun testHueSatBriBuilder() {
    val state = State.builder().hue(11).saturation(22).brightness(33).transitionTime(1000).on()
    assertEquals(11, state.hue)
    assertEquals(22, state.sat)
    assertEquals(33, state.bri)
    assertEquals(1000, state.transitiontime)
    assertTrue(state.on!!)
  }

  @Test
  fun testXyBrightnessBuilder() {
    val state = State.builder().xy(listOf(0.33f, 0.66f)).brightness(100).keepCurrentState()
    assertEquals(0.33f, state.xy!![0])
    assertEquals(0.66f, state.xy!![1])
    assertEquals(100, state.bri)
    assertNull(state.on)
  }

  @Test
  fun testColorTemperatureInMireksBuilder() {
    val state = State.builder().colorTemperatureInMireks(123).brightness(42).off()
    assertEquals(123, state.ct)
    assertEquals(42, state.bri)
    assertFalse(state.on!!)
  }

  @Test
  fun testSettingBrightnessOnly() {
    val state = (State.builder() as BrightnessStep).brightness(99).keepCurrentState()
    assertEquals(99, state.bri)
    assertNull(state.ct)
    assertNull(state.xy)
    assertNull(state.transitiontime)
    assertNull(state.sat)
    assertNull(state.hue)
    assertNull(state.on)
  }

  @Test
  fun testSettingHueOnly() {
    val state = (State.builder().hue(99) as OnOffStep).keepCurrentState()
    assertEquals(99, state.hue)
    assertNull(state.ct)
    assertNull(state.xy)
    assertNull(state.transitiontime)
    assertNull(state.sat)
    assertNull(state.bri)
    assertNull(state.on)
  }

  @Test
  fun testSettingSaturationOnly() {
    val state = ((State.builder() as SaturationStep).saturation(99) as OnOffStep).keepCurrentState()
    assertEquals(99, state.sat)
    assertNull(state.ct)
    assertNull(state.xy)
    assertNull(state.transitiontime)
    assertNull(state.hue)
    assertNull(state.bri)
    assertNull(state.on)
  }
}
