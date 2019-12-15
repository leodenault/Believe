package believe.app;

import believe.util.Util;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Utilities used at the application level. */
public final class AppUtil {
  // Find a more dynamic way of setting the java.library.path variable if there's a need for it.
  private static final Set<String> NATIVE_PATHS =
      Util.hashSetOf("third_party/jinput", "third_party/lwjgl", "third_party/openal");

  public static void setNativesOnJavaLibraryPath()
      throws NoSuchFieldException, IllegalAccessException {
    final Field javaLibraryPathField = ClassLoader.class.getDeclaredField("usr_paths");
    javaLibraryPathField.setAccessible(true);
    final String[] javaLibraryPaths = (String[]) javaLibraryPathField.get(null);

    // Check if the path to add is already present
    Set<String> pathsToAdd = new HashSet<>(NATIVE_PATHS);
    for (String nativePath : NATIVE_PATHS) {
      for (String javaLibraryPath : javaLibraryPaths) {
        if (javaLibraryPath.equals(nativePath)) {
          pathsToAdd.remove(nativePath);
          break;
        }
      }
    }

    ArrayList<String> fullPathsList = new ArrayList<>(Arrays.asList(javaLibraryPaths));
    fullPathsList.addAll(pathsToAdd);
    Log.info("Resetting java.library.path to contain natives.");
    javaLibraryPathField.set(null, fullPathsList.toArray(new String[0]));
  }

  public static void startApplication(
      String[] commandLineArguments, ApplicationComponent applicationComponent)
      throws NoSuchFieldException, IllegalAccessException, SlickException {
    setNativesOnJavaLibraryPath();
    AppGameContainerSupplier appGameContainerSupplier =
        applicationComponent.appGameContainerSupplier();
    appGameContainerSupplier.initAppGameContainer(
        commandLineArguments, applicationComponent.game());
    appGameContainerSupplier.get().start();
  }
}
