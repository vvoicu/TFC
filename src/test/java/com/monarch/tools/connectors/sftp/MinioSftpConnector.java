package com.monarch.tools.connectors.sftp;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.xmlpull.v1.XmlPullParserException;

import com.monarch.tools.utils.ConfigUtils;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.messages.Item;

public class MinioSftpConnector {

	private MinioClient minioClient;
	// Authentication
	private static String URL;
	private static String PORT;
	private static String ACCESS;
	private static String SECRET;

	public void connectMinio() throws InvalidEndpointException, InvalidPortException {
		minioClient = new MinioClient(URL + ":" + PORT, ACCESS, SECRET);
	}

	public MinioSftpConnector() {
		URL = ConfigUtils.getProperty("SFTP_MINIO_SERVER");
		PORT = ConfigUtils.getProperty("SFTP_MINIO_PORT");
		ACCESS = ConfigUtils.getProperty("SFTP_MINIO_USER");
		SECRET = ConfigUtils.getProperty("SFTP_MINIO_SECRET");
	}

	public void checkFolderPresence(String bucketName) throws InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException,
			NoResponseException, ErrorResponseException, InternalException, IOException, XmlPullParserException, RegionConflictException {
		// Check if the bucket already exists.
		boolean isExist = minioClient.bucketExists(bucketName);
		if (isExist) {
			System.out.println("Bucket already exists.");
		} else {
			// Make a new bucket called asiatrip to hold a zip file of photos.
			minioClient.makeBucket(bucketName);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean uploadFile(String bucketName, String fileName, String filePath) throws InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
			NoResponseException, ErrorResponseException, InternalException, InvalidArgumentException, InsufficientDataException, IOException, XmlPullParserException {
		minioClient.putObject(bucketName, fileName, filePath);

		return (minioClient.listObjects(bucketName, fileName) != null);
	}

	public String isFilePresentInBucket(String bucketName, String fileName) throws XmlPullParserException, InvalidKeyException, InvalidBucketNameException,
			NoSuchAlgorithmException, InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException {
		String result = "";
		boolean isExist = minioClient.bucketExists(bucketName);
		if (isExist)
			for (Result<Item> itemNow : minioClient.listObjects(bucketName, fileName)) {

				result += itemNow.get().objectName() + "\n";
			}
		return result;
	}

	public String getWorkdirFiles(String bucketName) throws XmlPullParserException, InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException {
		String result = "";
		for (Result<Item> itemNow : minioClient.listObjects(bucketName)) {

			result += itemNow.get().objectName() + "\n";
		}

		return result;
	}
}
