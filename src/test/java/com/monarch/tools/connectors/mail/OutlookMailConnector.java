package com.monarch.tools.connectors.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarch.tools.Constants;
import com.monarch.tools.connectors.mail.data.EmailDataModel;
import com.monarch.tools.connectors.mail.data.OutlookConnectorSession;
import com.monarch.tools.utils.ConfigUtils;

public class OutlookMailConnector {
	private Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);

	private Store store = null;
	private Folder inbox = null;
	private OutlookConnectorSession sessionConfiguration = new OutlookConnectorSession();

	/**
	 * Set Connection properties, like username password, server address, storeType,
	 * folder to read from. All data is read from the config files located in the src/test/resources/configs/.
	 */
	public OutlookMailConnector() {

		sessionConfiguration.setUsername(ConfigUtils.getProperty("EMAIL_ACCOUNT_USER"));
		sessionConfiguration.setPassword(ConfigUtils.getProperty("EMAIL_ACCOUNT_PASS"));

		// server details
		sessionConfiguration.setHost(ConfigUtils.getProperty("EMAIL_SERVER_HOST"));
		sessionConfiguration.setPort(ConfigUtils.getProperty("EMAIL_SERVER_PORT"));
		sessionConfiguration.setStoreType(ConfigUtils.getProperty("EMAIL_STORE_TYPE"));

		// mail configurations
		sessionConfiguration.setReadFolder(ConfigUtils.getProperty("EMAIL_READ_FOLDER"));
		logger.info(sessionConfiguration.toString());

	}
	
	
	public OutlookConnectorSession getSessionConfiguration() {
		return this.sessionConfiguration;
	}

	/**
	 * You may overwrite the username and password for the email login via this method.
	 * @param username
	 * @param password
	 */
	public void setUserCredentials(String username, String password) {
		sessionConfiguration.setUsername(username);
		sessionConfiguration.setPassword(password);
	}

	/**
	 * Will read and extract a list of emails and their respective contents. Contents of each email are stored under the {@link EmailDataModel}.
	 * 
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
	@SuppressWarnings("unchecked")
	public List<EmailDataModel> readEmails() throws IOException, MessagingException {
		List<EmailDataModel> results = new ArrayList<EmailDataModel>();

		logger.info("Read Emails...");
		if (inbox != null) {
			Message[] messages = inbox.getMessages();

			for (int i = 0; i < messages.length; i++) {
				// save emails from today to a list
				EmailDataModel dataNow = new EmailDataModel();
				dataNow.subject = messages[i].getSubject();
				dataNow.messageContent = getTextFromMessage(messages[i]);
				dataNow.receivedDate = messages[i].getReceivedDate();
				dataNow.headers = Collections.list(messages[i].getAllHeaders());
				results.add(dataNow);
			}
		} else {
			logger.error("ERROR: Inbox is null. Make sure you initialize the connection! Call createEmailConnection()");
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public List<EmailDataModel> readEmailsSinceYesterday() throws IOException, MessagingException {
		List<EmailDataModel> results = new ArrayList<EmailDataModel>();

		Date dateToday = new Date();
		Date daysAgo = new DateTime(dateToday).minusDays(1).toDate();
		logger.info("Read Emails...");
		if (inbox != null) {
			Message[] messages = inbox.getMessages();

			logger.info("0 Date: " + messages[0].getReceivedDate());
			logger.info("LAST Date: " + messages[messages.length - 1].getReceivedDate());

			for (int i = messages.length - 1; i >= 0; i--) {
//			for (int i = 0; i < messages.length; i++) {
				logger.info("MsgNr: " + i);
				logger.info("Rec Date: " + messages[i].getReceivedDate());
				logger.info("Before Date: " + daysAgo);
				if (messages[i].getReceivedDate().after(daysAgo)) {
					// save emails from today to a list
					EmailDataModel dataNow = new EmailDataModel();
					dataNow.subject = messages[i].getSubject();
					dataNow.messageContent = getTextFromMessage(messages[i]);
					dataNow.receivedDate = messages[i].getReceivedDate();
					dataNow.headers = Collections.list(messages[i].getAllHeaders());
					results.add(dataNow);
				} else {
					break;
				}
			}
		} else {
			System.err.println("ERROR: Inbox is null. Make sure you initialize the connection! Call createEmailConnection()");
		}

		return results;
	}

	public Folder createEmailConnection() throws MessagingException, InterruptedException {
		logger.info("Connecting to Outlook365...");
		Properties props = new Properties();
		props.put("mail.smtp.host", sessionConfiguration.getHost());
		props.put("mail.smtp.port", sessionConfiguration.getPort());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sessionConfiguration.getUsername(), sessionConfiguration.getPassword());
			}
		});

		boolean isSuccess = false;
		int retries = 0;
		do {
			try {
				logger.info("Session Created...");
				store = session.getStore(sessionConfiguration.getStoreType());
				store.connect(sessionConfiguration.getHost(), null, null);

				logger.info("Store Created...");
				inbox = store.getFolder(sessionConfiguration.getReadFolder());
				if (inbox == null) {
					logger.info("No " + sessionConfiguration.getReadFolder());
					System.exit(1);
				}
				inbox.open(Folder.READ_ONLY);

				logger.info("Mail folder grabbed...");
				isSuccess = true;
			} catch (Exception e) {
				isSuccess = false;
			}
			if (!isSuccess) {
				logger.info(retries + " retries. Mail Connection Error, will retry in (ms) " + Constants.TEST_RETRY_WAIT_TIME);
				Thread.sleep(Constants.TEST_RETRY_WAIT_TIME);
			}

			retries++;

		} while (!isSuccess && retries < Constants.EMAIL_MAX_RETRY);

		return inbox;
	}

	public void closeEmailConnections() throws MessagingException {
		if (inbox != null)
			inbox.close(false);
		if (store != null)
			store.close();
	}

	@SuppressWarnings("unused")
	private List<EmailDataModel> selectEmailsSinceYesterday(List<EmailDataModel> fullEmailList) {
		List<EmailDataModel> results = new ArrayList<EmailDataModel>();

		Date dateToday = new Date();
		Date daysAgo = new DateTime(dateToday).minusDays(1).toDate();
		for (EmailDataModel emailDataModel : fullEmailList) {
			if (emailDataModel.receivedDate.after(daysAgo)) {
				results.add(emailDataModel);
			}
		}

		return results;
	}

	@SuppressWarnings("unused")
	public List<EmailDataModel> selectEmailsThatContainStringInBody(String searchString, List<EmailDataModel> fullEmailList) {
		List<EmailDataModel> results = new ArrayList<EmailDataModel>();

		for (EmailDataModel emailDataModel : fullEmailList) {
			logger.info("---- Subject: " + emailDataModel.subject);
			logger.info("---- Header:  " + emailDataModel.getHeaders());
			if (emailDataModel.messageContent.contains(searchString)) {
				results.add(emailDataModel);
			}
		}

		return results;
	}

	private String getTextFromMessage(Message message) throws MessagingException, IOException {
		String result = "";

		// logger.info("Mime: " + message.getContentType());
		if (message.isMimeType("text/plain")) {
			result = message.getContent().toString();
		} else if (message.isMimeType("text/html")) {
			String html = (String) message.getContent();
			result += "\n" + org.jsoup.Jsoup.parse(html).text();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			result += getTextFromMimeMultipart(mimeMultipart);
		}
		return result;
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result += "\n" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result += "\n" + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				// bodyPart.writeTo(System.out);
				result += getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}
}
