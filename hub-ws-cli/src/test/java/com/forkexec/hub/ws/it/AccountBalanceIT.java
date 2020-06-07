package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class AccountBalanceIT extends BaseIT {
	@AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }
	
	// bad input tests

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserEmptyTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserWhitespaceTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("miguel @gmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserTabTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("\tteresa@aol.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
	public void activateUserNewlineTest() throws InvalidUserIdFault_Exception {
		client.accountBalance("\ngaspar@hotmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentUserTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("@hotmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentDomainTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("gaspar@");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentAtSign() throws InvalidUserIdFault_Exception {
        client.accountBalance("gasparhotmail.com");
    }

    // Good input 
    /*@Test
	public void activateGoodOne() throws InvalidUserIdFault_Exception {
        client.accountBalance("paulo123@hotmail.com");
    }*/
}
