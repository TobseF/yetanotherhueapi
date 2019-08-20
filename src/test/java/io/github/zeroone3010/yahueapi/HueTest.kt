package io.github.zeroone3010.yahueapi

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.nio.file.Files
import java.time.*
import java.util.*
import java.util.stream.Collectors

internal class HueTest {

  val wireMockServer = WireMockServer(wireMockConfig().dynamicPort())

  @BeforeEach
  fun startServer() {
    wireMockServer.start()
  }

  @AfterEach
  fun stopServer() {
    wireMockServer.stop()
  }

  private fun createHueAndInitializeMockServer(): Hue {
    val hueRoot = readFile("hueRoot.json")

    val objectMapper = ObjectMapper()
    val jsonNode: JsonNode
    try {
      jsonNode = objectMapper.readTree(hueRoot)
      wireMockServer.stubFor(get(API_BASE_PATH).willReturn(okJson(hueRoot)))
      mockIndividualGetResponse(jsonNode, "lights", "100")
      mockIndividualGetResponse(jsonNode, "lights", "101")
      mockIndividualGetResponse(jsonNode, "lights", "200")
      mockIndividualGetResponse(jsonNode, "lights", "300")
      mockIndividualGetResponse(jsonNode, "sensors", "1")
      mockIndividualGetResponse(jsonNode, "sensors", "4")
      mockIndividualGetResponse(jsonNode, "sensors", "15")
      mockIndividualGetResponse(jsonNode, "sensors", "16")
      mockIndividualGetResponse(jsonNode, "groups", "1")
      mockIndividualGetResponse(jsonNode, "groups", "2")
    } catch (e: IOException) {
      throw RuntimeException(e)
    }

    return Hue("localhost:" + wireMockServer.port(), API_KEY)
  }

  @Throws(JsonProcessingException::class)
  private fun mockIndividualGetResponse(hueRoot: JsonNode, itemClass: String, id: String) {
    val objectMapper = ObjectMapper()
    val json = objectMapper.writeValueAsString(hueRoot.get(itemClass).get(id))
    wireMockServer.stubFor(get("$API_BASE_PATH$itemClass/$id").willReturn(okJson(json)))
  }

