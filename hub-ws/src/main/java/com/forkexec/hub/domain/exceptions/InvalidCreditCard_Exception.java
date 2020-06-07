
package com.forkexec.hub.domain.exceptions;


public class InvalidCreditCard_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public InvalidCreditCard_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public InvalidCreditCard_Exception(String message, Throwable cause) {
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