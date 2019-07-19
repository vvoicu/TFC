package com.monarch.runners.tests.wiki.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarch.apps.wiki.steps.WikipediaSteps;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

@RunWith(SerenityRunner.class)
public class TC001WikiSearchAppleTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Managed(uniqueSession = true)
	public WebDriver webdriver;

	@Steps
	public WikipediaSteps wikipediaSteps;

	private String searchTerm;
	private String expectedText;
	private String expectedLanguage;

	@Before
	public void setupData() {
		searchTerm = "apple";
		expectedText = "A common, round fruit produced by the tree Malus domestica, cultivated in temperate climates.";
		expectedLanguage = "en";
	}

	@Issue("#WIKI-1")
	@Test
	public void tc001WikiSearchAppleTest() {
		logger.info("LOG: Running first test.");
		wikipediaSteps.goToHomePage();
		wikipediaSteps.searchFor(searchTerm);
		wikipediaSteps.shouldSeeDefinitionForLanguageThatStartsWith(expectedText, expectedLanguage);
	}
}