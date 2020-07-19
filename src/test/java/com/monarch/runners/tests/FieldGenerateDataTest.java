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
		System.setProperty("dictionaryFile", "local");
		String result = FieldGenerators.generateRandomString(20, Mode.ALPHA);
		System.out.println("EngData: " + result);
		Assert.assertTrue("Expected size is 20, actual: " + result.length(), result.length() == 20);
	}

	@Test
	public void generateSlavDataTest() {
		System.setProperty("dictionaryFile", "slav");
		String result = FieldGenerators.generateRandomString(22, Mode.ALPHA_CAPS);
		System.out.println("SlavData: " + result);
		Assert.assertTrue("Expected size is 22, actual: " + result.length(), result.length() == 22);

	}
}
