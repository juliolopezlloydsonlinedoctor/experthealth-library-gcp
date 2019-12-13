package com.experthealth.client;

import com.experthealth.CustomMockHttpServer;
import com.experthealth.ProductMock;
import com.experthealth.configuration.RestClientPropertiesDecorator;
import com.experthealth.exceptions.GenericRestTemplateException;

import org.apache.http.HttpStatus;
import org.apache.wink.client.MockHttpServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.net.ssl.SSLContext;

import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test for the GenericRestService implementation of RestService Interface.
 * <p/>
 * Created  on 05/03/2015.
 * @author moses.mansaray
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-config.xml")
@PropertySource("classpath*:test.properties")
public class GenericRestClientTest extends CustomMockHttpServer {

  /** Classes under test */
  @Autowired @Qualifier("genericRestClientWithSSL")
  private RestClient genericRestClientWithSSL;
  @Autowired @Qualifier("genericRestClientWithoutSSL")
  private RestClient genericRestClientWithoutSSL;
  @Autowired @Qualifier("genericRestClientWithInvalidSSLConfiguration")
  private RestClient genericRestClientWithInvalidSSLConfiguration;
  @Autowired @Qualifier("genericRestClientWithInvalidEncryption")
  private RestClient genericRestClientWithInvalidEncryption;
  @Autowired @Qualifier("genericRestClientWithInvalidPass")
  private RestClient genericRestClientWithInvalidPass;
  @Autowired @Qualifier("genericRestClientWithInvalidURL")
  private RestClient genericRestClientWithInvalidURL;
  @Autowired @Qualifier("genericRestClientWithSSLGreen")
  private RestClient genericRestClientWithSSLGreen;
  @Autowired @Qualifier("genericRestClientWithSSLBlack")
  private RestClient genericRestClientWithSSLBlack;
  @Autowired @Qualifier("genericRestClientForHTTPMock")
  private RestClient genericRestClientNoSSLMock;

  @Autowired @Qualifier("restClientPropertiesWithSSL")
  private RestClientPropertiesDecorator restClientPropertiesWithSSL;

  /**
   * Scenario: GenericRestService should be an instance of RestService.
   * <p/>
   * Expected: GenericRestService is an instance of RestService.
   */
  @Test
  public void shouldBeAnInstanceOfRestService() {

    String msg = "GenericRestService should be an instance of RestService";
    //Given
    //When
    boolean result = genericRestClientWithSSL != null;
    //Then
    assertTrue(msg, result);
  }


  /**
   * Scenario: should return a String that represent the returned object from Rest Client Response.
   * <p/>
   * Expected: returns a String that represent the returned object from Rest Client Response.
   */
  //@Test
  public void shouldReturnAStringFromRestClient() throws GenericRestTemplateException {
    String assertMsg = "should return a String that represent the returned object from Rest client Response";
    //Given
    //When
    String result = genericRestClientWithSSL.getClientResponse();
    //Then
    assertTrue(assertMsg, result != null);

  }

  /**
   * Scenario: Should return a string representing the Restful resource for MOHC_PREPRODUCTION.
   * <p/>
   * Expected: returns a string representing the Restful resource for MOHC_PREPRODUCTION.
   */
  //@Test
  public void shouldReturnAStringRepresentingTheRestfulResourceFOrPreProd() throws GenericRestTemplateException {
    String assertMsg = "Should return a string representing the Restful resource";
    //Given
    //When
    String clientResponse = genericRestClientWithSSL.getClientResponse();
    //Then
    boolean result = clientResponse.length() > 0;
    assertTrue(assertMsg, result);

  }


  /**
   * Scenario: Should return a string representing the Restful resource for MOHC_PREPRODUCTION.
   * <p/>
   * Expected: returns a string representing the Restful resource for MOHC_PREPRODUCTION.
   */
  //@Test
  public void shouldReturnAStringRepresentingTheRestfulResourceForCallWithHTTPParamsSet() throws GenericRestTemplateException {
    String assertMsg = "Should return a string representing the Restful resource";
    //Given
    //When
    String clientResponse = genericRestClientWithSSL.getClientResponseWithParamsAndHeaders();
    //Then
    boolean result = clientResponse.length() > 0;
    assertTrue(assertMsg, result);

  }

