package musicGame.core;

import java.io.File;
import java.io.FileFilter;

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
}
