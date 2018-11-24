package believe.util;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

public class Util {

  private static final String DEFAULT_DIRECTORY = "customFlowFiles";
  private static final String FILE_EXTENSION = ".lfl";
  private static final String NATIVE_PATH = "lib/lwjgl";

  public static File[] getFlowFiles() throws SecurityException {
    File parent = new File(DEFAULT_DIRECTORY);
    return parent.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(FILE_EXTENSION);
      }
    });
  }

  /**
   * Clips to the child clip in the context of the graphics object's current
   * clip. The result is the intersection of the two clips.
   *
   * @param g The graphics object from which to fetch the current clip.
   * @param childClip The new clipping area to clip to.
   * @return The clipping context used before executing this operation.
   */
  public static Rectangle changeClipContext(Graphics g, believe.geometry.Rectangle childClip) {
    Rectangle gClip = g.getClip();
    Rectangle oldClip = null;
    Rectangle newRect = null;
    if (gClip == null) {
      newRect = childClip;
    } else {
      oldClip = new Rectangle(gClip.getX(), gClip.getY(), gClip.getWidth(), gClip.getHeight());
      newRect = childClip.intersection(oldClip);
    }
    g.setClip(newRect);

    return oldClip;
  }

  public static void resetClipContext(Graphics g, Rectangle oldClip) {
    g.setClip(oldClip);
  }

  @SafeVarargs
  public static <T> Set<T> hashSetOf(T... elements) {
    HashSet<T> set = new HashSet<>();
    for (T element : elements) {
      set.add(element);
    }
    return set;
  }

  public static <T> Set<T> immutableSetOf(T... elements) {
    return Collections.unmodifiableSet(hashSetOf(elements));
  }

  @SafeVarargs
  public static <K, V> HashMap<K, V> hashMapOf(MapEntry<K, V>... entries) {
    HashMap<K, V> map = new HashMap<>();
    for (MapEntry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }

  @SafeVarargs
  public static <K, V> Map<K, V> immutableMapOf(MapEntry<K, V>... entries) {
    return Collections.unmodifiableMap(hashMapOf(entries));
  }

  public static void setNativePath() throws NoSuchFieldException, IllegalAccessException {
    // IMPORTANT CODE FOR LOADING NATIVES!
    final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
    usrPathsField.setAccessible(true);

    //get array of paths
    final String[] paths = (String[])usrPathsField.get(null);

    //check if the path to add is already present
    boolean pathExists = false;
    for(String path : paths) {
      if(path.equals(NATIVE_PATH)) {
        pathExists = true;
        break;
      }
    }

    if (!pathExists) {
      //add the new path
      final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
      newPaths[newPaths.length - 1] = NATIVE_PATH;
      Log.info("Resetting java.library.path to contain natives");
      usrPathsField.set(null, newPaths);
    }
  }
}
