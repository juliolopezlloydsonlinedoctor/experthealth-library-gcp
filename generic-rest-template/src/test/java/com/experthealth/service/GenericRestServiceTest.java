package com.experthealth.service;

import com.experthealth.CustomMockHttpServer;
import com.experthealth.ProductMock;
import com.experthealth.exceptions.GenericRestTemplateException;
import org.apache.http.HttpStatus;
import org.apache.wink.client.MockHttpServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Spring test class for GenericRestService Class
 *
 * Created on 13/03/2015.
 *
 * @author moses.mansaray
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-config.xml")
@PropertySource("classpath*:test.properties")
public class GenericRestServiceTest extends CustomMockHttpServer{

  @Autowired
  @Qualifier("genericRestService")
  private RestService genericRestService;

  @Autowired
  @Qualifier("genericRestServiceForHTTPMock")
  private RestService genericRestServiceForHTTPMock;

  /**
   * Scenario: Should return Json String Response for http GET service
   * <p/>
   * Expected: returns Json String
   */
  //@Test
  public void shouldReturnJsonString() throws GenericRestTemplateException {
    String assertMsg = "Should return Json String for http GET service";
    //Given
    //When
    String restfulResource = genericRestService.getRestfulResource();
    boolean result = !restfulResource.isEmpty() & restfulResource.length() > 0;
    //Then
    assertTrue(assertMsg, result);

  }

  /**
   * Scenario: Should return Json String Response for http GET service
   * <p/>
   * Expected: returns Json String
   */
  //@Test
  public void shouldReturnJsonStringForCallWithHTTPParams() throws GenericRestTemplateException {
    //Given
    String assertMsg = "Should return Json String for http GET service";
    //When
    String restfulResource = genericRestService.getClientResponseWithParamsAndHeaders();
    boolean result = !restfulResource.isEmpty() & restfulResource.length() > 0;
    //Then
    assertTrue(assertMsg, result);

  }

  /**
   * Scenario: Should return Json String Response for http POST service
   * <p/>
   * Expected: returns Json String
   */
  @Test
  public void shouldReturnJsonStringResponseForHTTPPPostService() throws GenericRestTemplateException {
    //Given
    String responseContent = "someResponse";
    initializeDefaultHHTPServerConfig(responseContent);
    ArrayList<ProductMock> modelToPost = new ArrayList<ProductMock>();
    modelToPost.add(new ProductMock(12, "some-name"));

    //When
    String restfulResource = genericRestServiceForHTTPMock.getClientPostResponse(modelToPost);
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();
    boolean result = !restfulResource.isEmpty() & restfulResource.length() > 0;

    //Then
    assertTrue("return String was empty", result);
    assertTrue("did not Match the expected Json String for http POST service", restfulResource.contains(responseContent));
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.POST.toString());

  }

  /**
   * Scenario: Should return HTTP Status OK when Service is call to delete
   * <p/>
   * Expected: returns Json String
   */
  @Test
  public void shouldReturnHTTPStatusOKWhenDeleteServiceIsCall() throws GenericRestTemplateException {
    //Given
    String responseContent = "deleteResource";
    initializeDefaultHHTPServerConfig(responseContent);

    //When
    genericRestServiceForHTTPMock.getClientDeleteResponse();
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();
    List<MockHttpServer.MockHttpServerResponse> mockHttpServerResponses = getMockServer().getMockHttpServerResponses();
    int mockResponseCode = mockHttpServerResponses.get(0).getMockResponseCode();

    //Then
    assertEquals("did not returned with HTTP Status OK", HttpStatus.SC_OK, mockResponseCode);
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.DELETE.toString());

  }

  /**
   * Scenario: Should return HTTP Status OK when Service is call for Put operation
   * <p/>
   * Expected: returns Json String
   */
  @Test
  public void shouldReturnHTTPStatusOKWhenPutServiceIsCall() throws GenericRestTemplateException {
    //Given
    String responseContent = "putResource";
    initializeDefaultHHTPServerConfig(responseContent);
    ProductMock productMock =  new ProductMock(1346562,"name");


    //When
    genericRestServiceForHTTPMock.getClientPutResponse(productMock);
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();
    List<MockHttpServer.MockHttpServerResponse> mockHttpServerResponses = getMockServer().getMockHttpServerResponses();
    int mockResponseCode = mockHttpServerResponses.get(0).getMockResponseCode();

    //Then
    assertEquals("did not returned with HTTP Status OK", HttpStatus.SC_OK, mockResponseCode);
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.PUT.toString());

  }

}
