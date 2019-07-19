package com.monarch.tools.connectors.sftp;

import java.io.File;
import java.util.Vector;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.monarch.tools.Constants;
import com.monarch.tools.utils.ConfigUtils;

public class JschSftpConnection {
//	private static Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
	private static String SFTPWORKINGDIR = "/inbound/inbound";
	private static String PARAMETER_PREFIX = "SFTP_";

	private static Channel channel = null;
	private static ChannelSftp channelSftp = null;
	private static Session session = null;

	// props initialized in setConnectionParameters()
	private static File mysshFile;
	private static String SFTPHOST;
	private static String SFTPUSER;
	private static String SFTPPORT;

	public static void main(String[] args) {
		setConnectionParameters("MINIO");
		try {
			System.out.println(getWorkingDirFileList());
		} catch (SftpException | JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendFileToUnifySftp(String localFilePath, String remoteFileName) throws JSchException, SftpException {
		setConnectionParameters("UNIFY");
		sendFileToSftp(localFilePath, remoteFileName);
	}

	public static String getUnifyWorkingDirFileList() throws JSchException, SftpException {
		setConnectionParameters("UNIFY");
		return getWorkingDirFileList();
	}

	public static void sendFileToStxSftp(String localFilePath, String remoteFileName) throws JSchException, SftpException {
		setConnectionParameters("STX");
		sendFileToSftp(localFilePath, remoteFileName);
	}

	public static String getStxWorkingDirFileList() throws JSchException, SftpException {
		setConnectionParameters("STX");
		return getWorkingDirFileList();
	}

	// ------- PRIVATE METHODS -------

	private static void sendFileToSftp(String localFilePath, String remoteFileName) throws JSchException, SftpException {
		Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
		createSftpConnection(SFTPHOST, SFTPPORT, SFTPUSER, mysshFile, SFTPPORT);
		channelSftp.cd(SFTPWORKINGDIR);
		logger.info("NOTE: Changed working directory...");
		channelSftp.put(localFilePath, remoteFileName);
		logger.info("NOTE: File Uploaded with remote name: " + remoteFileName);
		closeConnections();
	}

	@SuppressWarnings("rawtypes")
	private static String getWorkingDirFileList() throws SftpException, JSchException {
		Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
		String results = "";
		createSftpConnection(SFTPHOST, SFTPPORT, SFTPUSER, mysshFile, SFTPPORT);
		Vector filesList = channelSftp.ls(SFTPWORKINGDIR);
		logger.debug("NOTE: Extracted file list and parsing...");
		for (int i = 0; i < filesList.size(); i++) {
			results += filesList.get(i).toString().substring(55) + "\n";
		}
		results = results.replace(" ..\n", "");
		closeConnections();
		return results;
	}

	/**
	 * Initialize all connection parameters. Use the config file property name. <br>
	 * Ex. for property SFTP_STX_* you will provide patternPrefix="STX"
	 * 
	 * @param patternPrefix
	 */
	private static void setConnectionParameters(String patternPrefix) {
		mysshFile = new File(ConfigUtils.getProperty(PARAMETER_PREFIX + patternPrefix + "_SECRET_PATH"));
		SFTPHOST = ConfigUtils.getProperty(PARAMETER_PREFIX + patternPrefix + "_SERVER");
		SFTPUSER = ConfigUtils.getProperty(PARAMETER_PREFIX + patternPrefix + "_USER");
		SFTPPORT = ConfigUtils.getProperty(PARAMETER_PREFIX + patternPrefix + "_PORT");
		SFTPWORKINGDIR = ConfigUtils.getProperty(PARAMETER_PREFIX + patternPrefix + "_WORKINGDIR");
	}

	/**
	 * Close all connections and sessions used for the SFTP connection
	 */
	private static void closeConnections() {
		Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
		logger.debug("NOTE: SFTP connection closed.");
		if (channelSftp != null) {
			channelSftp.disconnect();
			channelSftp.exit();
		}
		if (channel != null)
			channel.disconnect();
		if (session != null)
			session.disconnect();
	}

	/**
	 * Create a SFTP connection with details from config properties
	 * 
	 * @throws JSchException
	 */
	private static void createSftpConnection(String SFTPHOST, String SFTPPORT, String SFTPUSER, File mysshFile, String SFTPWORKINGDIR) throws JSchException {
		Logger logger = LoggerFactory.getLogger(Constants.REPORT_TEST_NAME);
		JSch jSch = new JSch();
		Assert.assertTrue("Invalid key for connection to sftp. Please provide a valid private key. Current key: " + mysshFile.getAbsolutePath(),
				!mysshFile.getAbsolutePath().contains(".key"));
		jSch.addIdentity(mysshFile.getAbsolutePath());

		logger.info("NOTE: Private Key Added From Location: " + mysshFile.getAbsolutePath());
		session = jSch.getSession(SFTPUSER, SFTPHOST, Integer.valueOf(SFTPPORT));

		logger.info("session created.");
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		channel = session.openChannel("sftp");
		channel.connect();
		logger.debug("NOTE: Shell channel connected...");
		channelSftp = (ChannelSftp) channel;

		Assert.assertTrue("Could not create a connection to the SFTP server: " + SFTPHOST, channel.isConnected());
	}

}
