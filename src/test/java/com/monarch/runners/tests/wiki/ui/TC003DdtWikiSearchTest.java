package com.monarch.runners.tests.wiki.ui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarch.apps.wiki.steps.WikipediaSteps;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.Qualifier;
import net.thucydides.junit.annotations.UseTestDataFrom;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "src/test/resources/features/wiki/TC002DdtWikiSearchTest.csv")
public class TC003DdtWikiSearchTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Managed(uniqueSession = true)
	public WebDriver webdriver;

	@Steps
	public WikipediaSteps wikipediaSteps;

	// keys/headers found in the csv data files
	private String id, searchTerm, language, verificationMessage;

	@Qualifier
	public String qualifier() {
		return id + " = " + searchTerm + " + " + verificationMessage;
	}

	@Test
	public void tc003DdtWikiSearchTest() {
		logger.info("LOG: Running first test.");
		wikipediaSteps.goToHomePage();
		wikipediaSteps.searchFor(searchTerm);
		wikipediaSteps.shouldSeeDefinitionForLanguageThatStartsWith(verificationMessage, language);
	}

}