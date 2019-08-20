package io.github.zeroone3010.yahueapi

interface Light {
  /**
   * Returns the name of the light, as set by the user.
   *
   * @return Name of the light.
   */
  val name: String

  /**
   * Queries the light state -- or returns the cached state if caching
   * has been enabled with [Hue.setCaching].
   *
   * @return True if the light is on, false if it is off.
   */
  val isOn: Boolean

  /**
   * Queries the reachability of the light -- or returns the cached state if caching
   * has been enabled with [Hue.setCaching].
   *
   * @return True if the light is reachable, false if it is not.
   */
  val isReachable: Boolean

  /**
   * Returns info on the type of the light.
   *
   * @return An enum value specifying the color and dimming capabilities of the light.
   */
  val type: LightType

  /**
   * Gets the state of the light -- or returns the cached state if caching
   * has been enabled with [Hue.setCaching].
   *
   * @return The current state of the light.
   */
  /**
   * Sets a state for the light.
   *
   * @param state A state to be set for this light.
   */
  var state: State

  /**
   * Turns the light on.
   */
  fun turnOn()

  /**
   * Turns the light off.
   */
  fun turnOff()

  /**
   * Sets the brightness of the light. If the light is off, does not turn it on, nor does `0` turn it off.
   *
   * @param brightness A value from `0` (minimum brightness) to `254` (maximum brightness).
   */
  fun setBrightness(brightness: Int)
}
