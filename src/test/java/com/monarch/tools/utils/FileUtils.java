package com.monarch.tools.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monarch.tools.Constants;

public class FileUtils {

	public static File writeXmlDocumentToFile(String fileName, Document document) throws TransformerException {
		Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// enable indent on the xml file
		transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		// create xml content
		DOMSource source = new DOMSource(document);

		File xmlFile = new File(Constants.TEMP_RESOURCES_PATH + fileName);
		StreamResult fileStream = new StreamResult(xmlFile);

		transformer.transform(source, fileStream);

		logger.info("File saved!");
		return xmlFile;
	}

	/**
	 * Convert all class parameters to key,value map. Used to prepare xml model
	 * class to be written to properties file.
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertObjectDataToMap(Object object) {
		ObjectMapper m = new ObjectMapper();
		return m.convertValue(object, Map.class);
	}

	/**
	 * It will do a clean write to file (append = false)
	 * 
	 * @param fullFilePath
	 * @param content
	 */
	public static void writeToFile(String fullFilePath, String content) {
		writeToFile(fullFilePath, content, false);
	}

	public static void writeToFile(String fullFilePath, String content, boolean appendToFile) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(fullFilePath, appendToFile);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			out.println(content);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

	public static void deleteFileIfExistsInTemp(String tempFileName) {
		File tempFile = new File(Constants.TEMP_RESOURCES_PATH + tempFileName);
		if (tempFile.exists())
			tempFile.delete();
	}

	public static List<String> readFromFile(String fullFilePath) {
		Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
		File yourFile = new File(fullFilePath);

		List<String> records = new ArrayList<String>();
		try {
			yourFile.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(fullFilePath));
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line);
			}
			reader.close();
			return records;
		} catch (Exception e) {
			logger.error("Exception occurred trying to read '%s'.", fullFilePath);
			e.printStackTrace();
			return null;
		}
	}

}
