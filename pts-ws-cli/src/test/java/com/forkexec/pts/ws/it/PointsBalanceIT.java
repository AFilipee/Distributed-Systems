package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.forkexec.pts.ws.cli.exceptions.InvalidEmailFault_Exception;

/**
 * Test suite
 */
public class PointsBalanceIT extends BaseIT {

    @AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }

    // members

    // initialization and clean-up for each test
    @Before
    public void setUp() throws BadInitFault_Exception {
        client.ctrlInit(100);
    }

    @After
	public void tearDown() {
    }
    
    // good input tests

   @Test
   public void success() throws InvalidEmailFault_Exception {
       int initialPoints = client.pointsBalance("pedro123@gmail.com");
       assertEquals(100, initialPoints);
   }

    // bad input tests

    @Test(expected = InvalidEmailFault_Exception.class)
    public void pointsBalanceEmptyTest() throws InvalidEmailFault_Exception {
        client.pointsBalance("");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void pointsBalanceWhitespaceTest() throws InvalidEmailFault_Exception {
        client.pointsBalance("miguel @gmail.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void pointsBalanceTabTest() throws InvalidEmailFault_Exception {
        client.pointsBalance("\tteresa@aol.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
	public void pointsBalanceNewlineTest() throws InvalidEmailFault_Exception {
		client.pointsBalance("\ngaspar@hotmail.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void pointsBalanceInexistentUserTest() throws InvalidEmailFault_Exception {
        client.pointsBalance("@hotmail.com");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void pointsBalanceInexistentDomainTest() throws InvalidEmailFault_Exception {
        client.pointsBalance("gaspar@");
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void pointsBalanceInexistentAtSign()
            throws InvalidEmailFault_Exception {
        client.pointsBalance("gasparhotmail.com");
    }


}