  /**
   * Scenario: should return true after setting the ssl strategy.
   * <p/>
   * Expected: returns true after setting the ssl strategy.
   */
  @Test
  public void testShouldReturnTrueAfterSettingTheSslStrategy() throws GenericRestTemplateException {
    String assertMsg = "should return true after setting the ssl strategy";
    //Given
    //When
    boolean result = genericRestClientWithSSL.configureSSLStrategy();
    //Then
    assertTrue(assertMsg, result);

  }

  /**
   * Scenario: Should return a string representing the Restful resource for GreenEnvironment.
   * <p/>
   * Expected: returns a string representing the Restful resource for GreenEnvironment.
   */
  //@Test
  public void returnAStringRepresentingTheRestfulResourceForGreenEnvironment() throws GenericRestTemplateException {
    String assertMsg = "Expected returned String is empty";
    //Given
    String clientResponse = genericRestClientWithSSLGreen.getClientResponse();
    //When
    boolean result = clientResponse.length() > 0;
    //Then
    assertTrue(assertMsg, result);

  }


  /**
   * Scenario: non ssl enabled protocol should return the restful resource.
   * <p/>
   * Expected: non ssl enabled protocol returns the restful resource.
   */
  @Test
  public void nonSSLEnabledProtocolShouldReturnTheRestfulResource() throws GenericRestTemplateException {
    String assertMsg = "test against non ssl enabled protocol should return the restful resource";
    //Given
    //When
    String clientResponse = genericRestClientWithoutSSL.getClientResponse();
    boolean result = clientResponse.length() > 0;
    //Then
    assertTrue(assertMsg, result);

  }

  /**
   * Scenario: Should return a KeyStore for the certificate.
   * <p/>
   * Expected: returns a KeyStore for the certificate.
   */
  @Test
  public void shouldReturnAInputStreamForTheCertificate() throws KeyStoreException, GenericRestTemplateException {
    String assertMsg = "Should return a InputStream for the certificate";
    //Given
    KeyStore certificatesKeyStore = ((GenericRestClient) genericRestClientWithSSL).getCertificatesKeyStore();
    //When
    boolean result = certificatesKeyStore != null;
    //Then
    assertTrue(assertMsg, result);
    assertEquals(assertMsg, restClientPropertiesWithSSL.getSslKeyStoreType(), certificatesKeyStore.getType());
    assertTrue(assertMsg, certificatesKeyStore.size() > 0);

  }


  /**
   * Scenario: Should return an initialized SSLContext.
   * <p/>
   * Expected: returns an initialized SSLContext.
   */
  @Test
  public void shouldReturnAnInitializedSSLContext() throws GenericRestTemplateException {
    String assertMsg = "Should return an initialized SSLContext";
    //Given
    KeyStore certificatesKeyStore = ((GenericRestClient) genericRestClientWithSSL).getCertificatesKeyStore();
    //When
    SSLContext sslContext = ((GenericRestClient) genericRestClientWithSSL).getInitializeSSLContext(certificatesKeyStore);
    boolean result = sslContext != null;
    //Then
    assertTrue(assertMsg, result);
    assertEquals(assertMsg, restClientPropertiesWithSSL.getSslEncryptionProtocol(), sslContext.getProtocol());

  }


  /**
   * Scenario: Should throw invalid SSL configuration Exception.
   * <p/>
   * Expected: throws invalid SSL configuration Exception.
   */
  @Test(expected = GenericRestTemplateException.class)
  public void shouldThrowInvalidSSLConfigurationException() throws GenericRestTemplateException {
    //Given
    genericRestClientWithInvalidSSLConfiguration.getClientResponse();
    //When
    //Then

  }

  /**
   * Scenario: Should throw GenericRestTemplateException for a invalid Encryption.
   * <p/>
   * Expected: throws GenericRestTemplateException for a invalid Encryption..
   */
  @Test(expected = GenericRestTemplateException.class)
  public void shouldThrowGenericRestTemplateExceptionForInvalidEncryption() throws GenericRestTemplateException {
    //Given
    genericRestClientWithInvalidEncryption.getClientResponse();
    //When

  }

