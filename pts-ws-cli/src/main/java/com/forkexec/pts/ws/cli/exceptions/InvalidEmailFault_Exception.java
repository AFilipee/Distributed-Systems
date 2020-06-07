
package com.forkexec.pts.ws.cli.exceptions;


/**
 * Java class for InvalidEmailFault_Exception complex type.
 * 
 */
@SuppressWarnings("serial")
public class InvalidEmailFault_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public InvalidEmailFault_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public InvalidEmailFault_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @return
     *     returns the message string 
     */
    public String getMessage() {
        return super.getMessage();
    }
}
