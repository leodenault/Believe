package believe.core.io;

import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * {@link ResourceLocation} implementation which allows reloading the contents of a file every time
 * it is requested.
 */
public final class ReloadableFileSystemLocation implements ResourceLocation {
  private static final String ROOT = ".";

  @Override
  public InputStream getResourceAsStream(String ref) {
    FileInputStream in = null;
    try {
      File file = new File(ROOT + ref);
      in = new FileInputStream(file);
    } catch (FileNotFoundException e) {
    }

    return in;
  }

  @Override
  public URL getResource(String ref) {
    URI uri = new File(ROOT + ref).toURI();
    try {
      return uri.toURL();
    } catch (MalformedURLException e) {
    }
    return null;
  }
}
