package com.experthealth.service;

import com.experthealth.client.GenericRestClient;
import com.experthealth.exceptions.GenericRestTemplateException;

/**
 * Implementation for the Exposed service layer for the Generic Spring Rest
 *
 * Created on 13/03/2015.
 *
 * @author moses.mansaray
 */
public class GenericRestService implements RestService {

  private GenericRestClient genericRestClient;

  public GenericRestService(GenericRestClient genericRestClient){
    this.genericRestClient = genericRestClient;

  }

  /**
   * {@inheritDoc}
   */
  public String getRestfulResource() throws GenericRestTemplateException {
    return genericRestClient.getClientResponse();

  }

  /**
   * Service calls to GET Restful resource using the GenericRestClient
   * <p/>
   * This will sets HTTP Headers and Params before making call
   *
   * @return String representation the response object
   * @throws GenericRestTemplateException is can be thrown with detailed message in the exception
   */
  public String getClientResponseWithParamsAndHeaders() throws GenericRestTemplateException {
    return genericRestClient.getClientResponseWithParamsAndHeaders();
  }

  /**
   * {@inheritDoc}
   */
  public <T> String getClientPostResponse(T modelToPost) throws GenericRestTemplateException {
    return genericRestClient.getClientPostResponse(modelToPost);

  }

  /**
   * {@inheritDoc}
   */
  public String getClientDeleteResponse() throws GenericRestTemplateException {
    return genericRestClient.getClientDeleteResponse();

  }

  /**
   * {@inheritDoc}
   */
  public <T> String getClientPutResponse(T modelToPut) throws GenericRestTemplateException {
    return genericRestClient.getClientPutResponse(modelToPut);

  }

}
