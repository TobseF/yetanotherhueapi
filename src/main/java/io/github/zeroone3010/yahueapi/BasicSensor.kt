package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

class BasicSensor implements Sensor {
  private static final Logger logger = Logger.getLogger("SensorImpl");
  private static final String UTC_SUFFIX = "+00:00[UTC]";

  protected final String id;
  protected final String name;
  protected final URL baseUrl;
  protected final SensorType type;
  private final Supplier<Map<String, Object>> stateProvider;

  BasicSensor(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    this.id = id;
    if (sensor == null) {
      throw new HueApiException("Sensor " + id + " cannot be found.");
    }
    this.name = sensor.getName();
    this.baseUrl = url;
    this.stateProvider = stateProvider;
    this.type = SensorType.parseTypeString(sensor.getType());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public SensorType getType() {
    return type;
  }

  @Override
  public ZonedDateTime getLastUpdated() {
    return ZonedDateTime.parse(readStateValue("lastupdated", String.class) + UTC_SUFFIX);
  }

  protected <T> T readStateValue(final String stateValueKey, final Class<T> type) {
    final Map<String, Object> state = stateProvider.get();
      logger.fine(state.toString());
      return type.cast(state.get(stateValueKey));
  }

  @Override
  public String toString() {
    return "Sensor{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }
}
