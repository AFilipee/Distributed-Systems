package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class InitUserPointsIT extends BaseIT {
	@AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }
	
	// bad input test
	
	@Test(expected = InvalidInitFault_Exception.class)
    public void activateUserNegativeTest() throws InvalidInitFault_Exception {
        client.ctrlInitUserPoints(-1);
    }
	
	// good input test
	
		@Test
    public void activateUserPositiveTest() throws InvalidInitFault_Exception {
        client.ctrlInitUserPoints(1);
    }
}
