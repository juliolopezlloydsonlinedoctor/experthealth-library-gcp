package com.experthealth.configuration;

import java.util.Map;

/**
 * Implements the default configuration property contract RestClientProperties.
 * <p/>
 * Created on 03/03/2015.
 *
 * @author moses.mansaray
 */
public class RestClientPropertiesDecorator implements RestClientProperties {

  private String password;
  private String url;
  private String environmentName;
  private String sslKeyStoreType;
  private String sslEncryptionProtocol;
  private Map<String, String> parameters;
  private Map<String, String> headers;

  /**
   * @deprecated as of v2.03, replaced by the constructor with password, environment, sslKeyStore and sslEncryptionProtocol
   *
   * @param password
   * @param url
   * @param environmentName
   * @param sslKeyStoreType
   * @param sslEncryptionProtocol
   * @param parameters
   * @param headers
   */
  @Deprecated
  public RestClientPropertiesDecorator(String password, String url, String environmentName,
                                       String sslKeyStoreType, String sslEncryptionProtocol,
                                       Map<String, String> parameters, Map<String, String> headers){
    this.password = password;
    this.url = url;
    this.environmentName = environmentName;
    this.sslKeyStoreType = sslKeyStoreType;
    this.sslEncryptionProtocol = sslEncryptionProtocol;
    this.parameters = parameters;
    this.headers = headers;

  }

  /**
   * Constructor which only accepts password, environment, SSLKeystoreType and SSLEncryption protocol.
   * All other fields are sent in dynamically when the service call is made.
   *
   * @param password
   * @param environmentName
   * @param sslKeyStoreType
   * @param sslEncryptionProtocol
   */
  public RestClientPropertiesDecorator(String password, String environmentName, String sslKeyStoreType, String sslEncryptionProtocol) {
    this.password = password;
    this.environmentName = environmentName;
    this.sslKeyStoreType = sslKeyStoreType;
    this.sslEncryptionProtocol = sslEncryptionProtocol;
  }

  public String getPassword() {
    return password;

  }

  public String getUrl() {
    return url;

  }

  public String getEnvironmentName() {
    return environmentName;

  }

  public String getSslKeyStoreType() {
    return sslKeyStoreType;

  }

  public String getSslEncryptionProtocol() {
    return sslEncryptionProtocol;

  }

  public Map<String, String> getParameters() {
    return parameters;

  }

  public Map<String, String> getHeaders() {
    return headers;

  }

}
