package believe.core.io;

import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

import java.io.InputStream;
import java.net.URL;

/**
 * Implementation that doesn't use the class loader to load resources
 */
public class JarClasspathLocation implements ResourceLocation {
  /**
   * @see org.newdawn.slick.util.ResourceLocation#getResource(java.lang.String)
   */
  @Override
  public URL getResource(String ref) {
    String cpRef = ref.replace('\\', '/');
    return ResourceLoader.class.getResource(cpRef);
  }

  /**
   * @see org.newdawn.slick.util.ResourceLocation#getResourceAsStream(java.lang.String)
   */
  @Override
  public InputStream getResourceAsStream(String ref) {
    String cpRef = ref.replace('\\', '/');
    return ResourceLoader.class.getResourceAsStream(cpRef);
  }
}
