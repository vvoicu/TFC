package com.monarch.apps.wiki.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class SearchUser {
	@Steps
	public WikipediaSteps wikipediaSteps;
	
	@Step("#actor Navigates to Search Page")
	public void navigatesToWikiSearchPage() {
		wikipediaSteps.goToHomePage();
	}

	@Step("#actor Searches for {0}")
	public void searchesFor(String searchTerm) {
		wikipediaSteps.searchFor(searchTerm);
	}

	@Step("#actor Should see '{0}' as the result for English")
	public void shouldSeeResultForEnglish(String verificationMessage) {
		wikipediaSteps.shouldSeeDefinitionForLanguageThatStartsWith(verificationMessage, "en");
	}

	@Step("#actor Should see '{0}' as the result for {1}")
	public void shouldSeeResultForLanguage(String verificationMessage, String language) {
		wikipediaSteps.shouldSeeDefinitionForLanguageThatStartsWith(verificationMessage, language);
	}

}
