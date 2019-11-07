package believe.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Optional;

/** Supplies an {@link Image} when prompted. */
public interface ImageSupplier {
  ImageSupplier DEFAULT = new ImageSupplier() {};

  /** Returns an image found at {@code fileLocation}. */
  default Optional<Image> get(String fileLocation) {
    try {
      return Optional.of(new Image(fileLocation));
    } catch (SlickException e) {
      return Optional.empty();
    }
  }
}
