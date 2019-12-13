package com.experthealth;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * This wires up Cucumber to JUnit and
 * tells it to find the feature files in the "src/test/resources" folder.
 * <p/>
 * Created by moses.mansaray on 03/03/2015.
 */
@RunWith(Cucumber.class)
@CucumberOptions(format = { "pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json" }, tags = {"~@ignore"})
public class CukesRunner {

}
