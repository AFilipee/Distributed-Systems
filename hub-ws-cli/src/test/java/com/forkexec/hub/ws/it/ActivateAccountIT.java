package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ActivateAccountIT extends BaseIT {
	@AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }
	
	// bad input tests

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserEmptyTest() throws InvalidUserIdFault_Exception {
        client.activateAccount("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserWhitespaceTest() throws InvalidUserIdFault_Exception {
        client.activateAccount("miguel @gmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserTabTest() throws InvalidUserIdFault_Exception {
        client.activateAccount("\tteresa@aol.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
	public void activateUserNewlineTest() throws InvalidUserIdFault_Exception {
		client.activateAccount("\ngaspar@hotmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentUserTest() throws InvalidUserIdFault_Exception {
        client.activateAccount("@hotmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentDomainTest() throws InvalidUserIdFault_Exception {
        client.activateAccount("gaspar@");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentAtSign() throws InvalidUserIdFault_Exception {
        client.activateAccount("gasparhotmail.com");
    }

    // Good input 
    @Test
	public void activateGoodOne() throws InvalidUserIdFault_Exception {
        client.activateAccount("paulo123@hotmail.com");
    }
}
