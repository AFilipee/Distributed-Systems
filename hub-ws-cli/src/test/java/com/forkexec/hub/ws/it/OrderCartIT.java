package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class OrderCartIT extends BaseIT {
	@AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }
	
	// bad input tests

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserEmptyTest()
    		throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserWhitespaceTest()
    		throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("miguel @gmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserTabTest()
    		throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("\tteresa@aol.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
	public void activateUserNewlineTest()
			throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		client.orderCart("\ngaspar@hotmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentUserTest()
    		throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("@hotmail.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentDomainTest()
    		throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("gaspar@");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentAtSign()
    		throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("gasparhotmail.com");
    }

    // Shopping cart not filled
    @Test(expected = EmptyCartFault_Exception.class)
	public void activateEmptyCart()
			throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("paulo123@hotmail.com");
    }
}
