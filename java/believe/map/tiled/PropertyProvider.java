package believe.map.tiled;

import java.util.Optional;

/** An object that provides property values when supplied with a corresponding key. */
public interface PropertyProvider {
  /** Returns the value of a property for {@code key}. */
  Optional<String> getProperty(String key);
}
