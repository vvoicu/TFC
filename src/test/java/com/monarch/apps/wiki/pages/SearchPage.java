package com.monarch.apps.wiki.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;

public class SearchPage extends PageObject {

    @FindBy(name="search")
    private WebElementFacade searchTerms;

    @FindBy(name="go")
    private WebElementFacade lookupButton;

    public void searchKeyword(String keyword) {
    	element(searchTerms).waitUntilVisible();
        searchTerms.type(keyword);
    }

    public void clickOnSearchButton() {
    	element(lookupButton).waitUntilVisible();
        lookupButton.click();
    }
}