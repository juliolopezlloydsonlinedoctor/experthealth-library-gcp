package com.experthealth.client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.experthealth.configuration.RestClientPropertiesDecorator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Integration to test making dynamic requests using the GenericRest Template.
 * 
 * @author ronan.tobin
 *
 */
@SuppressWarnings("restriction")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-config.xml")
public class GenericRestClientIT {

	public void startUpMockServer() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/test", new MyHandler());
		server.setExecutor(null);
		server.start();
	}

	static class MyHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String response = "The request method is "+t.getRequestMethod();
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	/**
	 * Scenario: The HTTP request types PUT, DELETE, GET, POST use the generic rest template.
	 * <p/>
	 * Expected: The correct response to each method type is in the response.
	 */
	@Test
	public void shouldMakeHttpMethodRequestsUsingGenericRestTemplate() throws Exception {
		// Given
		HttpMethod httpMethod;
		RestTemplate restTemplate = new RestTemplate();
		RestClientPropertiesDecorator testProperties = new RestClientPropertiesDecorator("","","","","",null,null);
		
		GenericRestClient testClient = new GenericRestClient(testProperties);
		testClient.setExternalTemplate(restTemplate);

		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("site", "UK");
		String URL = "http://localhost:8000/test";
		HttpEntity<Object> requestEntity = new HttpEntity<Object>("",getDefaultHttpHeaders());
		startUpMockServer();
		
		
		// When
		httpMethod = HttpMethod.DELETE;
		ResponseEntity<String> response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
		// Then
		assertTrue("The response was not as expected", response.getBody().contains("The request method is "+httpMethod));
		
		// When
		httpMethod = HttpMethod.GET;
		response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
		// Then
		assertTrue("The response was not as expected",response.getBody().contains("The request method is " + httpMethod));
		
		// When
		httpMethod = HttpMethod.PUT;
		response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
		// Then
		assertTrue("The response was not as expected",response.getBody().contains("The request method is " + httpMethod));

		// When
		httpMethod = HttpMethod.POST;
		response = testClient.makeRequest(URL, parameterMap, httpMethod, requestEntity);
		// Then
		assertTrue("The response was not as expected",response.getBody().contains("The request method is " + httpMethod));
	}

	private HttpHeaders getDefaultHttpHeaders() {
		Map<String, String> headersMap = new HashMap<String, String>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "*/*");

		for (Map.Entry<String, String> entry : headersMap.entrySet()) {
			headers.add(entry.getKey(), entry.getValue());
		}
		return headers;
	}
}
