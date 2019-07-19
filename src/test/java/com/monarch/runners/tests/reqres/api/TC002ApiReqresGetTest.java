package com.monarch.runners.tests.reqres.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.symphony.jcurl.JCurl.HttpMethod;
import org.symphonyoss.symphony.jcurl.JCurl.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monarch.apps.reqres.ReqresUri;
import com.monarch.apps.reqres.user.post.RequestCreateUserModel;
import com.monarch.apps.reqres.user.post.UserPostCreateData;
import com.monarch.tools.connectors.JCurlConnector;
import com.monarch.tools.utils.ConfigUtils;
import com.monarch.tools.utils.StringUtils;

@RunWith(JUnit4.class)
public class TC002ApiReqresGetTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	//connector and data model classes for request creation
	private RequestCreateUserModel userData = UserPostCreateData.generateCreateUserData();
	private JCurlConnector httpConnector = new JCurlConnector();

	//test data
	private String url = "";
	private String body = "";
	
	//expected conditions
	private String expectedResponseMessage = "\"id\":";
	private int expectedResponseCode = 201;

	@Before
	public void setupData() throws JsonProcessingException  {
		logger.info("=================================================================");
		logger.info("================= " + this.getClass().getSimpleName());
		logger.info("=================================================================");
		
		//form the URL from the base config file + the URI prefix
		url = ConfigUtils.getProperty("BASE_URL_REQRES") + ReqresUri.POST_CREATE_USER_URI;

		logger.info("------ Test Data ----------------------");
		//generate test data values for the payload fields
		userData = UserPostCreateData.generateCreateUserData();
		logger.info("name: " + userData.name);
		logger.info("job: " + userData.job);

		//format the body data from model to string
		logger.info("------ Formatted Test Data ----------------------");
		body = StringUtils.modelToString(userData);
	}

	@Test
	public void tc002ApiReqresGetTest() {

		//perform the request against the target service/server
		Response responseObject = httpConnector.sendJsonDataHttpRequest(url, HttpMethod.POST, body);

		//print out the response from the server
		logger.info("Response Output: " + responseObject.getOutput());
		logger.info("Expected Response: " + expectedResponseMessage);

		//Check expected conditions
		Assert.assertTrue(
				"Expected message - '" + expectedResponseMessage + "' - Actual Message: " + responseObject.getOutput(),
				responseObject.getOutput().contains(expectedResponseMessage));

		Assert.assertTrue(
				"Expected status code - '" + expectedResponseCode + "' - Actual Status code: " + responseObject.getResponseCode(),
				responseObject.getResponseCode() == expectedResponseCode);
	}

}
