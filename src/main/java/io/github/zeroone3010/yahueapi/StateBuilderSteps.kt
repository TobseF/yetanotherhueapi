package io.github.zeroone3010.yahueapi

import common.util.Color


object StateBuilderSteps {

  interface InitialStep : HueStep, XyStep, ColorStep, ColorTemperatureStep

  interface HueStep {
    /**
     * Hue.
     *
     * @param hue A value from `0` to `65280`.
     * @return The next step of this state builder
     */
    fun hue(hue: Int): SaturationStep
  }

  interface SaturationStep {
    /**
     * Saturation.
     *
     * @param saturation A value from 0 (white) to 254 (most saturated).
     * @return The next step of this state builder
     */
    fun saturation(saturation: Int): BrightnessStep
  }

  interface BrightnessStep {
    /**
     * Brightness.
     *
     * @param brightness A value from `1` (minimum brightness) to `254` (maximum brightness).
     * @return The next step of this state builder
     */
    fun brightness(brightness: Int): BuildStep
  }

  interface XyStep {
    /**
     * Color.
     *
     * @param xy The x and y coordinates of the C.I.E. chromaticity diagram. Exactly two values between 0 and 1 required.
     * @return The next step of this state builder
     */
    fun xy(xy: List<Float>?): BrightnessStep
  }

  interface ColorStep {
    /**
     * Color.
     *
     * @param color The color as a [java.awt.Color] object.
     * @return The next step of this state builder
     */
    fun color(color: Color?): BuildStep

    /**
     * Color.
     *
     * @param color The color as a hexadecimal string, for example "#ff0000" for red.
     * @return The next step of this state builder
     */
    fun color(color: String): BuildStep
  }

  interface ColorTemperatureStep {
    /**
     * Color temperature.
     *
     * @param colorTemperature The color temperature in mireks. Must be between `153` (6500K) and `500` (2000K)
     * @return The next step of this state builder
     */
    fun colorTemperatureInMireks(colorTemperature: Int): BrightnessStep
  }

  interface TransitionTimeStep {
    /**
     * Transition time.
     *
     * @param tenths Transition time in tenths of seconds, i.e. "4" equals "0.4 seconds".
     * @return The next step of this state builder
     */
    fun transitionTime(tenths: Int): OnOffStep
  }

  interface BuildStep : OnOffStep, TransitionTimeStep

  interface OnOffStep {
    fun on(on: Boolean?): State

    /**
     * Creates a new `State` that will turn on the light to which it is assigned.
     * A shorthand method for `on(true)`. Avoid calling in vain if you know the light is on already.
     *
     * @return A new `State`.
     */
    fun on(): State {
      return on(true)
    }

    /**
     * Creates a new `State` that will turn off the light to which it is assigned.
     * A shorthand method for `on(false)`.
     *
     * @return A new `State`.
     */
    fun off(): State {
      return on(false)
    }

    /**
     * Keeps the current state of the light to which this state is assigned: if it's on,
     * it stays on, and if it's off, it stays off.
     * A shorthand method for `on(null)`.
     *
     * @return A new `State`.
     */
    fun keepCurrentState(): State {
      return on(null)
    }
  }
}// prevent instantiation
