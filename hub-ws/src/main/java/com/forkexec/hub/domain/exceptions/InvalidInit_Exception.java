
package com.forkexec.hub.domain.exceptions;


public class InvalidInit_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public InvalidInit_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public InvalidInit_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @return
     *     returns eturns the message string 
     */
    public String getMessage() {
        return super.getMessage();
    }
}