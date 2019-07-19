package com.monarch.runners.tests.minio.sftp;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import com.monarch.tools.Constants;
import com.monarch.tools.FieldGenerators;
import com.monarch.tools.FieldGenerators.Mode;
import com.monarch.tools.connectors.sftp.MinioSftpConnector;

import io.minio.errors.MinioException;

public class TC005UploadFileToSftpTest {

	private MinioSftpConnector sftpConnector;
	// test flow
	private static String bucketName;
	private static String fileName;
	private static String filePath;

	@Before
	public void dataSetup() {
		sftpConnector = new MinioSftpConnector();

		bucketName = "pictures";
		fileName = FieldGenerators.generateRandomString(10, Mode.ALPHANUMERIC);
		filePath = Constants.CONFIG_RESOURCES_PATH + "local-config.properties";

	}

	@Test
	public void tc005UploadFileToSftpTest() throws MinioException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, IOException {

		sftpConnector.connectMinio();

		sftpConnector.checkFolderPresence(bucketName);

		boolean isSuccess = sftpConnector.uploadFile(bucketName, fileName, filePath);
		System.out.println(isSuccess + " is successfully uploaded.");

//		System.out.println("-- " + sftpConnector.isFilePresentInBucket(bucketName, fileName));
//		System.out.println("-- " + sftpConnector.isFilePresentInBucket(fileName, fileName+"2"));
//		System.out.println("-- " + sftpConnector.isFilePresentInBucket(bucketName, "loc"));

		System.out.println(sftpConnector.getWorkdirFiles(bucketName));

		Assert.assertTrue("Expected Bucket: " + fileName + " - Actual List: " + sftpConnector.getWorkdirFiles(bucketName),
				sftpConnector.getWorkdirFiles(bucketName).contains(fileName));

	}
}
