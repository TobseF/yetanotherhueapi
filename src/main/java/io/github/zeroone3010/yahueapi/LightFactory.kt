package io.github.zeroone3010.yahueapi

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.zeroone3010.yahueapi.domain.LightDto
import io.github.zeroone3010.yahueapi.domain.LightState
import io.github.zeroone3010.yahueapi.domain.Root
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

internal class LightFactory(private val hue: Hue, private val objectMapper: ObjectMapper) {

  fun buildLight(lightId: String, root: Root, bridgeUri: String): Light {
    try {
      val url = URL(bridgeUri + "lights/" + lightId)
      return LightImpl(
          lightId,
          root.lights!![lightId],
          createStateProvider(url, lightId),
          stateSetter(url))
    } catch (e: MalformedURLException) {
      throw HueApiException(e)
    }

  }

  private fun createStateProvider(url: URL,
                                  id: String): () -> LightState? {
    return { createStateProvider2(url, id) }
  }

  private fun createStateProvider2(url: URL,
                                   id: String): LightState? {
    if (hue.isCaching) {
      return hue.raw!!.lights!![id]?.state
    }
    try {
      return objectMapper.readValue(url, LightDto::class.java).state
    } catch (e: IOException) {
      throw HueApiException(e)
    }

  }

  private fun stateSetter(url: URL): (State) -> String {
    return { state -> stateSetter2(url, state) }
  }

  private fun stateSetter2(url: URL, state: State): String {
    val body: String
    try {
      body = objectMapper.writeValueAsString(state)
    } catch (e: JsonProcessingException) {
      throw HueApiException(e)
    }
    return HttpUtil.put(url, ACTION_PATH, body)
  }

  companion object {
    private val ACTION_PATH = "/state"
  }
}
