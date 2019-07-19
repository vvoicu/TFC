package com.monarch.apps.wiki.defs;

import com.monarch.apps.wiki.steps.WikipediaSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

public class SearchDefinitions {
	@Steps
	WikipediaSteps wikiSteps;

	@Given("^I want to buy '(.*)'")
	public void buyerWantsToBuy(String article) {
		wikiSteps.goToHomePage();
	}

	@When("^I search for items containing '(.*)'")
	public void searchByKeyword(String keyword) {
		wikiSteps.searchFor(keyword);
	}

	@Then("^I should only see items related to '(.*)'")
	public void resultsForACategoryAndKeywordInARegion(String keyword) {
		wikiSteps.shouldSeeDefinitionForLanguageThatStartsWith(keyword, "en");
	}
}
