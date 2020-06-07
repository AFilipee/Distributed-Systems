
package com.forkexec.pts.ws.cli.exceptions;


/**
 * Java class for NotEnoughBalanceFault_Exception complex type.
 * 
 */
@SuppressWarnings("serial")
public class NotEnoughBalanceFault_Exception extends Exception {

    /**
     * 
     * @param message
     */
    public NotEnoughBalanceFault_Exception(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     * @param message
     */
    public NotEnoughBalanceFault_Exception(String message, Throwable cause) {
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
