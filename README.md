# Yet Another Kotlin Hue API

This is a Kotlin API for the Philips Hue lights.<sup>1</sup> It does not use the official 
Hue SDK but instead accesses the REST API of the Philips Hue Bridge directly.
This library has been confirmed to work with the Philips Hue Bridge API version 1.32.0.

The code is a fork of [ZeroOne3010/yetanotherhueapi](https://github.com/ZeroOne3010/yetanotherhueapi) with a to Kotlin conversation.

### The main advantages
 * Android Support - no `java.awt.Color` dependency
 * Plain Kotlin API without `Optionals`
 * Android API level 9 instead of 26 

## Usage

First, import the classes from this library:

[//]: # (imports)
```java
import io.github.zeroone3010.yahueapi.*;
```

Then, if you already have an API key for your Bridge:

[//]: # (init)
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String apiKey = "bn4z908...34jf03jokaf4"; // Fill in an API key to access your Bridge
final Hue hue = new Hue(bridgeIp, apiKey);
```

If you don't have an API key for your bridge:

[//]: # (throws-InterruptedException|java.util.concurrent.ExecutionException)
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String appName = "MyFirstHueApp"; // Fill in the name of your application
final java.util.concurrent.CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(bridgeIp).initializeApiConnection(appName);
// Push the button on your Hue Bridge to resolve the apiKey future:
final Hue hue = new Hue(bridgeIp, apiKey.get());
```

Using the rooms and the lights:

[//]: # (requires-init)
```java
// Get a room -- returns Optional.empty() if the room does not exist, but 
// let's assume we know for a fact it exists and can do the .get() right away:
final Room room = hue.getRoomByName("Basement").get();

// Turn the lights on, make them pink:
room.setState(State.builder().color(java.awt.Color.PINK).on());

// Make the entire room dimly lit:
room.setBrightness(10);

// Turn off that single lamp in the corner:
room.getLightByName("Corner").get().turnOff();

// Turn one of the lights green. This also demonstrates the proper use of Optionals:
final java.util.Optional<Light> light = room.getLightByName("Ceiling 1");
light.ifPresent(l -> l.setState(State.builder().color(java.awt.Color.GREEN).keepCurrentState()));
```

### Caching

By default this library always queries the Bridge every time you query the state of a light, a room, or a sensor.
When querying the states of several items in quick succession, it is better to use caching. You can turn it on
by calling the `setCaching(true)` method of the `Hue` object. Subsequent `getState()` calls well *not* trigger a
query to the Bridge. Instead they will return the state that was current when caching was toggled on, or the last time
that the `refresh()` method of the `Hue` object was called. Toggling caching off by calling `setCaching(false)`
will direct subsequent state queries to the Bridge again. Caching is off by default. When toggling caching on/off
there is no need to get the `Light`, `Room` or `Sensor` from the `Hue` object again: you can keep using the same
object reference all the time. Objects that return a cached state will accept and execute state changes (calls to 
the `setState` method) just fine, but they will *not* update their cached state with those calls.

## Including the library with Maven

Add the following dependency to your pom.xml file:

```xml
<dependency>
    <groupId>io.github.tobsef</groupId>
    <artifactId>yetanotherhueapi</artifactId>
    <version>1.2.0</version>
</dependency>
```

## Scope and philosophy

This library is not intended to have all the possible functionality of the SDK
or the REST API. Instead it is focusing on the essentials: querying and setting
the states of the rooms and the lights. And this library should do those 
essential functions well: in an intuitive and usable way for the programmer.
The number of external dependencies should be kept to a minimum.
Version numbering follows the [Semantic Versioning](https://semver.org/).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Version history

See [CHANGELOG.md](CHANGELOG.md).

## This project elsewhere
* [Black Duck Open Hub](https://www.openhub.net/p/yetanotherhueapi)
* [Code Climate](https://codeclimate.com/github/ZeroOne3010/yetanotherhueapi)

Notes
-----

<sup>1</sup> Java 8, while old already, was chosen because it is easy to 
install and run it on a Raspberry Pi computer. For the installation instructions,
see, for example, [this blog post](http://wp.brodzinski.net/raspberry-pi-3b/install-latest-java-8-raspbian/).
