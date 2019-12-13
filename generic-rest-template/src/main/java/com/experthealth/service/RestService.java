package com.experthealth.service;

import com.experthealth.exceptions.GenericRestTemplateException;

/**
 * Interface Contract for the Exposed service layer for the Generic Spring Rest.
 *
 * Created on 13/03/2015.
 *
 * @author moses.mansaray
 */
public interface RestService {

  /**
   * Service calls to GET Restful resource using the GenericRestClient
   * @return String representation the response object
   * @throws GenericRestTemplateException is can be thrown with detailed message in the exception
   */
  public String getRestfulResource() throws GenericRestTemplateException;

  /**
   * Service calls to GET Restful resource using the GenericRestClient
   * <p/>
   * This will sets HTTP Headers and Params before making call
   * @return String representation the response object
   * @throws GenericRestTemplateException is can be thrown with detailed message in the exception
   */
  public String getClientResponseWithParamsAndHeaders() throws GenericRestTemplateException;

  /**
   * Makes Exposes Service for POST calls to a restful API and returns the response
   *
   * @param modelToPost the model to pass with the Rest call
   * @param <T> return type is Generic
   * @return String representation the response object
   * @throws GenericRestTemplateException is can be thrown with detailed message in the exception
   */
  public <T> String getClientPostResponse(T modelToPost) throws GenericRestTemplateException;

  /**
   * Service calls to DELETE a Restful resource using the GenericRestClient
   * @throws GenericRestTemplateException
   */
  public String getClientDeleteResponse() throws GenericRestTemplateException;

  /**
   * Service calls to PUT a Restful resource Model using the GenericRestClient
   *
   * @param modelToPut Model to PUT
   * @param <T> Generic type
   * @throws GenericRestTemplateException
   */
  public <T> String getClientPutResponse(T modelToPut) throws GenericRestTemplateException;

}
