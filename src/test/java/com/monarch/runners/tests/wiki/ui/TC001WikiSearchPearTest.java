package com.monarch.runners.tests.wiki.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.monarch.apps.wiki.steps.WikipediaSteps;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

@RunWith(SerenityRunner.class)
public class TC001WikiSearchPearTest {

	@Managed(uniqueSession = true)
	public WebDriver webdriver;

	@Steps
	public WikipediaSteps wikipediaSteps;

	private String searchTerm;
	private String expectedText;
	private String expectedLanguage;
	
	@Before
	public void setupData() {
		searchTerm = "pear";
		expectedText = "An edible fruit produced by the pear tree, similar to an apple but elongated towards the stem.";
		expectedLanguage = "en";
	}

	@Test
	public void tc001WikiSearchPearTest() {
		wikipediaSteps.goToHomePage();
		wikipediaSteps.searchFor(searchTerm);
		wikipediaSteps.shouldSeeDefinitionForLanguageThatStartsWith(expectedText, expectedLanguage);
	}

}