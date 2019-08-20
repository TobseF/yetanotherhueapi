package io.github.zeroone3010.yahueapi

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.zeroone3010.yahueapi.domain.Group
import io.github.zeroone3010.yahueapi.domain.GroupState
import io.github.zeroone3010.yahueapi.domain.Root
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

internal class RoomFactory(private val hue: Hue, private val objectMapper: ObjectMapper, private val bridgeUri: String) {
  private val lightFactory: LightFactory

  init {
    this.lightFactory = LightFactory(hue, objectMapper)
  }

  fun buildRoom(groupId: String, group: Group, root: Root): Room {

    val lights = group.lights!!
        .map { lightId -> buildLight(lightId, root) }.toSet()
    try {
      val url = URL(bridgeUri + "groups/" + groupId)
      return RoomImpl(
          group,
          lights,
          createStateProvider(url, groupId),
          stateSetter(url))
    } catch (e: MalformedURLException) {
      throw HueApiException(e)
    }

  }

  private fun buildLight(lightId: String, root: Root): Light {
    return lightFactory.buildLight(lightId, root, bridgeUri)
  }

  private fun createStateProvider(url: URL,
                                  id: String): () -> GroupState? {
    return { createStateProvider2(url, id) }
  }

  private fun createStateProvider2(url: URL,
                                   id: String): GroupState? {
    if (hue.isCaching) {
      return hue.raw!!.groups!![id]?.state
    }
    try {
      return objectMapper.readValue(url, Group::class.java).state
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
    private const val ACTION_PATH = "/action"
  }
}
