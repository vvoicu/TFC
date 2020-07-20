package com.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import com.monarch.runners.tests.FieldGenerateDataTest;

import net.serenitybdd.core.SerenitySystemProperties;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.reports.html.HtmlAggregateStoryReporter;

public class SmokeTestMain {
	public static void main(String args[]) throws IOException, InitializationError {
		String chromeDriverPath = null;
		if (args == null || args.length != 1) {
			System.out.println("The path to chrome-driver was not given!");
			System.out.println(
					"Using defaults: the chrome-driver is in the same folder as the jar; Windos: chromedriver.exe; Linux: chromedriverlinux");
			if (SystemUtils.IS_OS_WINDOWS) {
				chromeDriverPath = "chromedriver.exe";
			} else if (SystemUtils.IS_OS_LINUX) {
				chromeDriverPath = "chromedriverlinux";
			} else {
				throw new IllegalArgumentException("Your os has no defaults! Please give the PATH to chrome-driver!");
			}
		} else {
			chromeDriverPath = args[0];
		}
//		loadProperties();
		JUnitCore junit = new JUnitCore();
		SerenitySystemProperties sp = new SerenitySystemProperties();
		sp.setValue(ThucydidesSystemProperty.DRIVER, "chrome");
		sp.setValue(ThucydidesSystemProperty.WEBDRIVER_CHROME_DRIVER, chromeDriverPath);
		@SuppressWarnings("unused")
		Result result = junit.run(JUnit4.class, FieldGenerateDataTest.class);
//		Result result = junit.run(Suite.class, RegressionSuite.class);
		HtmlAggregateStoryReporter tfcReports = new HtmlAggregateStoryReporter("TFC-aggregator");
		
		File resultFolder = new File("./target/site/serenity");
		resultFolder.mkdirs();
		
		tfcReports.setOutputDirectory(resultFolder);
		tfcReports.generateReportsForTestResultsFrom(resultFolder);
		
//		System.out.println("---- Results ----");
//		System.out.println(result.toString());
		System.exit(0);
	}

//	private static void loadProperties() {
////to load application's properties, we use this class
//		Properties mainProperties = new Properties();
////the base folder is ./, the root of the main.properties file
//		String path = "./dab.properties";
////load the file handle for main.properties
//		try (FileInputStream file = new FileInputStream(path)) {
////load all the properties from this file
//			mainProperties.load(file);
//		} catch (IOException e) {
//			System.out.println("Could not parse file:" + path);
//		}
//	}
}