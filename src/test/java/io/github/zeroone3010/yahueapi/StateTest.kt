package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.OnOffStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.SaturationStep;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateTest {

  @Test
  void hexColorAndColorBuildersShouldYieldSameValues() {
    final State hexColorRed = State.builder().color("FF0000").on();
    final State colorRed = State.builder().color(Color.RED).on();
    assertEquals(hexColorRed, colorRed);

    final State hexColorBlue = State.builder().color("0000FF").on();
    final State colorBlue = State.builder().color(Color.BLUE).on();
    assertEquals(hexColorBlue, colorBlue);
  }

  @Test
  void testHueSatBriBuilder() {
    final State state = State.builder().hue(11).saturation(22).brightness(33).transitionTime(1000).on();
    assertEquals(11, state.getHue());
    assertEquals(22, state.getSat());
    assertEquals(33, state.getBri());
    assertEquals(1000, state.getTransitiontime());
    assertTrue(state.getOn());
  }

  @Test
  void testXyBrightnessBuilder() {
    final State state = State.builder().xy(Arrays.asList(0.33f, 0.66f)).brightness(100).keepCurrentState();
    assertEquals(0.33f, state.getXy().get(0));
    assertEquals(0.66f, state.getXy().get(1));
    assertEquals(100, state.getBri());
    assertNull(state.getOn());
  }

  @Test
  void testColorTemperatureInMireksBuilder() {
    final State state = State.builder().colorTemperatureInMireks(123).brightness(42).off();
    assertEquals(123, state.getCt());
    assertEquals(42, state.getBri());
    assertFalse(state.getOn());
  }

  @Test
  void testSettingBrightnessOnly() {
    final State state = ((BrightnessStep) State.builder()).brightness(99).keepCurrentState();
    assertEquals(99, state.getBri());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getSat());
    assertNull(state.getHue());
    assertNull(state.getOn());
  }

  @Test
  void testSettingHueOnly() {
    final State state = ((OnOffStep) State.builder().hue(99)).keepCurrentState();
    assertEquals(99, state.getHue());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getSat());
    assertNull(state.getBri());
    assertNull(state.getOn());
  }

  @Test
  void testSettingSaturationOnly() {
    final State state = ((OnOffStep) ((SaturationStep) State.builder()).saturation(99)).keepCurrentState();
    assertEquals(99, state.getSat());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getHue());
    assertNull(state.getBri());
    assertNull(state.getOn());
  }
}
