package musicGame;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


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
			System.out.println("Resetting java.library.path to contain natives");
			usrPathsField.set(null, newPaths);
			
			BelieveGame b = new BelieveGame("Believe");
			
			b.run();
		}
		catch (Exception e) {
			System.out.println("Failed to add natives path to java.library.path: " + e.getMessage());
		}
	}
}
