package com.monarch.tools.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.monarch.tools.Constants;

public class ConfigUtils {

	private static Properties prop = new Properties();
	private static InputStream input = null;

	public static void main(String[] args) {
		System.out.println(getProperty("BASE_URL_UI_WIKI"));
	}

	public static String getProperty(String propertyKey) {
		return getConfigData(propertyKey, "configFile", "-config.properties");
	}

	// local-dictionary.properties
	public static String getDictionary(String propertyKey) {
		return getConfigData(propertyKey, "dictionaryFile", "-dictionary.properties");
	}

	private static String getConfigData(String propertyKey, String systemPropertyName, String propertySuffix) {
		String result = "";
		String configFile = System.getProperty(systemPropertyName) == null ? "local"
				: System.getProperty(systemPropertyName);
		try {
//			System.out.println(Constants.CONFIG_RESOURCES_PATH + configFile + "-config.properties");
			input = new FileInputStream(Constants.CONFIG_RESOURCES_PATH + configFile + propertySuffix);
			prop.load(input);
			result = prop.getProperty(propertyKey);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
