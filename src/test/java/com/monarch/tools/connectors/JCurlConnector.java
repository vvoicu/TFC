package com.monarch.tools.connectors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateParsingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.symphony.jcurl.JCurl;
import org.symphonyoss.symphony.jcurl.JCurl.Builder;

import com.monarch.tools.Constants;

public class JCurlConnector {
	private Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);

	/**
	 * Request model for Generic
	 * 
	 * @param url
	 * @param httpMethod
	 * @param body
	 * @param clientKeyStorePath
	 * @param clientKeyStorePassword
	 * @param clientKeyStoreType
	 * @return
	 */
	public JCurl.Response sendJsonDataHttpRequest(String url, JCurl.HttpMethod httpMethod, String body) {
		logger.info("----------------------------");
		logger.info("Body: " + body);
		logger.info("URL: " + url);
		JCurl jcurl = setJcurlConnection(httpMethod, body)
				.header("Content-Type", "application/json").data(body).build();
		return doJcurlConnection(jcurl, url);
	}
	

	/**
	 * Request model for Sony
	 * 
	 * @param url
	 * @param httpMethod
	 * @param body
	 * @param clientKeyStorePath
	 * @param clientKeyStorePassword
	 * @param clientKeyStoreType
	 * @return
	 */
	public JCurl.Response sendXmlDataHttpRequest(String url, JCurl.HttpMethod httpMethod, String body, String clientKeyStorePath,
			String clientKeyStorePassword, String clientKeyStoreType) {
		logger.info("----------------------------");
		logger.info("Body: " + body);
		logger.info("URL: " + url);
		JCurl jcurl = setJcurlConnection(httpMethod, body, clientKeyStorePath, clientKeyStorePassword, clientKeyStoreType)
				.header("Content-Type", "text/xml").data(body).build();
		return doJcurlConnection(jcurl, url);
	}
	
	private Builder setJcurlConnection(JCurl.HttpMethod httpMethod, String body) {
		logger.info("HttpMethod: " + httpMethod);
		logger.info("----------------------------");
		return JCurl.builder().method(httpMethod);
	}
	
	private Builder setJcurlConnection(JCurl.HttpMethod httpMethod, String body, String clientKeyStorePath,
			String clientKeyStorePassword, String clientKeyStoreType) {

		logger.info("HttpMethod: " + httpMethod);
		logger.info("KeyPath: " + clientKeyStorePath);
		logger.info("KeyType: " + clientKeyStoreType);
		logger.info("----------------------------");

		return JCurl.builder().method(httpMethod).insecure(true).keystore(clientKeyStorePath).storepass(clientKeyStorePassword)
				.storetype(clientKeyStoreType);
	}

	private JCurl.Response doJcurlConnection(JCurl jcurl, String url) {
		JCurl.Response response = null;
		try {
			HttpURLConnection connection = jcurl.connect(url);
			response = jcurl.processResponse(connection);
			logger.info("ResponseCode: " + connection.getResponseCode());
			logger.info("ResponseMessage: " + connection.getResponseMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateParsingException e) {
			e.printStackTrace();
		}
		return response;
	}
}
