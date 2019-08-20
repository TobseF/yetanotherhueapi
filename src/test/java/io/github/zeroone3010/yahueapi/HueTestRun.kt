package io.github.zeroone3010.yahueapi

object HueTestRun {
  /**
   * Copies state from a light in one room to another light in another (or the same) room.
   *
   * @param args IP address of the Bridge, API key, fromRoom, fromLight, toRoom, toLight
   */
  @JvmStatic
  fun main(args: Array<String>) {
    val ip = args[0]
    val apiKey = args[1]
    val fromRoom = args[2]
    val fromLight = args[3]
    val toRoom = args[4]
    val toLight = args[5]

    val hue = Hue(HueBridgeProtocol.UNVERIFIED_HTTPS, ip, apiKey)

    val state = hue.getRoomByName(fromRoom)!!.getLightByName(fromLight)!!.state
    hue.getRoomByName(toRoom)!!.getLightByName(toLight)!!.state = state


    println("Zones:")
    hue.getZones().forEach { z ->
      println(String.format("Zone '%s' has these lights: %s",
          z.name, z.lights))
    }

    println("Motion sensors:")
    hue.motionSensors.forEach { s ->
      println(String.format("%s (%s): Presence %s",
          s.name, s.id, s.isPresence))
    }

    println("Temperature sensors:")
    hue.temperatureSensors.forEach { s ->
      println(String.format("%s (%s): %s °C",
          s.name, s.id, s.degreesCelsius))
    }

    println("Daylight sensors:")
    hue.daylightSensors.forEach { s ->
      println(String.format("%s (%s): Is daylight: %s",
          s.name, s.id, s.isDaylightTime))
    }

    println("Unknown sensors:")
    hue.unknownSensors.forEach { println(it) }
  }
}
