package com.monarch.apps.wiki.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;

public class ArticlePage extends PageObject {
    private List<String> orderedListToListOfStrings(WebElementFacade orderedList) {
        return orderedList.findElements(By.tagName("li")).stream()
                .map( element -> element.getText() )
                .collect(Collectors.toList());
    }
    
    public List<String> getDefinitions() {
    	return orderedListToListOfStrings(find(By.tagName("ol")));
    }
    
    // If language param is blank, use default of en
    private String getLanguage(String language) {
    	if (language == null || language.isEmpty()) {
    		language = "en";
    	}
    	return language;
    }
    
    // Finds the start of the word-entry on this page, for a given language
    private WebElementFacade getDefinitionSection(String language) {
    	return find(By.xpath(
    			"//strong[@class='Latn headword' and @lang='" + getLanguage(language) + "']"));
    }
    
    // Navigates from the word-entry to the definition-list
    public List<String> getDefinitionList(String language) {
    	 return orderedListToListOfStrings(getDefinitionSection(language)
    			.find(By.xpath("parent::p/following-sibling::ol")));
    }
}