package com.experthealth.client;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.experthealth.configuration.RestClientPropertiesDecorator;
import com.experthealth.exceptions.GenericRestTemplateException;

/**
 * Service Interface for the Generic Rest Template to make calls to Restful APIs
 * <p/>
 * Created on 05/03/2015.
 * <b/>
 * @author moses.mansaray
 */
interface RestClient {
    	
  /**
   * Makes a dynamic request to a given url.
   * 
   * @param url The url to request
   * @param parameters a collection of parameters (if any)
   * @param http Method the request method
   * @param requestEntity the request entity Headers
   * @return The response as an entity
   * 
   * @throws GenericRestTemplateException
   */
  public ResponseEntity<String> makeRequest(String url, Map<String, String> parameters, HttpMethod httpMethod, HttpEntity<Object> requestEntity) throws GenericRestTemplateException;

  /**
   * Makes call to a restful API and returns the response
   * <b/>
   * @return a String - this will represent the returned object
   * @throws GenericRestTemplateException
   */
  public String getClientResponse() throws GenericRestTemplateException;

  /**
   * Configures the underlying architecture such as setting the keyStore and SSlContext to enable SSL
   *
   * @return true if the configuration is successful
   * @throws GenericRestTemplateException

   */
  public boolean configureSSLStrategy() throws GenericRestTemplateException;


  /**
   * Sets HTTP Headers and Params before
   * Making call to a restful API and returns the response
   * <b/>
   * @return a String - this will represent the returned object
   * @throws GenericRestTemplateException
   */
  public String getClientResponseWithParamsAndHeaders() throws GenericRestTemplateException;

    /**
     * Makes a POST call to a restful API and returns the response
     * <b/>
     * @param modelToPost the model to post
     * @param <T> the Generic type
     * @return a String - this will represent the returned object from the post operation
     * @throws GenericRestTemplateException is possible thrown
     */
  public <T> String getClientPostResponse(T modelToPost) throws GenericRestTemplateException;

  /**
   * Makes a Delete call to a restful API and returns the response
   * <b/>
   * @return String - this will represent the returned object from the DELETE operation
   * @throws GenericRestTemplateException is possible thrown
   */
  public String getClientDeleteResponse() throws GenericRestTemplateException;

  /**
   * Makes a PUT call to a restful API and returns the response
   * <b/>
   * @param modelToPut  the model to PUT
   * @param <T> the Generic type
   * @return String - this will represent the returned object from the PUT operation
   * @throws GenericRestTemplateException is possible thrown
   */
  public <T> String getClientPutResponse(T modelToPut) throws GenericRestTemplateException;

  /**
   * Get the injected RestClientPropertiesDecorator for the instance of this class
   * <b/>
   * @return the RestClientPropertiesDecorator
   */
  public RestClientPropertiesDecorator getProperties();

  }
