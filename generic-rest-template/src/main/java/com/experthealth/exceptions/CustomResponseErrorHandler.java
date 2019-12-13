package com.experthealth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Strategy interface used by the {@link org.springframework.web.client.RestTemplate} to determine whether a particular
 * response has an error or not
 *
 * Created on 16/03/2015.
 *
 * @author moses.mansaray
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler{

  private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
  private HttpStatus status;
  private String statusMessage;

  /**
   * Indicates whether the given response has any errors.
   * Implementations will typically inspect
   * the {@link org.springframework.http.client.ClientHttpResponse#getStatusCode() HttpStatus}
   * of the response.
   *
   * @param response the response to inspect
   * @return <code>true</code> if the response has an error; <code>false</code> otherwise
   * @throws java.io.IOException in case of I/O errors
   */
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return errorHandler.hasError(response);

  }

  /**
   * Handles the error in the given response.
   * This method is only called
   * when {@link #hasError(org.springframework.http.client.ClientHttpResponse)} has returned <code>true</code>.
   *
   * @param response the response with the error
   * @throws java.io.IOException in case of I/O errors
   */
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    status = response.getStatusCode();
    statusMessage = response.getStatusText();

  }

  public HttpStatus getStatus() {
    return status;

  }

  public void clearStatus() {
    this.status = HttpStatus.OK;

  }

  public String getStatusMessage() {
    return statusMessage;

  }

}



