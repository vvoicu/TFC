package com.monarch.runners.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.monarch.runners.tests.FieldGenerateDataTest;
import com.monarch.runners.tests.ecs.mail.TC004CheckEmailTest;
import com.monarch.runners.tests.reqres.api.TC002ApiReqresGetTest;
import com.monarch.runners.tests.wiki.ui.TC001WikiSearchAppleTest;
import com.monarch.runners.tests.wiki.ui.TC003DdtWikiSearchTest;
import com.monarch.runners.tests.wiki.ui.TC008BddCucumberRunnerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ FieldGenerateDataTest.class, TC001WikiSearchAppleTest.class, TC002ApiReqresGetTest.class, TC003DdtWikiSearchTest.class, TC004CheckEmailTest.class, TC008BddCucumberRunnerTest.class })
public class RegressionSuite {

}
