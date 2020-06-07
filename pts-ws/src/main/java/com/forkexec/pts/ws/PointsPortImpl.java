package com.forkexec.pts.ws;

import javax.xml.ws.Response;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.forkexec.pts.domain.Points;
import javax.jws.WebService;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
    private final PointsEndpointManager endpointManager;

    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------
    
    @Override 
    public String write(final String email, final int value, final int tag) {
        Points userPoints = Points.getInstance();
        if (!userPoints.emailExists(email))
            userPoints.registerEmail(email);
        userPoints.writeEmail(email,value,tag);
        return "ACK";
    }

    @Override 
    public List<Integer> read(String email) {
        Points userPoints = Points.getInstance();
        if (!userPoints.emailExists(email))
            userPoints.registerEmail(email);
        return userPoints.readEmail(email);
    }

    @Override 
    public void activateUser(String email) throws EmailAlreadyExistsFault_Exception{
        Points userPoints = Points.getInstance();
        if (!userPoints.emailExists(email))
            userPoints.registerEmail(email);
        else
            throwEmailAlreadyExistsFault("Email is already taken");

    }


    // Control operations ----------------------------------------------------
    // TODO
    /** Diagnostic operation to check if service is running. */
    @Override
    public String ctrlPing(String inputMessage) {
	// If no input is received, return a default name.
	if (inputMessage == null || inputMessage.trim().length() == 0)
	    inputMessage = "friend";

	// If the park does not have a name, return a default.
	String wsName = endpointManager.getWsName();
	if (wsName == null || wsName.trim().length() == 0)
	    wsName = "Park";

	// Build a string with a message to return.
	final StringBuilder builder = new StringBuilder();
	builder.append("Hello ").append(inputMessage);
	builder.append(" from ").append(wsName);
	return builder.toString();
    }

    /** Return all variables to default values. */
    @Override
    public void ctrlClear() {
        Points userPoints = Points.getInstance();
        userPoints.reset();
    }

    /** Set variables with specific values. */
    @Override
    public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
        Points userPoints = Points.getInstance();
		userPoints.init(startPoints);
    }

    // Exception helpers -----------------------------------------------------

    /** Helper to throw a new BadInit exception. */
    private void throwBadInit(final String message) throws BadInitFault_Exception {
        final BadInitFault faultInfo = new BadInitFault();
        faultInfo.message = message;
        throw new BadInitFault_Exception(message, faultInfo);
    }

    private void throwEmailAlreadyExistsFault (final String message) throws EmailAlreadyExistsFault_Exception {
        final EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
        faultInfo.message = message;
        throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
    }

}
