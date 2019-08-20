package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class LightDto {
  /**
   * @param state
   */
  @set:Deprecated("This method does not actually affect the state of the light.\n" +
      "    Use the {@link io.github.zeroone3010.yahueapi.Light#setState(State)} instead.\n" +
      "    Acquire these {@code Light} objects with the {@link io.github.zeroone3010.yahueapi.Room#getLights()} and\n" +
      "    {@link io.github.zeroone3010.yahueapi.Room#getLightByName(String)} methods.")
  var state: LightState? = null
  /**
   * @param swupdate
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var swupdate: ComponentSoftwareUpdate? = null
  /**
   * @param type
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var type: String? = null
  /**
   * @param name
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var name: String? = null
  /**
   * @param modelid
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var modelid: String? = null
  /**
   * @param manufacturername
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var manufacturername: String? = null
  /**
   * @param capabilities
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var capabilities: Map<String, Any>? = null
  /**
   * @param uniqueid
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var uniqueid: String? = null
  /**
   * @param swversion
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var swversion: String? = null
  /**
   * @param swconfigid
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var swconfigid: String? = null
  /**
   * @param productid
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var productid: String? = null
  /**
   * @param productName
   */
  @JsonProperty("productname")
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var productName: String? = null
  /**
   * @param config
   */
  @set:Deprecated("The properties of the light cannot be changed with this method.")
  var config: LightConfig? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
