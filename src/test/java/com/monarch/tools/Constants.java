package com.monarch.tools;

import java.io.File;

public class Constants {

	// Path Constants
	public static String CONFIG_RESOURCES_PATH = "src" + File.separator + "test" + File.separator + "resources"
			+ File.separator + "configs" + File.separator;
	public static String TEMP_RESOURCES_PATH = "target" + File.separator;
	// API
	public static long TEST_RETRY_WAIT_TIME = 60000;
	public static int EMAIL_MAX_RETRY = 5;
	public static String TC004_OUTPUT_FILE = TEMP_RESOURCES_PATH + "TC004FilteredEmails.txt";
	// REPORT FILE
	public static String REPORT_TEST_NAME = "ROOT";

	// ===== Server VertxExposed Endpoint ====
	public static String BROKER_MESSAGE_ENDPOINT = "/broker/message";
	public static CharSequence VERTX_KILL_SWITCH = "KSWK990992";

	public static int ERROR_CODE_INTERNAL = 422;

	// BROKER (SELF) CONFIGS
	public static String BROKER_SERVER_PORT_KEY = "http.port";
	public static String BROKER_KILL_FILE = "KillVertxServer.txt";
	public static String BROKER_SERVER_HOST = "127.0.0.1";
	public static int BROKER_SERVER_PORT = 8008;
	// ==================================
}