  @Test
  fun testInitializationAndRefresh() {
    val hue = createHueAndInitializeMockServer()
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH)))

    hue.refresh()
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))

    hue.refresh()
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)))
  }

  @Test
  fun testGetRooms() {
    val hue = createHueAndInitializeMockServer()

    assertEquals(3, hue.getRooms().size)
    hue.getRooms()
    hue.getRooms()
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
  }

  @Test
  fun testGetRoomByName() {
    val hue = createHueAndInitializeMockServer()

    assertEquals(2, hue.getRoomByName("Living room")!!.lights.size)
    assertEquals(1, hue.getRoomByName("Bedroom")!!.lights.size)
    assertFalse(hue.getRoomByName("No such room") != null)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
  }

  @Test
  fun testGetZones() {
    val hue = createHueAndInitializeMockServer()

    assertEquals(1, hue.getZones().size)
    hue.getZones()
    hue.getZones()
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
  }

  @Test
  fun testGetZoneByName() {
    val hue = createHueAndInitializeMockServer()

    assertEquals(2, hue.getZoneByName("Path to toilet")!!.lights.size)
    assertFalse(hue.getZoneByName("No such zone") != null)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
  }

  @Test
  fun testGetLightReachability() {
    val hue = createHueAndInitializeMockServer()

    assertTrue(hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!.isReachable)
    assertFalse(hue.getRoomByName("Bedroom")!!.getLightByName("Pendant")!!.isReachable)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
  }

  @Test
  fun testLightTypes() {
    val hue = createHueAndInitializeMockServer()
    assertEquals(LightType.EXTENDED_COLOR,
        hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!.type)
    assertEquals(LightType.COLOR_TEMPERATURE,
        hue.getRoomByName("Living room")!!.getLightByName("LR 2")!!.type)
    assertEquals(LightType.COLOR,
        hue.getRoomByName("Hallway 1")!!.getLightByName("LED strip 1")!!.type)
  }

  @Test
  fun testGetLightStateWhenXyMode() {
    val hue = createHueAndInitializeMockServer()
    val state = hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!.state
    assertFalse(state.on!!)
    assertEquals(0.3689f, state.xy!![0])
    assertEquals(0.3719f, state.xy!![1])
    assertEquals(254, state.bri!!.toInt())
    assertNull(state.hue)
    assertNull(state.sat)
    assertNull(state.ct)

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100")))
  }

  @Test
  fun testGetLightStateWhenCtMode() {
    val hue = createHueAndInitializeMockServer()
    val state = hue.getRoomByName("Living room")!!.getLightByName("LR 2")!!.state
    assertFalse(state.on!!)
    assertNull(state.xy)
    assertEquals(123, state.bri!!.toInt())
    assertEquals(230, state.ct!!.toInt())
    assertNull(state.hue)
    assertNull(state.sat)

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/101")))
  }

  @Test
  fun testGetLightStateWhenHsMode() {
    val hue = createHueAndInitializeMockServer()
    val state = hue.getRoomByName("Hallway 1")!!
        .getLightByName("LED strip 1")!!.state

    assertTrue(state.on!!)
    assertNull(state.xy)
    assertEquals(42, state.bri!!.toInt())
    assertEquals(38677, state.hue!!.toInt())
    assertEquals(61, state.sat!!.toInt())
    assertNull(state.ct)

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))
  }

  @Test
  fun testGetLightStateQueriesTheBridgeEveryTimeWhenCachingIsOffByDefault() {
    val hue = createHueAndInitializeMockServer()
    val light = hue.getRoomByName("Hallway 1")!!.getLightByName("LED strip 1")!!
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))
  }

  @Test
  fun testGetLightStateQueriesTheBridgeEveryTimeWhenCachingIsOffExplicitly() {
    val hue = createHueAndInitializeMockServer()
    hue.isCaching = false
    val light = hue.getRoomByName("Hallway 1")!!.getLightByName("LED strip 1")!!
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))
  }

  @Test
  fun testGetLightStateIsNotQueriedFromTheBridgeWhenCachingIsOn() {
    val hue = createHueAndInitializeMockServer()
    hue.isCaching = true
    val light = hue.getRoomByName("Hallway 1")!!.getLightByName("LED strip 1")!!
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    assertTrue(light.state.on!!)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))
  }

  @Test
  fun testGetLightStateWhenTogglingCachingOnAndOff() {
    val hue = createHueAndInitializeMockServer()

    hue.isCaching = true
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    val light = hue.getRoomByName("Hallway 1")!!.getLightByName("LED strip 1")!!
    light.state
    light.state
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))

    hue.isCaching = false
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    light.state
    light.state
    light.state
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))

    hue.isCaching = true
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    light.state
    light.state
    light.state
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")))
  }

  @Test
  fun testGetUnknownSensors() {
    val hue = createHueAndInitializeMockServer()
    val sensors = hue.unknownSensors
    assertEquals(2, sensors.size)
  }

  @Test
  fun testGetTemperatureSensorByName() {
    val hue = createHueAndInitializeMockServer()
    assertNotNull(hue.getTemperatureSensorByName(TEMPERATURE_SENSOR_NAME))
    assertNull(hue.getTemperatureSensorByName("No such sensor"))
  }

  @Test
  fun testGetMotionSensorByName() {
    val hue = createHueAndInitializeMockServer()
    assertTrue(hue.getMotionSensorByName(MOTION_SENSOR_NAME) != null)
    assertFalse(hue.getMotionSensorByName("No such sensor") != null)
  }

  @Test
  fun testMotionSensorLastUpdated() {
    val hue = createHueAndInitializeMockServer()
    val actual = hue.getMotionSensorByName(MOTION_SENSOR_NAME)!!.lastUpdated
    val expected = ZonedDateTime.of(LocalDate.of(2018, Month.JULY, 29),
        LocalTime.of(6, 6, 6), ZoneId.of("UTC"))
    assertEquals(expected, actual)
  }

  @Test
  fun testMotionSensorPresence() {
    val hue = createHueAndInitializeMockServer()
    assertTrue(hue.getMotionSensorByName(MOTION_SENSOR_NAME) != null)
  }

  @Test
  fun testTemperatureSensorTemperature() {
    val hue = createHueAndInitializeMockServer()
    val temperature = hue.getTemperatureSensorByName(TEMPERATURE_SENSOR_NAME)!!.degreesCelsius.toDouble()
    assertEquals(29.53, temperature)
  }

  @Test
  fun testDaylightSensor() {
    val hue = createHueAndInitializeMockServer()
    val daylight = hue.daylightSensors
        .map { it.isDaylightTime }
        .firstOrNull()
    assertTrue(daylight != null)
  }

  @Test
  fun testDimmerSwitchLastUpdated() {
    val hue = createHueAndInitializeMockServer()
    val actual = hue.getDimmerSwitchByName(DIMMER_SWITCH_NAME)!!.lastUpdated
    val expected = ZonedDateTime.of(LocalDate.of(2018, Month.JULY, 28),
        LocalTime.of(21, 12, 0), ZoneId.of("UTC"))
    assertEquals(expected, actual)
  }

  @Test
  fun testDimmerSwitchLastButtonEvent() {
    val hue = createHueAndInitializeMockServer()
    val event = hue.getDimmerSwitchByName(DIMMER_SWITCH_NAME)!!.latestButtonEvent
    assertEquals(DimmerSwitchAction.SHORT_RELEASED, event.action)
    assertEquals(DimmerSwitchButton.OFF, event.button)
  }

  @Test
  fun testGetSensorStateQueriesTheBridgeEveryTimeWhenCachingIsOffByDefault() {
    val hue = createHueAndInitializeMockServer()
    val sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1")
    assertEquals(BigDecimal("29.53"), sensor!!.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")))
  }

  @Test
  fun testGetSensorStateQueriesTheBridgeEveryTimeWhenCachingIsOffExplicitly() {
    val hue = createHueAndInitializeMockServer()
    hue.isCaching = false
    val sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1")
    assertEquals(BigDecimal("29.53"), sensor!!.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")))
  }

  @Test
  fun testGetSensorStateIsNotQueriedFromTheBridgeWhenCachingIsOn() {
    val hue = createHueAndInitializeMockServer()
    hue.isCaching = true
    val sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1")
    assertEquals(BigDecimal("29.53"), sensor!!.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    assertEquals(BigDecimal("29.53"), sensor.degreesCelsius)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")))
  }

  @Test
  fun testGetSensorStateWhenTogglingCachingOnAndOff() {
    val hue = createHueAndInitializeMockServer()

    hue.isCaching = true
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    val sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1")
    sensor!!.degreesCelsius
    sensor.degreesCelsius
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")))

    hue.isCaching = false
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    sensor.degreesCelsius
    sensor.degreesCelsius
    sensor.degreesCelsius
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")))

    hue.isCaching = true
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    sensor.degreesCelsius
    sensor.degreesCelsius
    sensor.degreesCelsius
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")))
  }

  @Test
  fun testSetLightBrightness() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"bri\":42}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/bri\":42}}]")))

    val hue = createHueAndInitializeMockServer()
    val light = hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!
    light.setBrightness(42)

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(EqualToJsonPattern("{\"bri\":42}", false, false)))
  }

  @Test
  fun testTurnLightOff() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"on\":false}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/on\":false}}]")))

    val hue = createHueAndInitializeMockServer()
    val light = hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!
    light.turnOff()

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(EqualToJsonPattern("{\"on\":false}", false, false)))
  }

  @Test
  fun testTurnLightOn() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"on\":true}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/on\":true}}]")))

    val hue = createHueAndInitializeMockServer()
    val light = hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!
    light.turnOn()

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(EqualToJsonPattern("{\"on\":true}", false, false)))
  }

  @Test
  fun testSetLightState() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"hue\":1, \"sat\":2, \"bri\":3}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/on\":true}}]")))

    val hue = createHueAndInitializeMockServer()
    val light = hue.getRoomByName("Living room")!!.getLightByName("LR 1")!!
    light.state = State.builder().hue(1).saturation(2).brightness(3).keepCurrentState()

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(EqualToJsonPattern("{\"hue\":1, \"sat\":2, \"bri\":3}", false, false)))
  }

  @Test
  fun testSetRoomBrightness() {
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1/action")
        .willReturn(okJson("[{\"success\":{\"/gruops/1/action/bri\":42}}]")))

    val hue = createHueAndInitializeMockServer()
    hue.getRoomByName("Living room")!!.setBrightness(42)

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(EqualToPattern("{\"bri\":42}", false)))
  }

  @Test
  fun testGetRoomStateQueriesTheBridgeEveryTimeWhenCachingIsOffByDefault() {
    val hue = createHueAndInitializeMockServer()
    val room = hue.getRoomByName("Living room")!!
    assertFalse(room.isAnyOn)
    assertFalse(room.isAllOn)
    assertFalse(room.isAnyOn)
    assertFalse(room.isAllOn)
    assertFalse(room.isAnyOn)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1")))
  }

  @Test
  fun testGetRoomStateQueriesTheBridgeEveryTimeWhenCachingIsOffExplicitly() {
    val hue = createHueAndInitializeMockServer()
    hue.isCaching = false
    val room = hue.getRoomByName("Living room")!!
    assertFalse(room.isAnyOn)
    assertFalse(room.isAllOn)
    assertFalse(room.isAnyOn)
    assertFalse(room.isAllOn)
    assertFalse(room.isAnyOn)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1")))
  }

  @Test
  fun testGetRoomStateIsNotQueriedFromTheBridgeWhenCachingIsOn() {
    val hue = createHueAndInitializeMockServer()
    hue.isCaching = true
    val room = hue.getRoomByName("Living room")!!
    assertFalse(room.isAnyOn)
    assertFalse(room.isAllOn)
    assertFalse(room.isAnyOn)
    assertFalse(room.isAllOn)
    assertFalse(room.isAnyOn)
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1")))
  }

  @Test
  fun testGetRoomStateWhenTogglingCachingOnAndOff() {
    val hue = createHueAndInitializeMockServer()

    hue.isCaching = true
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    val room = hue.getRoomByName("Bedroom")!!
    assertTrue(room.isAnyOn)
    assertTrue(room.isAllOn)
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2")))

    hue.isCaching = false
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    assertTrue(room.isAnyOn)
    assertTrue(room.isAllOn)
    assertTrue(room.isAnyOn)
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2")))

    hue.isCaching = true
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH)))
    assertTrue(room.isAllOn)
    assertTrue(room.isAnyOn)
    assertTrue(room.isAllOn)
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2")))
  }

  @Test
  fun testGetRawRules() {
    val hue = createHueAndInitializeMockServer()
    val root = hue.raw
    assertEquals(2, root!!.rules!!.size)
    assertEquals("Dimmer Switch 4 on0", root.rules!!["1"]!!.name)
    assertEquals("6655664454522131aaaeeaaeaeaeaea", root.rules!!["1"]!!.owner)
    assertEquals("2017-07-14T14:04:04", root.rules!!["1"]!!.created)
    assertEquals("2018-07-27T13:00:13", root.rules!!["1"]!!.lastTriggered)
    assertEquals(5, root.rules!!["1"]!!.timesTriggered)
    assertEquals("enabled", root.rules!!["1"]!!.status)
    assertEquals(true, root.rules!!["1"]!!.isRecycle)
    val conditions = root.rules!!["1"]!!.conditions
    assertEquals(3, conditions!!.size)
    assertEquals("/sensors/4/state/buttonevent", conditions[0].address)
    assertEquals("eq", conditions[0].operator)
    assertEquals("1000", conditions[0].value)
    val actions = root.rules!!["1"]!!.actions
    assertEquals(1, actions!!.size)
    assertEquals("/groups/1/action", actions[0].address)
    assertEquals("PUT", actions[0].method)
    assertEquals(Collections.singletonMap("on", true), actions[0].body)
  }

  @Test
  fun testGetRawConfig() {
    val hue = createHueAndInitializeMockServer()
    val config = hue.raw!!.config
    assertEquals(11, config!!.zigbeeChannel)
    assertEquals("00:17:11:22:33:aa", config.mac)
    assertEquals(true, config.isDhcp)
    assertEquals("1.26.0", config.apiVersion)
    assertEquals("noupdates", config.softwareUpdate2!!.bridge!!.state)
    assertEquals(true, config.portalState!!.isSignedOn)
    assertEquals(false, config.portalState!!.isIncoming)
    assertEquals(true, config.portalState!!.isOutgoing)
    assertEquals("disconnected", config.portalState!!.communication)
    assertEquals("2015-01-09T19:19:19", config.whiteList!!["6655664454522131aaaeeaaeaeaeaea"]!!.createDate)
  }

  private fun readFile(fileName: String): String {
    val classLoader = javaClass.classLoader
    val file = File(classLoader.getResource(fileName)!!.file)
    try {
      return Files.lines(file.toPath()).collect(Collectors.joining())
    } catch (e: IOException) {
      throw RuntimeException(e)
    }

  }

  companion object {
    private val API_KEY = "abcd1234"
    private val API_BASE_PATH = "/api/$API_KEY/"
    private val MOTION_SENSOR_NAME = "Hallway sensor"
    private val TEMPERATURE_SENSOR_NAME = "Hue temperature sensor 1"
    private val DIMMER_SWITCH_NAME = "Living room door"
  }
}
