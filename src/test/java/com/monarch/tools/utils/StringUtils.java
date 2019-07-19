package com.monarch.tools.utils;

import java.io.File;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monarch.tools.Constants;

public class StringUtils {
	
	private static Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());


	/**
	 * Convert an Object class to String converting all fields and outputting a Json
	 * String.
	 * 
	 * @param dataObject
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String modelToString(Object dataObject) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(dataObject);
	}

	/**
	 * If the kill switch sequence is found in the request body then it will set the
	 * property to true. This property will be read from the vertx server and will
	 * kill the server based on this value.
	 * 
	 * @param clientRequestBody
	 */
	public static void checkForKillSwitch(String clientRequestBody) {
		logger.info("---- Checking for Kill Switch key ----");
		if (clientRequestBody.contains(Constants.VERTX_KILL_SWITCH))
			FileUtils.writeToFile(Constants.TEMP_RESOURCES_PATH + Constants.BROKER_KILL_FILE, "true");
	}

	/**
	 * Check if the file exists, if so then return true
	 * 
	 * @return
	 */
	public static boolean killSwitchExists() {
		File killSwitchFile = new File(Constants.TEMP_RESOURCES_PATH + Constants.BROKER_KILL_FILE);
		logger.info("--- Kill Switch READ --- is set to: " + killSwitchFile.exists());
		return killSwitchFile.exists();
	}

	/**
	 * Clean up for the start
	 */
	public static void cleanKillSwitch() {
		File killSwitchFile = new File(Constants.TEMP_RESOURCES_PATH + Constants.BROKER_KILL_FILE);
		logger.info("--- Kill Switch READ --- is set to: " + killSwitchFile.exists());
		if (killSwitchFile.exists())
			killSwitchFile.delete();
	}

}
