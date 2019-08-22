package believe.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Optional;

/** Supplies an {@link Image} when prompted. */
public interface ImageSupplier {
  /** Returns an image found at {@code fileLocation}. */
  Optional<Image> get(String fileLocation);
}
