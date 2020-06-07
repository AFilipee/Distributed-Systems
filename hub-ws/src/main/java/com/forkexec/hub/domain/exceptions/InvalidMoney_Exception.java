
package com.forkexec.hub.domain.exceptions;


public class InvalidMoney_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public InvalidMoney_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public InvalidMoney_Exception(String message, Throwable cause) {
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