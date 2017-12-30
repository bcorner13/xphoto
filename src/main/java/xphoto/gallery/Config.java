package xphoto.gallery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private static Properties mainProperties = new Properties();
	static {
		try {
			String path = "./xphoto.properties";
			FileInputStream file = new FileInputStream(path);
			mainProperties.load(file);
			file.close();
		} catch (IOException e) {
			System.err.println("Fatal, could not load xphoto.properties");
			System.exit(1);
		}
	}
	
	public static String getPhotoFolder() {
		return mainProperties.getProperty("photos");
	}

	public static boolean getParseMetaTags() {
		return mainProperties.getProperty("parsetags", "false").equals("true");
	}

	public static int getServerPort() {
		return Integer.parseInt(mainProperties.getProperty("server.port", "8080"));
	}
}
