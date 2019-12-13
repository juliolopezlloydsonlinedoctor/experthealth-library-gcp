package com.experthealth;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CustomMockHttpServer
 *
 * Created on 13/05/2015.
 *
 * @author moses.mansaray
 */
public class CustomMockHttpServerTest extends CustomMockHttpServer {

  /**
   * Scenario: Should call server with Get HTTP method for Getting resource
   *
   * Actual:  called server with Get HTTP method
   */
  @Test
  public void shouldCallServerWithGetHTTPMethodForGettingResource(){
    //Given
    String responseContent = "someResponse";
    initializeDefaultHHTPServerConfig(responseContent);
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:" + getMockServer().getServerPort() + "/somewhere";


    //When
    String clientResponse = restTemplate.getForObject(url, String.class);
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();

    //Then
    assertEquals("did not returned with he expected requestMethod", requestMethod, HttpMethod.GET.toString());
    assertEquals("returned content not as expected", responseContent, clientResponse);
  }

}