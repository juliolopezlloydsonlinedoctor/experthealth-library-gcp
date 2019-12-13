package com.experthealth.configuration;

/**
 * Enums to hold strict certificates names to External Restful Resource Environment.
 * <p/>
 * Only certificates names mapping to existing external server environment will be exposed here.
 * E.g MOHC Green environment for development
 *
 * Created on 12/03/2015.
 *
 * @author moses.mansaray
 */
public enum RemoteRestClientServerEnum {
  MOHC_GREEN,
  MOHC_BLACK,
  MOHC_PREPRODUCTION,
  MOHC_PRODUCTION,
  EH_MICROSERVICE_MOCKS

}
