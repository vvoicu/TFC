package com.monarch.runners.tests.ecs.mail;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.monarch.tools.Constants;
import com.monarch.tools.connectors.mail.OutlookMailConnector;
import com.monarch.tools.connectors.mail.data.EmailDataModel;
import com.monarch.tools.utils.FileUtils;

import net.serenitybdd.junit.runners.SerenityRunner;

@RunWith(SerenityRunner.class)
public class TC004CheckEmailTest {

	private OutlookMailConnector emailsConnector;
	private List<EmailDataModel> sortedEmailList;
	private String sortingCriteria;

	@Before
	public void setupData() {

		emailsConnector = new OutlookMailConnector();

		// set credentials
//		emailsConnector.setUserCredentials(ConfigUtils.getProperty("EMAIL_ACCOUNT_USER"), ConfigUtils.getProperty("EMAIL_ACCOUNT_PASS"));

		// criteria for sorting emails - string is contained in email body
		sortingCriteria = "QA";

		System.out.println("------------" + emailsConnector.getSessionConfiguration().getPassword().isEmpty());
		Assert.assertTrue(
				"WARNING: SET PASSWORD in config file under resources. Expected Password not empty. Actual: " + emailsConnector.getSessionConfiguration().getPassword().isEmpty(),
				!emailsConnector.getSessionConfiguration().getPassword().isEmpty());
	}

	@Test
	public void tc004CheckEmailTest() throws IOException, MessagingException, InterruptedException {

		emailsConnector.createEmailConnection();
		List<EmailDataModel> emailsList = emailsConnector.readEmailsSinceYesterday();
		// List<EmailDataModel> emailsList = readEmails();
		emailsConnector.closeEmailConnections();

		sortedEmailList = emailsConnector.selectEmailsThatContainStringInBody(sortingCriteria, emailsList);
		// printEmailList(finalList);

		Assert.assertTrue("Sorted email list was empty. No extracted data! ", sortedEmailList.size() > 0);

	}

	@After
	public void persistToFile() {
		if (sortedEmailList != null)
			FileUtils.writeToFile(Constants.TC004_OUTPUT_FILE, sortedEmailList.toString(), true);
	}
}
