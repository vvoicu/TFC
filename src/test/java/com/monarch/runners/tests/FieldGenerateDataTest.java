package com.monarch.runners.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.monarch.tools.FieldGenerators;
import com.monarch.tools.FieldGenerators.Mode;

@RunWith(JUnit4.class)
public class FieldGenerateDataTest {

	@Test
	public void generateEngDataTest() {
		System.setProperty("alphabetFile", "local");
		String result = FieldGenerators.generateRandomString(20, Mode.ALPHA_CAPS);
		System.out.println("EngData: " + result);
		Assert.assertTrue("Expected size is 20, actual: " + result.length(), result.length() == 20);
	}

	@Test
	public void generateSlavDataTest() {
		System.setProperty("alphabetFile", "slav");
		String result = FieldGenerators.generateRandomString(22, Mode.ALPHA);
		System.out.println("SlavData: " + result);
		Assert.assertTrue("Expected size is 22, actual: " + result.length(), result.length() == 22);

	}
	
	@Test
	public void generateChinaDataTest() {
		System.setProperty("alphabetFile", "china");
//		System.out.println(ConfigUtils.getDictionary("ALPHA"));
		String result = FieldGenerators.generateRandomString(22, Mode.ALPHA);
		System.out.println("ChinaData: " + result);
		Assert.assertTrue("Expected size is 22, actual: " + result.length(), result.length() == 22);

	}

	@Test
	public void generateNoAlphabetDataTest() {
		System.setProperty("alphabetFile", "slav");
		String result = FieldGenerators.generateRandomString(22, Mode.ALPHANUMERIC);
		System.out.println("NoAlphabet: " + result);
		Assert.assertTrue("Expected size is 22, actual: " + result.length(), result.length() == 22);

	}
}
