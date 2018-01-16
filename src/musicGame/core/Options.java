package musicGame.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.newdawn.slick.util.Log;

public class Options {

  private static final String CONFIG_FILE = "config.prop";
  private static final int DEFAULT_FLOW_SPEED = 4;

  private static Options INSTANCE;

  public int flowSpeed;

  private Options() {
    flowSpeed = DEFAULT_FLOW_SPEED;
  }

  public static Options getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Options();
      INSTANCE.load();
    }

    return INSTANCE;
  }

  private void load() {
      try {
        Properties p = new Properties();
        InputStream s = new FileInputStream(CONFIG_FILE);
        p.load(s);
        flowSpeed = Integer.parseInt(p.getProperty("flowSpeed"));
      } catch (FileNotFoundException e) {
        Log.warn(String.format("Missing %s file. Creating new one.", CONFIG_FILE));
        save();
      } catch (NumberFormatException e) {
        Log.warn(String.format("Incorrectly formatted %s file. Using defaults.", CONFIG_FILE));
      } catch (IOException e) {
        Log.warn(String.format("Problem reading %s file. Using defaults.", CONFIG_FILE));
      }
  }

  public void save() {
    Properties p = new Properties();

    try {
      OutputStream o = new FileOutputStream(CONFIG_FILE);
      p.setProperty("flowSpeed", String.valueOf(flowSpeed));
      p.store(o, null);
    } catch (IOException e) {
      Log.warn(String.format("Could not access or create %s file. Save operation aborted.", CONFIG_FILE));
    }
  }
}
