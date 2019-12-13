package com.experthealth.client;

import com.experthealth.configuration.RemoteRestClientServerEnum;
import com.experthealth.configuration.RestClientProperties;
import com.experthealth.configuration.RestClientPropertiesDecorator;
import com.experthealth.exceptions.CustomResponseErrorHandler;
import com.experthealth.exceptions.GenericRestTemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.util.Map;

/**
 * Generic Rest Template Strategy.
 * <p/>
 * This implements the default contract RestService Interface.
 *
 * Created on 05/03/2015.
 *
 * @author moses.mansaray
 */
public class GenericRestClient implements RestClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(GenericRestClient.class);
  private static final String STARTING_CONFIGURATION = "Starting configurations to make Restful calls call";
  private static final String STARTING_CONFIGURATIONS_WITH_SSL = "Starting configurations with SSL";
  private static final String STARTING_CONFIGURATIONS_WITHOUT_SSL = "Starting configurations without SSL";
  private static final String MAKING_CALL_TO_RESTFUL_RESOURCE_WITH_URL_OF = "Making call to restful resource with url of";
  private static final String CREATING_KEY_STORE_ENCRYPTION_TYPE = "creating KeyStore EncryptionType";
  private static final String SSL_CONFIGURATION_SUCCESSFUL = "Configuration of configureSSLStrategy and SSLEncryptionProtocol was successful";

  //paths to Certificates
  private static final String MOHC_PRODUCTION_CERT_PATH = "certificates/cms.drthom.com.p12";
  private static final String MOHC_PRE_PRODUCTION_CERT_PATH = "certificates/coloured.p12";
  private static final String MOHC_GREEN_CERT_PATH = "certificates/website.drthom.com.p12";
  private static final String MOHC_BLACK_CERT_PATH = "certificates/website.drthom.com.p12";

  @Autowired
  private RestTemplate restTemplate;

  //TBR - deliberately not injecting this as it through cast exception on the CMS project
  private CustomResponseErrorHandler customResponseErrorHandler;

  private SSLContext sslContext;
  private final RestClientPropertiesDecorator properties;

  public GenericRestClient(RestClientProperties restClientProperties){
    this.properties = (RestClientPropertiesDecorator)restClientProperties;

  }
  
  /**
   * {@inheritDoc}
   */
  public ResponseEntity<String> makeRequest(String url, Map<String, String> parameters, HttpMethod httpMethod, HttpEntity<Object> requestEntity) throws GenericRestTemplateException {
	  initializeConfigurations();
	  ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, String.class, parameters);
	  return responseEntity;
  }
  
  public void setExternalTemplate(RestTemplate restTemplate) {
	  this.restTemplate = restTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public String getClientResponse() throws GenericRestTemplateException {
    initializeConfigurations();
    String forObject = restTemplate.getForObject(getProperties().getUrl(), String.class);
    verifyStatusForRestTemplateErrors();
    return forObject;

  }

  /**
   * {@inheritDoc}
   */
  public String getClientResponseWithParamsAndHeaders() throws GenericRestTemplateException {
    initializeConfigurations();
    Map<String, String> parameters = getProperties().getParameters();
    HttpEntity<Object> requestEntity = new HttpEntity<Object>("", getHttpHeaders());
    ResponseEntity<String> responseEntity = restTemplate.exchange(getProperties().getUrl(), HttpMethod.GET, requestEntity, String.class, parameters);

    verifyStatusForRestTemplateErrors();
    return responseEntity.toString();

  }

  /**
   * {@inheritDoc}
   */
  public <T> String getClientPostResponse(T modelToPost) throws GenericRestTemplateException {
    initializeConfigurations();
    Map<String, String> parameters = getProperties().getParameters();
    HttpEntity<Object> requestEntity = new HttpEntity<Object>(modelToPost, getHttpHeaders());
    ResponseEntity<String> responseEntity = restTemplate.exchange(getProperties().getUrl(),
            HttpMethod.POST, requestEntity, String.class, parameters);

    return responseEntity.toString();

  }

  /**
   * {@inheritDoc}
   */
  public <T> String getClientPutResponse(T modelToPut) throws GenericRestTemplateException {
    initializeConfigurations();
    Map<String, String> parameters = getProperties().getParameters();
    HttpEntity<Object> requestEntity = new HttpEntity<Object>(modelToPut, getHttpHeaders());
    ResponseEntity<String> responseEntity = restTemplate.exchange(getProperties().getUrl(),
            HttpMethod.PUT, requestEntity, String.class, parameters);

    return responseEntity.toString();
  }

  /**
   * {@inheritDoc}
   */
  public String getClientDeleteResponse() throws GenericRestTemplateException {
    initializeConfigurations();
    HttpEntity<Object> requestEntity = new HttpEntity<Object>(null, getHttpHeaders());

    ResponseEntity<String> responseEntity =
            restTemplate.exchange(getProperties().getUrl(), HttpMethod.DELETE, requestEntity,
                    String.class, getProperties().getParameters());

    return responseEntity.toString();
  }

  /**
   * Sets up the HttpsURLConnection its SSLContext.
   *
   * @throws GenericRestTemplateException
   */
  private void initializeConfigurations() throws GenericRestTemplateException {
    LOGGER.info(STARTING_CONFIGURATION);
    if(configureSSLStrategy()){
      HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }
    configureRestErrorHandler();
    LOGGER.debug(MAKING_CALL_TO_RESTFUL_RESOURCE_WITH_URL_OF + " : ", getProperties().getUrl());

  }

  /**
   * Verify the Status For RestTemplate Errors.
   *
   * @throws GenericRestTemplateException if HTTP Status is not 200 (ok).
   */
  private void verifyStatusForRestTemplateErrors() throws GenericRestTemplateException {
    if (isErrorHandlerInstantiated(customResponseErrorHandler)
            && customResponseErrorHandler.getStatus() != HttpStatus.OK){
      String description = "Code : " + customResponseErrorHandler.getStatus() + " Message : "
              + customResponseErrorHandler.getStatusMessage();
      customResponseErrorHandler.clearStatus();
      throw new GenericRestTemplateException("Rest Template Error : " + description);
    }

  }

  private boolean isErrorHandlerInstantiated(CustomResponseErrorHandler errorHandler) {
    return errorHandler != null && errorHandler.getStatus() != null;

  }

  private void configureRestErrorHandler() {
    this.customResponseErrorHandler = new CustomResponseErrorHandler();
    this.restTemplate.setErrorHandler(customResponseErrorHandler);

  }

  /**
   * {@inheritDoc}
   */
  public boolean configureSSLStrategy() throws GenericRestTemplateException {
    boolean isConfigured = false;
    if(isSSLRequired()){
      KeyStore certificatesKeyStore = getCertificatesKeyStore();
      sslContext = getInitializeSSLContext(certificatesKeyStore);
      isConfigured = isConfigured();
    }
    return isConfigured;

  }

  /**
   * does checks based on the RestClientProperties properties sets for.
   *
   * @return true if SSL Configurations is needed.
   */
  private boolean isSSLRequired() {
    boolean isSSLRequired = false;
    if (!getProperties().getSslEncryptionProtocol().isEmpty() && !getProperties().getSslKeyStoreType().isEmpty()){
      LOGGER.info(STARTING_CONFIGURATIONS_WITH_SSL);
      isSSLRequired = true;
    }else{
      LOGGER.info(STARTING_CONFIGURATIONS_WITHOUT_SSL);
    }
    return isSSLRequired;

  }

  protected KeyStore getCertificatesKeyStore() throws GenericRestTemplateException {
    Resource resource = getResourceCertificateForEnvironment();
    KeyStore clientStore = null;
    try {
      LOGGER.debug(CREATING_KEY_STORE_ENCRYPTION_TYPE + ": " + getProperties().getSslKeyStoreType());
      clientStore = KeyStore.getInstance(getProperties().getSslKeyStoreType());
      clientStore.load(resource.getInputStream(), getProperties().getPassword().toCharArray());
    } catch (KeyStoreException e) {
      throwCustomException(e);
    }  catch (Exception e) {
      throwCustomException(e);
    }
    return clientStore;

  }

  protected SSLContext getInitializeSSLContext(KeyStore certificatesKeyStore) throws GenericRestTemplateException {
    RestClientPropertiesDecorator clientProperties = getProperties();
    KeyManagerFactory kmf;
    KeyManager[] keyManagers;
    try {
      kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(certificatesKeyStore, clientProperties.getPassword().toCharArray());
      keyManagers = kmf.getKeyManagers();

      sslContext = SSLContext.getInstance(clientProperties.getSslEncryptionProtocol());
      sslContext.init(keyManagers, null, new SecureRandom());
    } catch (Exception e) {
      throwCustomException(e);
    }
    return sslContext;

  }

  /**
   * Verification method to check that the sslContext is configured with the correct expected protocol.
   * <p/>
   * This is to ensure that nothing fails silently under the hood.
   *
   * @return true if sslContext is configured correctly
   */
  private boolean isConfigured() {
    boolean isSuccessfullyConfigured = false;
    String sslEncryptionProtocol = getProperties().getSslEncryptionProtocol();
    if(sslContext.getProtocol().equalsIgnoreCase(sslEncryptionProtocol)){
      isSuccessfullyConfigured = true;
      LOGGER.debug(SSL_CONFIGURATION_SUCCESSFUL);
    }
    return isSuccessfullyConfigured;

  }

  private Resource getResourceCertificateForEnvironment() {
    Resource resource = null;
    RemoteRestClientServerEnum remoteRestClientServerEnum =
            RemoteRestClientServerEnum.valueOf(getProperties().getEnvironmentName());
    switch (remoteRestClientServerEnum){
      case MOHC_PRODUCTION:
        resource = new DefaultResourceLoader().getResource(MOHC_PRODUCTION_CERT_PATH);
        break;
      case MOHC_PREPRODUCTION:
        resource = new DefaultResourceLoader().getResource(MOHC_PRE_PRODUCTION_CERT_PATH);
        break;
      case MOHC_GREEN:
        resource = new DefaultResourceLoader().getResource(MOHC_GREEN_CERT_PATH);
        break;
      case MOHC_BLACK:
        resource = new DefaultResourceLoader().getResource(MOHC_BLACK_CERT_PATH);
        break;
    }
    return resource;

  }

  /**
   * get the injected RestClientPropertiesDecorator for the instance of this class
   * @return the RestClientPropertiesDecorator
   */
  public RestClientPropertiesDecorator getProperties() {
    return properties;
  }

  private HttpHeaders getHttpHeaders() {
    Map<String, String> headersParamMap = getProperties().getHeaders();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Accept", "*/*");

    for (Map.Entry<String, String> entry : headersParamMap.entrySet()){
      headers.add(entry.getKey(), entry.getValue());
    }
    return headers;

  }

  /**
   * Logs and throw Exceptions.
   *
   * @param exception the thrown exception.
   *
   * @throws GenericRestTemplateException
   */
  protected void throwCustomException(Exception exception) throws GenericRestTemplateException {
    LOGGER.error("An Exception has occurred Message : " + exception.getMessage());
    throw new GenericRestTemplateException(exception.getMessage());

  }

}

