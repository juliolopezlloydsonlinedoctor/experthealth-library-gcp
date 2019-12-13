package com.experthealth.exceptions;

/**
 * Generic Exception to be customized further by  calling classes with relevant message
 *
 * Created on 13/03/2015.
 *
 * @author moses.mansaray
 */
public class GenericRestTemplateException extends Exception {

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the {@link #getMessage()} method.
   */
  public GenericRestTemplateException(String message) {
    super(message);
  }
}
