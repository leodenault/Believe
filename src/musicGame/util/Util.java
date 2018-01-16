package musicGame.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Util {

  private static final String DEFAULT_DIRECTORY = "customFlowFiles";
  private static final String FILE_EXTENSION = ".lfl";

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
  public static Rectangle changeClipContext(Graphics g, musicGame.geometry.Rectangle childClip) {
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
}
