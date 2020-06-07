package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.forkexec.pts.ws.cli.exceptions.InvalidEmailFault_Exception;


/**
 * Test suite
 */
public class ActivateUserIT extends BaseIT {

    @AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }

    // members

    // initialization and clean-up for each test
    @Before
    public void setUp() {
    }

    @After
	public void tearDown() {
    }
    
    // bad input tests
    
    @Test(expected = InvalidEmailFault_Exception.class)
    public void activateUserEmptyTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception{
        client.activateUser("");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void activateUserWhitespaceTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception{
        client.activateUser("miguel @gmail.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void activateUserTabTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception{
        client.activateUser("\tteresa@aol.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
	public void activateUserNewlineTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception{
		client.activateUser("\ngaspar@hotmail.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void activateUserInexistentUserTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
        client.activateUser("@hotmail.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void activateUserInexistentDomainTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
        client.activateUser("gaspar@");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void activateUserInexistentAtSign()  throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
        client.activateUser("gasparhotmail.com");
    }
    
    @Test
	public void success() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception{
        client.activateUser("andre156@hotmail.com");
    }
    
    @Test(expected = EmailAlreadyExistsFault_Exception.class)
	public void activateAlreadyRegisteredEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception{
        client.activateUser("paulo123@hotmail.com");
        client.activateUser("paulo123@hotmail.com");
    }

}
