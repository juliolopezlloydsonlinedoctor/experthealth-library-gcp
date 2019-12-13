package com.experthealth.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for the RestClientProperty model
 *
 * Created on 03/03/2015.
 *
 * @author moses.mansaray
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-config.xml")
@PropertySource("classpath*:test.properties")
public class RestClientPropertiesDecoratorTest {

  @Qualifier("restClientPropertiesWithSSL")
  @Autowired
  private RestClientProperties restClientProperties;

  /**
   *
   * Scenario: RestClientProperties should be valid
   * <p/>
   * Expected: RestClientProperties is not null
   */
  @Test
  public void restClientPropertiesShouldBeValid() {
    //Given
    String assertMsg= "RestClientProperties should be valid";
    //When
    boolean result = restClientProperties != null;
    //Then
    assertTrue(assertMsg, result);

  }


  /**
   * Scenario:  RestClientProperties should return expected properties
   * <p/>
   * Expected:  RestClientProperties returns expected properties
   */
  @Test
  public void restClientPropertiesShouldReturnExpectedProperties() {
    //Given
    String assertMsg = "RestClientProperties returns expected properties";
    //When
    RestClientPropertiesDecorator result = (RestClientPropertiesDecorator) restClientProperties;
    //Then
    assertEquals(assertMsg, "drthom123", result.getPassword());
    assertEquals(assertMsg, "https://cms-api.prerelease.drthom.com:444/api/v1/pharmacies?site=lp", result.getUrl());
    assertEquals(assertMsg, "MOHC_PREPRODUCTION", result.getEnvironmentName());
    assertEquals(assertMsg, "PKCS12", result.getSslKeyStoreType());
    assertEquals(assertMsg, "TLS", result.getSslEncryptionProtocol());

  }

}
