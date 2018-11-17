package de.x3lq;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	public static Properties readConfig(String filePath) {
		Properties properties = new Properties();

		try {
			InputStream resourceStream = new FileInputStream(filePath);
			properties.load(resourceStream);
			resourceStream.close();
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
