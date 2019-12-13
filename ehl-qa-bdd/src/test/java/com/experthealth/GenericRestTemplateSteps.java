package com.experthealth;

import com.experthealth.service.GenericRestService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertTrue;

/**
 * Step Definitions Support class for the ExpertHealth Generic-Rest-Template
 *
 * Created by moses.mansaray on 03/03/2015.
 */
@ContextConfiguration("/cucumber.xml")
@PropertySource("classpath*:qa-test.properties")
public class GenericRestTemplateSteps {

  /** Classes under test */
  @Autowired @Qualifier("genericRestServiceWithValidProperties")
  private GenericRestService genericRestClientWithSSL;
  @Autowired @Qualifier("genericRestServiceWithInvalidProperties")
  private GenericRestService genericRestServiceWithInvalidProperties;

  private GenericRestService genericRestClient;
  private String restfulResource;
  boolean isExceptionThrown = false;

  @Given("^valid parameters$")
  public void valid_parameters() throws Throwable {
    genericRestClient = genericRestClientWithSSL;

  }

  @When("^the request is made$")
  public void the_request_is_made() throws Throwable {
    try {
      restfulResource = genericRestClient.getRestfulResource();
    }catch (Exception e){
      isExceptionThrown = true;
    }

  }

  @Given("^an invalid parameters$")
  public void an_invalid_parameters() throws Throwable {
    genericRestClient = genericRestServiceWithInvalidProperties;

  }

  @Then("^an exception is thrown$")
  public void an_exception_is_thrown() throws Throwable {
    assertTrue(isExceptionThrown);

  }

  @Then("^a json array string is return with size greater than (\\d+)$")
  public void a_json_array_string_is_return_with_size_greater_than(int sizeExpected) throws Throwable {
    assertTrue(restfulResource.length() > sizeExpected);

  }

}