  /**
   * Scenario: Should throw GenericRestTemplateException for a invalid Password.
   * <p/>
   * Expected: throws GenericRestTemplateException for a invalid Password..
   */
  @Test(expected = GenericRestTemplateException.class)
  public void shouldThrowGenericRestTemplateExceptionForInvalidPassword() throws GenericRestTemplateException {
    //Given
    genericRestClientWithInvalidPass.getClientResponse();
    //When
    //Then

  }

  /**
   * Scenario: Should throw GenericRestTemplateException for a invalid URL.
   * <p/>
   * Expected: throws GenericRestTemplateException for a invalid URL..
   */
  @Test(expected = GenericRestTemplateException.class)
  public void shouldThrowGenericRestTemplateExceptionForInvalidURL() throws GenericRestTemplateException {
    //Given
    genericRestClientWithInvalidURL.getClientResponse();
    //When
    //Then

  }

  /**
   * Scenario: Should call server with Get HTTP method for Getting resource
   * <p/>
   * Actual:  called server with Get HTTP method
   * @throws GenericRestTemplateException
   */
  @Test
  public void shouldCallServerWithGetHTTPMethodForGettingResource() throws GenericRestTemplateException {
    //Given
    String responseContent = "someResponse";
    initializeDefaultHHTPServerConfig(responseContent);

    //When
    String clientResponse = genericRestClientNoSSLMock.getClientResponse();
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();

    //Then
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.GET.toString());
    assertTrue("returned content not as expected", clientResponse.contains(responseContent));

  }

  /**
   * Scenario: Should call server with Get HTTP method for Posting resource
   * <p/>
   * Actual:  called server with Get HTTP method
   * @throws GenericRestTemplateException
   */
  @Test
  public void shouldCallServerWithGetHTTPMethodForPostingResource() throws GenericRestTemplateException {
    //Given
    String responseContent = "someResponse";
    initializeDefaultHHTPServerConfig(responseContent);
    ProductMock productMock =  new ProductMock(12,"name");


    //When
    String clientResponse = genericRestClientNoSSLMock.getClientPostResponse(productMock);
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();

    //then
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.POST.toString());
    assertTrue("returned content not as expected", clientResponse.contains(responseContent));

  }

  /**
   * Scenario: Should call server with DEL HTTP method for Delete resource Call
   * <p/>
   * Actual:  called server with Get HTTP method
   * @throws GenericRestTemplateException
   */
  @Test
  public void shouldCallServerWithDelHTTPMethodForDeleteResource() throws GenericRestTemplateException {
    //Given
    String responseContent = "deleteResource";
    initializeDefaultHHTPServerConfig(responseContent);

    //When
    genericRestClientNoSSLMock.getClientDeleteResponse();
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();

    List<MockHttpServer.MockHttpServerResponse> mockHttpServerResponses = getMockServer().getMockHttpServerResponses();
    int mockResponseCode = mockHttpServerResponses.get(0).getMockResponseCode();

    //then
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.DELETE.toString());
    assertEquals("did not returned with HTTP Status OK", HttpStatus.SC_OK, mockResponseCode);

  }


  /**
   * Scenario: Should call server with PUT HTTP method for Putting resource
   * <p/>
   * Actual:  called server with PUT HTTP method
   * @throws GenericRestTemplateException
   */
  @Test
  public void shouldCallServerWithPUTHTTPMethodForPuttingResource() throws GenericRestTemplateException {
    //Given
    String responseContent = "{\"product\": \"sku\"}";
    initializeDefaultHHTPServerConfig(responseContent);
    ProductMock productMock =  new ProductMock(12,"name");

    //When
    genericRestClientNoSSLMock.getClientPutResponse(productMock);
    String requestMethod = getMockServer().getRequestMethod();
    getMockServer().stopServer();
    List<MockHttpServer.MockHttpServerResponse> mockHttpServerResponses = getMockServer().getMockHttpServerResponses();
    int mockResponseCode = mockHttpServerResponses.get(0).getMockResponseCode();

    //then
    assertEquals("did not returned with the expected requestMethod", requestMethod, HttpMethod.PUT.toString());
    assertEquals("did not returned with HTTP Status OK", HttpStatus.SC_OK, mockResponseCode);

  }

  /**
   * Scenario: Should return String object after a Http Put
   * <p/>
   * Actual; returns a String object
   * @throws GenericRestTemplateException
   */
  @Test
  public void shouldReturnAStringObjectAfterAHttpPut() throws GenericRestTemplateException {
    //Given
    String responseContent = "{\"product\": \"sku\"}";
    initializeDefaultHHTPServerConfig(responseContent);
    ProductMock productMock =  new ProductMock(12,"name");

    //When
    String response = genericRestClientNoSSLMock.getClientPutResponse(productMock);
    getMockServer().stopServer();

    //then
    assertTrue("response string is empty", response != null);

  }

  /**
   * Scenario: Should return a String object with Content after a Http Put
   * <p/>
   * Actual: returns a String object with content
   * @throws GenericRestTemplateException
   */
  @Test
  public void shouldReturnAStringObjectWithContentAfterAHttpPut() throws GenericRestTemplateException {
    //Given
    String responseContent = "{\"product\": \"sku\"}";
    initializeDefaultHHTPServerConfig(responseContent);
    ProductMock productMock =  new ProductMock(12,"name");

    //When
    String response = genericRestClientNoSSLMock.getClientPutResponse(productMock);
    getMockServer().stopServer();

    //then
    assertTrue("Expected Response does not match Actual response String", response.contains(responseContent));

  }

  /**
   * Scenario: Should return String object after a Http delete
   * <p/>
   * Actual; returns a String object
   */
  @Test
  public void shouldReturnAStringObjectAfterAHttpDelete() throws GenericRestTemplateException {
    // Given
    ProductMock responseProductMock = new ProductMock(13, "Mr Vegas");
    initializeDefaultHHTPServerConfig(responseProductMock);

    // When
    String response = genericRestClientNoSSLMock.getClientDeleteResponse();
    getMockServer().stopServer();

    // Then
    assertTrue("response is not a string object", response != null);

  }

  /**
   * Scenario: Should return a response with data after a Http delete
   * <p/>
   * Actual; returns a response with object
   */
  @Test
  public void shouldReturnAResponseWithDataAfterAHttpDelete() throws GenericRestTemplateException {
    // Given
    ProductMock productMock = new ProductMock(13, "Mr Vegas");
    initializeDefaultHHTPServerConfig(productMock);

    // When
    String response = genericRestClientNoSSLMock.getClientDeleteResponse();
    getMockServer().stopServer();

    // Then
    assertTrue("Expected returned String is empty", response.length() > 0);

  }

  /**
   * Scenario: Should pass through the HTTP Headers to the Service Server
   *
   * Actual: passes through the HTTP Headers to the Service Server
   */
  @Test
  public void shouldPassThroughTheHTTPHeadersToTheServiceServer() throws GenericRestTemplateException {
    // Given
    Map<String, String> expectedHeaderMap = genericRestClientNoSSLMock.getProperties().getHeaders();
    ProductMock productMock = new ProductMock(13, "Mr Vegas");
    initializeDefaultHHTPServerConfig(productMock);

    // When
    genericRestClientNoSSLMock.getClientDeleteResponse();
    Map<String, List<String>> requestHeaders = getMockServer().getRequestHeaders();
    String requestedHeaderStrings = requestHeaders.toString();
    getMockServer().stopServer();

    // Then
    for (Map.Entry<String, String> entry : expectedHeaderMap.entrySet()){
      assertTrue("Expected header Entry Key not in response", requestedHeaderStrings.contains(entry.getKey().toString()));
      assertTrue("Expected header Entry Value not in response", requestedHeaderStrings.contains(entry.getValue().toString()));
    }

  }
  
 /**
  * Scenario: Make a dynamic GET request.
  * <p/>
  * Expected: The GET response is returned.
  * 
 * @throws GenericRestTemplateException 
  */
  @Test
  public void shouldMakeDynamicGetRequest() throws GenericRestTemplateException {
    // Given
	  Map<String, String> parameterMap = new HashMap<String, String>();
	    parameterMap.put("site", "UK");	 
	  String URL = "https://somewhere.com";
	  HttpMethod httpMethod = HttpMethod.GET;
	  HttpEntity<Object> requestEntity = new HttpEntity<Object>("", getDefaultHttpHeaders());
	  RestClient testClient = Mockito.mock(GenericRestClient.class);
	  ResponseEntity<String> responseEntity = new ResponseEntity<String>("result of GET", null);
	  	// When
	  	  Mockito.when(testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity)).thenReturn(responseEntity);
	  ResponseEntity<String> response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
	// Then
	  assertTrue(response.getBody().equalsIgnoreCase("result of GET"));
  }
  
  /**
   * Scenario: Make a dynamic PUT request.
   * <p/>
   * Expected: The PUT response is returned.
   * 
  * @throws GenericRestTemplateException 
   */
   @Test
   public void shouldMakeDynamicPutRequest() throws GenericRestTemplateException {
     // Given
 	  Map<String, String> parameterMap = new HashMap<String, String>();
 	    parameterMap.put("site", "UK");	 
 	  String URL = "https://somewhere.com";
 	  HttpMethod httpMethod = HttpMethod.PUT;
 	  HttpEntity<Object> requestEntity = new HttpEntity<Object>("", getDefaultHttpHeaders());
 	  RestClient testClient = Mockito.mock(GenericRestClient.class);
 	 ResponseEntity<String> responseEntity = new ResponseEntity<String>("result of PUT", null);
   	// When
   	  Mockito.when(testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity)).thenReturn(responseEntity);
 	  ResponseEntity<String> response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
 	// Then
 	  assertTrue(response.getBody().equalsIgnoreCase("result of PUT"));
   }
   
   /**
    * Scenario: Make a dynamic POST request.
    * <p/>
    * Expected: The POST response is returned.
    * 
   * @throws GenericRestTemplateException 
    */
    @Test
    public void shouldMakeDynamicPostRequest() throws GenericRestTemplateException {
      // Given
  	  Map<String, String> parameterMap = new HashMap<String, String>();
  	    parameterMap.put("site", "UK");	 
  	  String URL = "https://somewhere.com";
  	  
  	  HttpMethod httpMethod = HttpMethod.POST;
  	  HttpEntity<Object> requestEntity = new HttpEntity<Object>("", getDefaultHttpHeaders());
  	  RestClient testClient = Mockito.mock(GenericRestClient.class);
  	    	  
  	  ResponseEntity<String> responseEntity = new ResponseEntity<String>("result of POST", null);
  	// When
  	  Mockito.when(testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity)).thenReturn(responseEntity);
  	  ResponseEntity<String> response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
  	// Then
  	  assertTrue(response.getBody().equalsIgnoreCase("result of POST"));
    }
  
  private HttpHeaders getDefaultHttpHeaders() {
	  Map<String, String> headersMap = new HashMap<String, String>();
	  HttpHeaders headers = new HttpHeaders();
	  headers.add("Content-Type", "application/json");
	  headers.add("Accept", "*/*");

	  for (Map.Entry<String, String> entry : headersMap.entrySet()){
	    headers.add(entry.getKey(), entry.getValue());
	  }
	  return headers;
   }

  /**
   * Scenario: Should get an empty basket Json from Black environment
   * <p/>
   * Expected: returns a ResponseEntity<String> representing the Restful resource for Black.
   */
  @Test
  public void shouldGetEmptyBasketFromBlack() throws GenericRestTemplateException {
    //Given
    String assertMsg = "Expected returned String is empty";
    String url = "https://sal.black.drthom.com:444/sal/basket?site=lp";
    Map<String, String> parameters = new HashMap<String, String>();
    HttpMethod httpMethod = HttpMethod.GET;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Accept", "application/vnd.sal.v2+json");
    httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
    HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);

    ResponseEntity<String> serviceResponse = genericRestClientWithSSLBlack.makeRequest(url, parameters, httpMethod, requestEntity);
    //When
    boolean result = serviceResponse.getBody().length() > 0;
    //Then
    assertTrue(assertMsg, result);

  }

}
