package io.github.zeroone3010.yahueapi

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.zeroone3010.yahueapi.domain.ApiInitializationStatus
import io.github.zeroone3010.yahueapi.domain.Group
import io.github.zeroone3010.yahueapi.domain.Root
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class Hue
/**
 * A basic constructor for initializing the Hue Bridge connection for this library.
 * Use the `hueBridgeConnectionBuilder` method if you do not have an API key yet.
 *
 * @param protocol The desired protocol for the Bridge connection. HTTP or UNVERIFIED_HTTPS,
 * as the certificate that the Bridge uses cannot be verified. Defaults to HTTP
 * when using the other constructor.
 * @param bridgeIp The IP address of the Hue Bridge.
 * @param apiKey   The API key of your application.
 * @since 1.0.0
 */
(protocol: HueBridgeProtocol, bridgeIp: String, apiKey: String) {

  private val objectMapper = ObjectMapper()

  private val sensorFactory = SensorFactory(this, objectMapper)
  private val roomFactory: RoomFactory

  private val uri: String
  private var root: Root? = null
  private var rooms: Map<String, Room>? = null
  private var zones: Map<String, Room>? = null
  private var sensors: Map<String, Sensor>? = null
  /**
   * Tells whether this instance caches the states of objects, such as lights.
   *
   * @return `true` if cached results are returned, `false` if all queries are directed to the Bridge.
   * @since 1.2.0
   */
  /**
   *
   * Controls whether cached states of objects, such as lights, should be used.
   * Off (`false`) by default. If set on (`true`), querying light states will
   * NOT actually relay the query to the Bridge. Instead, it uses the state that was valid
   * when this method was called, or the state that was valid when subsequent calls to
   * the [.refresh] method were made.
   *
   *
   * One should use caching when performing multiple queries in a quick succession,
   * such as when querying the states of all the individual lights in a Hue setup.
   *
   *
   * If caching is already on and you try to enable it again, this method does nothing.
   * Similarly nothing happens if caching is already disabled and one tries to disable it again.
   *
   * @param enabled Set to `true` to have the lights cache their results from the Bridge.
   * Remember to call [.refresh] first when you need to retrieve the
   * absolutely current states.
   * @since 1.2.0
   */
  var isCaching = false
    set(enabled) {
      if (isCaching != enabled) {
        field = enabled
        refresh()
      }
    }

  /**
   * Returns the raw root node information of the REST API. Not required for anything but querying the most
   * technical details of the Bridge setup. Note that it is not possible to change the state of the Bridge or
   * the lights by using any values returned by this method: the results are read-only.
   *
   * The results of this method are also always cached, so a call to this method never triggers a query to the Bridge
   * (unless no other queries have been made to the Bridge since this instance of `Hue` was constructed).
   * To refresh the cache call the [.refresh] method.
   *
   * @return A Root element, as received from the Bridge REST API. Always returns a cached version of the data.
   *
   * @since 1.0.0
   */
  val raw: Root?
    get() {
      doInitialDataLoadIfRequired()
      return this.root
    }

  /**
   * Returns all the sensors configured into the Bridge.
   *
   * @return A Collection of sensors.
   * @since 1.0.0
   */
  val unknownSensors: Collection<Sensor>
    get() = getSensorsByType(SensorType.UNKNOWN, Sensor::class.java)

  /**
   * Returns all the temperature sensors configured into the Bridge.
   *
   * @return A Collection of temperature sensors.
   * @since 1.0.0
   */
  val temperatureSensors: Collection<TemperatureSensor>
    get() = getSensorsByType(SensorType.TEMPERATURE, TemperatureSensor::class.java)

  /**
   * Returns all the dimmer switches configured into the Bridge.
   *
   * @return A Collection of dimmer switches.
   * @since 1.0.0
   */
  val dimmerSwitches: Collection<DimmerSwitch>
    get() = getSensorsByType(SensorType.DIMMER_SWITCH, DimmerSwitch::class.java)

  /**
   * Returns all the motion sensors configured into the Bridge.
   *
   * @return A Collection of motion sensors.
   * @since 1.0.0
   */
  val motionSensors: Collection<MotionSensor>
    get() = getSensorsByType(SensorType.MOTION, MotionSensor::class.java)

  /**
   * Returns all the daylight sensors configured into the Bridge.
   *
   * @return A Collection of daylight sensors.
   * @since 1.0.0
   */
  val daylightSensors: Collection<DaylightSensor>
    get() = getSensorsByType(SensorType.DAYLIGHT, DaylightSensor::class.java)

  /**
   * The basic constructor for initializing the Hue Bridge connection for this library.
   * Use the `hueBridgeConnectionBuilder` method if you do not have an API key yet.
   *
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   * @since 1.0.0
   */
  constructor(bridgeIp: String, apiKey: String) : this(HueBridgeProtocol.HTTP, bridgeIp, apiKey)

  init {
    this.uri = protocol.protocol + "://" + bridgeIp + "/api/" + apiKey + "/"
    if (HueBridgeProtocol.UNVERIFIED_HTTPS == protocol) {
      TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification()
    }
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    roomFactory = RoomFactory(this, objectMapper, uri)
  }

  private fun doInitialDataLoadIfRequired() {
    if (root == null) {
      refresh()
    }
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   *
   * This method is particularly useful if caching is enabled
   * with the [.setCaching] method. Calls to `refresh()`
   * will, in that case, refresh the states of the lights.
   *
   * @since 1.0.0
   */
  fun refresh() {
    try {
      root = objectMapper.readValue<Root>(URL(uri), Root::class.java)
    } catch (e: IOException) {
      throw HueApiException(e)
    }

    this.rooms = Collections.unmodifiableMap(findGroupsOfType(ROOM_TYPE_GROUP))
    this.zones = Collections.unmodifiableMap(findGroupsOfType(ZONE_TYPE_GROUP))

    this.sensors = root!!.sensors!!.entries.map { sensor -> buildSensor(sensor.key, root!!) }.map { it.id to it }.toMap()
  }

  private fun findGroupsOfType(groupType: String): Map<String, Room> {
    return root!!.groups!!.entries
        .filter { it.value.type == groupType }
        .map { group -> buildRoom(group.key, group.value, root!!) }.map { it.name!! to it }.toMap()
  }

  /**
   * Returns all the rooms configured into the Bridge.
   *
   * @return A Collection of rooms.
   * @since 1.0.0
   */
  fun getRooms(): Collection<Room> {
    doInitialDataLoadIfRequired()
    return Collections.unmodifiableCollection(this.rooms!!.values)
  }

  /**
   * Returns all the zones configured into the Bridge.
   *
   * @return A Collection of zones as Room objects.
   * @since 1.1.0
   */
  fun getZones(): Collection<Room> {
    doInitialDataLoadIfRequired()
    return Collections.unmodifiableCollection(this.zones!!.values)
  }

  /**
   * Returns a specific room by its name.
   *
   * @param roomName The name of a room
   * @return A room or `Optional.empty()` if a room with the given name does not exist.
   * @since 1.0.0
   */
  fun getRoomByName(roomName: String): Room? {
    doInitialDataLoadIfRequired()
    return this.rooms!![roomName]
  }

  /**
   * Returns a specific zone by its name.
   *
   * @param zoneName The name of a zone
   * @return A zone or `Optional.empty()` if a zone with the given name does not exist.
   * @since 1.1.0
   */
  fun getZoneByName(zoneName: String): Room? {
    doInitialDataLoadIfRequired()
    return this.zones!![zoneName]
  }

  private fun buildRoom(groupId: String, group: Group, root: Root): Room {
    return roomFactory.buildRoom(groupId, group, root)
  }

  private fun buildSensor(sensorId: String, root: Root): Sensor {
    return sensorFactory.buildSensor(sensorId, root.sensors!![sensorId], uri)
  }

  private fun <T> getSensorsByType(type: SensorType, sensorClass: Class<T>): Collection<T> {
    doInitialDataLoadIfRequired()
    return this.sensors!!.values.filter { s -> type == s.type }.map { sensorClass.cast(it) }.toList()
  }

  /**
   * Returns a specific temperature sensor by its name.
   *
   * @param sensorName The name of a sensor
   * @return A sensor or `Optional.empty()` if a sensor with the given name does not exist.
   * @since 1.0.0
   */
  fun getTemperatureSensorByName(sensorName: String): TemperatureSensor? {
    doInitialDataLoadIfRequired()
    return temperatureSensors.firstOrNull { sensor -> sensor.name == sensorName }
  }

  /**
   * Returns a specific motion sensor by its name.
   *
   * @param sensorName The name of a sensor
   * @return A sensor or `Optional.empty()` if a sensor with the given name does not exist.
   * @since 1.0.0
   */
  fun getMotionSensorByName(sensorName: String): MotionSensor? {
    doInitialDataLoadIfRequired()
    return motionSensors.firstOrNull { sensor -> sensor.name == sensorName }
  }

  /**
   * Returns a specific dimmer switch by its name.
   *
   * @param switchName The name of a switch
   * @return A dimmer switch or `Optional.empty()` if a dimmer switch with the given name does not exist.
   * @since 1.0.0
   */
  fun getDimmerSwitchByName(switchName: String): DimmerSwitch? {
    doInitialDataLoadIfRequired()
    return dimmerSwitches.firstOrNull { sensor -> sensor.name == switchName }
  }

  class HueBridgeConnectionBuilder constructor(private val bridgeIp: String) {

    /**
     * Returns a `CompletableFuture` that completes once you push the button on the Hue Bridge. Returns an API
     * key that you should use for any subsequent calls to the Bridge API.
     *
     * @param appName The name of your application.
     * @return A `CompletableFuture` with an API key for your application. You should store this key for future usage.
     * @since 1.0.0
     */
    fun initializeApiConnection(appName: String): CompletableFuture<String> {
      return CompletableFuture.supplyAsync { this.initializeApiConnectionDirect(appName) }
    }

    companion object {
      private val MAX_TRIES = 30
    }

    /**
     * Returns an API
     * key that you should use for any subsequent calls to the Bridge API.
     *
     * @param appName The name of your application.
     * @return an API key for your application. You should store this key for future usage.
     * @since 1.3.0
     * @see initializeApiConnection(appName: String)
     */
    fun initializeApiConnectionDirect(appName: String): String {

      val body = "{\"devicetype\":\"yetanotherhueapi#$appName\"}"
      val baseUrl: URL
      try {
        baseUrl = URL("http://$bridgeIp/api")
      } catch (e: MalformedURLException) {
        throw HueApiException(e)
      }

      var latestError: String? = null
      for (triesLeft in MAX_TRIES downTo 1) {
        try {
          println("Please push the button on the Hue Bridge now ($triesLeft seconds left).")

          val result = HttpUtil.post(baseUrl, "", body)
          println(result)
          val status = ObjectMapper().readValue<ArrayList<ApiInitializationStatus>>(result,
              object : TypeReference<ArrayList<ApiInitializationStatus>>() {

              })[0]
          status.success?.let {
            return it.username ?: throw HueApiException("ApiInitializationSuccess was empty")
          }


          latestError = status.error!!.description
          TimeUnit.SECONDS.sleep(1L)

        } catch (e: Exception) {
          throw HueApiException(e)
        }

      }
      throw HueApiException(latestError ?: "")
    }
  }


  companion object {
    private val ROOM_TYPE_GROUP = "Room"
    private val ZONE_TYPE_GROUP = "Zone"

    /**
     * The method to be used if you do not have an API key for your application yet.
     * Returns a `HueBridgeConnectionBuilder` that initializes the process of
     * adding a new application to the Bridge.
     *
     * @param bridgeIp The IP address of the Bridge.
     * @return A connection builder that initializes the application for the Bridge.
     * @since 1.0.0
     */
    fun hueBridgeConnectionBuilder(bridgeIp: String): HueBridgeConnectionBuilder {
      return HueBridgeConnectionBuilder(bridgeIp)
    }
  }
}
