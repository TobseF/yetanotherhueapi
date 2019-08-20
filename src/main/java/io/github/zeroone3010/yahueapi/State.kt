package io.github.zeroone3010.yahueapi

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import common.util.Color
import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.BuildStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.ColorStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.ColorTemperatureStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.HueStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.InitialStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.OnOffStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.SaturationStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.TransitionTimeStep
import io.github.zeroone3010.yahueapi.StateBuilderSteps.XyStep
import io.github.zeroone3010.yahueapi.domain.LightState
import java.util.*
import java.util.logging.Logger
import kotlin.math.pow

@JsonInclude(Include.NON_NULL)
class State private constructor(builder: Builder) {

  val on: Boolean?
  val hue: Int?
  val sat: Int?
  val bri: Int?
  val ct: Int?
  val transitiontime: Int?
  val xy: List<Float>?

  init {
    this.on = builder.on
    this.bri = builder.bri
    this.xy = builder.xy
    this.hue = builder.hue
    this.sat = builder.sat
    this.ct = builder.ct
    this.transitiontime = builder.transitionTime
  }


  class Builder : InitialStep, HueStep, SaturationStep, BrightnessStep, XyStep, ColorStep, ColorTemperatureStep, TransitionTimeStep, BuildStep, OnOffStep {
    var on: Boolean? = null
    var hue: Int? = null
    var sat: Int? = null
    var bri: Int? = null
    var ct: Int? = null
    var transitionTime: Int? = null
    var xy: List<Float>? = null

    override fun hue(hue: Int): SaturationStep {
      this.hue = hue
      return this
    }

    override fun saturation(saturation: Int): BrightnessStep {
      this.sat = saturation
      return this
    }

    override fun brightness(brightness: Int): BuildStep {
      this.bri = brightness
      return this
    }

    override fun xy(xy: List<Float>?): BrightnessStep {
      if (xy == null || xy.size != 2 || !isInRange(xy[0], 0f, 1f) || !isInRange(xy[1], 0f, 1f)) {
        throw IllegalArgumentException("The xy list must contain exactly 2 values, between 0 and 1.")
      }
      val xyValues = ArrayList<Float>()
      xyValues.addAll(xy)
      this.xy = Collections.unmodifiableList(xyValues)
      return this
    }

    private fun isInRange(value: Float?, min: Float, max: Float): Boolean {
      return value != null && value >= min && value <= max
    }

    override fun color(color: Color?): BuildStep {
      if (color == null) {
        throw IllegalArgumentException("Color must not be null")
      }
      val xAndYAndBrightness = rgbToXy(color)
      this.xy = xAndYAndBrightness.xy
      this.bri = xAndYAndBrightness.brightness
      return this
    }

    override fun color(color: String): BuildStep {
      return color(hexToColor(color))
    }

    override fun colorTemperatureInMireks(colorTemperature: Int): BrightnessStep {
      this.ct = colorTemperature
      return this
    }

    override fun transitionTime(tenths: Int): OnOffStep {
      this.transitionTime = tenths
      return this
    }

    override fun on(on: Boolean?): State {
      this.on = on
      return build()
    }

    private fun build(): State {
      return State(this)
    }


    private fun hexToColor(hexColor: String): Color {
      return Color.valueOf(hexColor)
    }

    private fun rgbToXy(color: Color): XAndYAndBrightness {
      val red = color.r / 255f
      val green = color.g / 255f
      val blue = color.b / 255f
      val r = gammaCorrection(red)
      val g = gammaCorrection(green)
      val b = gammaCorrection(blue)
      val rgbX = r * 0.664511f + g * 0.154324f + b * 0.162028f
      val rgbY = r * 0.283881f + g * 0.668433f + b * 0.047685f
      val rgbZ = r * 0.000088f + g * 0.072310f + b * 0.986039f
      val x = (rgbX / (rgbX + rgbY + rgbZ))
      val y = (rgbY / (rgbX + rgbY + rgbZ))
      return XAndYAndBrightness(x, y, (rgbY * 255f).toInt())
    }

    private fun gammaCorrection(component: Float): Float {
      return (if (component > 0.04045f) ((component + 0.055f) / (1.0f + 0.055f)).toDouble().pow(2.4).toFloat() else component / 12.92f)
    }
  }


  private class XAndYAndBrightness internal constructor(internal val x: Float, internal val y: Float, internal val brightness: Int) {

    internal val xy: List<Float>
      get() {
        val xyColor = ArrayList<Float>()
        xyColor.add(this.x)
        xyColor.add(this.y)
        return xyColor
      }

    override fun toString(): String {
      try {
        return ObjectMapper().writeValueAsString(this)
      } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
      }

    }
  }

  companion object {
    private val logger = Logger.getLogger("State")

    private const val DIMMABLE_LIGHT_COLOR_TEMPERATURE = 370

    fun builder(): InitialStep {
      return Builder()
    }

    internal fun build(state: LightState): State {
      logger.fine(state.toString())
      if (state.colorMode == null) {
        return State.builder().colorTemperatureInMireks(DIMMABLE_LIGHT_COLOR_TEMPERATURE).brightness(state.brightness).on(state.isOn)
      }
      when (state.colorMode) {
        "xy" -> return State.builder().xy(state.xy).brightness(state.brightness).on(state.isOn)
        "ct" -> return State.builder().colorTemperatureInMireks(state.ct).brightness(state.brightness).on(state.isOn)
        "hs" -> return State.builder().hue(state.hue).saturation(state.saturation).brightness(state.brightness).on(state.isOn)
      }
      throw HueApiException("Unknown color mode '" + state.colorMode + "'.")
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as State

    if (on != other.on) return false
    if (hue != other.hue) return false
    if (sat != other.sat) return false
    if (bri != other.bri) return false
    if (ct != other.ct) return false
    if (xy != other.xy) return false

    return true
  }

  override fun hashCode(): Int {
    var result = on?.hashCode() ?: 0
    result = 31 * result + (hue ?: 0)
    result = 31 * result + (sat ?: 0)
    result = 31 * result + (bri ?: 0)
    result = 31 * result + (ct ?: 0)
    result = 31 * result + (xy?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "State(on=$on, hue=$hue, sat=$sat, bri=$bri, ct=$ct, xy=$xy)"
  }


}
