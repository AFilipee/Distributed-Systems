
package com.forkexec.hub.domain.exceptions;


public class InvalidFoodId_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public InvalidFoodId_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public InvalidFoodId_Exception(String message, Throwable cause) {
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