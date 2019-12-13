package com.experthealth;


import org.apache.wink.client.MockHttpServer;

/**
 * MockHttpServer implementation to support mocking services.
 * <p/>
 *
 * Created on 13/05/2015.
 *
 * @author moses.mansaray
 */
public class CustomMockHttpServer {

  /** The Mocked Server **/
  protected MockHttpServer mockServer;

  /** The Mocked Server Response Object **/
  protected MockHttpServer.MockHttpServerResponse response;

  /**
   * Default Mock HTTP Server implementation, this can be overridden by setting your own properties
   * and starting the server...
   *
   * @param responseContent the content to response back with
   */
  public <T> void initializeDefaultHHTPServerConfig(T responseContent) {
    mockServer = new MockHttpServer(3333);
    mockServer.startServer();
    response = new MockHttpServer.MockHttpServerResponse();
    response.setMockResponseContent(responseContent.toString());
    response.setMockResponseCode(200);
    mockServer.setMockHttpServerResponses(response);
  }

  /**
   * The Mocked Server
   * @return MockHttpServer whose configuration can be overridden
   */
  public MockHttpServer getMockServer() {
    return mockServer;
  }

}
