package believe;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.newdawn.slick.util.Log;


public class Main {

  private static final String PATH = "lib/native";

  public static void main(String[] args) {
    try {

      // IMPORTANT CODE FOR LOADING NATIVES!
      final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
      usrPathsField.setAccessible(true);

      //get array of paths
      final String[] paths = (String[])usrPathsField.get(null);

      //check if the path to add is already present
      for(String path : paths) {
        if(path.equals(PATH)) {
          return;
        }
      }

      //add the new path
      final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
      newPaths[newPaths.length-1] = PATH;
      Log.info("Resetting java.library.path to contain natives");
      usrPathsField.set(null, newPaths);

      BelieveGame b = new BelieveGame("Believe");
      b.setDebug(args.length > 0 && args[0].equals("debug"));
      b.run();
    }
    catch (Exception e) {
      Log.info(String.format("An error was caught and unfortunately, it was not graceful: %s", e.getMessage()));
      e.printStackTrace();
    }
  }
}
