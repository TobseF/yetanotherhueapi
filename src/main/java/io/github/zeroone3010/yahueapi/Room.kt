package io.github.zeroone3010.yahueapi

/**
 * A room or a zone that has been configured into the Hue Bridge.
 */
interface Room {
  /**
   * Returns the name of the room or zone, as set by the user.
   *
   * @return Name of the room or zone.
   */
  val name: String?

  /**
   * Returns all the lights that have been assigned to this room or zone.
   *
   * @return A Collection of Light objects.
   */
  val lights: Collection<Light>

  /**
   * Queries the state of the room or zone.
   *
   * @return True if any light is on in this room or zone, false if not.
   */
  val isAnyOn: Boolean

  /**
   * Queries the state of the room or zone.
   *
   * @return True if all lights in this room or zone are on, false if they are not.
   */
  val isAllOn: Boolean

  /**
   * Returns one light, if found by the given name.
   *
   * @param lightName Name of a light in this room or zone.
   * @return Optional.empty() if a light is not found by this name, an Optional&lt;Light&gt; if it is.
   */
  fun getLightByName(lightName: String): Light?

  /**
   * Sets a state for the room or zone.
   *
   * @param state A state to be set for this room or zone.
   */
  fun setState(state: State)

  /**
   * Sets the brightness of the room. If the lights in the room are off, does not turn them on, nor does `0` turn them off.
   *
   * @param brightness A value from `0` (minimum brightness) to `254` (maximum brightness).
   * @since 1.2.0
   */
  fun setBrightness(brightness: Int)
}
