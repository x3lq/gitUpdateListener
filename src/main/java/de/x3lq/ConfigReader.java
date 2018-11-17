package de.x3lq;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private static final String filePath = "config.properties";

	public static Properties readConfig() {
		Properties properties = new Properties();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try {
			InputStream resourceStream = loader.getResourceAsStream(filePath);
			properties.load(resourceStream);
			resourceStream.close();
			return properties;
		} catch (Exception e) {

			System.out.println("An error occured while loading the config");
			return null;
		}
	}
}
