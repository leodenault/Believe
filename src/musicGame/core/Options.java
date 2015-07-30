package musicGame.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Options {
	
	private static final String CONFIG_FILE = "config.prop";
	private static final int DEFAULT_FLOW_SPEED = 4;
	
	private static Options INSTANCE;
	
	private Logger logger;
	
	public int flowSpeed;
	
	private Options() {
		flowSpeed = DEFAULT_FLOW_SPEED;
		logger = Logger.getLogger("believe");
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
				logger.log(Level.WARNING, String.format("Missing %s file. Creating new one.", CONFIG_FILE));
				save();
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, String.format("Incorrectly formatted %s file. Using defaults.", CONFIG_FILE));
			} catch (IOException e) {
				logger.log(Level.WARNING, String.format("Problem reading %s file. Using defaults.", CONFIG_FILE));
			}
	}
	
	public void save() {
		Properties p = new Properties();
		
		try {
			OutputStream o = new FileOutputStream(CONFIG_FILE);
			p.setProperty("flowSpeed", String.valueOf(flowSpeed));
			p.store(o, null);
		} catch (IOException e) {
			logger.log(Level.WARNING, String.format("Could not access or create %s file. Save operation aborted.", CONFIG_FILE));
		}
	}
}
