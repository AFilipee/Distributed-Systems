
package com.forkexec.hub.domain.exceptions;


public class InvalidText_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public InvalidText_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public InvalidText_Exception(String message, Throwable cause) {
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