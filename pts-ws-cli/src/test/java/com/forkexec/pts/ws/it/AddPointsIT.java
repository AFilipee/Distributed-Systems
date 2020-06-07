package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.forkexec.pts.ws.cli.exceptions.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.cli.exceptions.NotEnoughBalanceFault_Exception;
import com.forkexec.pts.ws.cli.exceptions.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.exceptions.EmailAlreadyExistsFault_Exception;

/**
 * Test suite
 */
public class AddPointsIT extends BaseIT {

    // initialization and clean-up for each test
    @Before
    public void setUp() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, BadInitFault_Exception, com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception {
        client.ctrlInit(100);
        client.activateUser("pedro123@gmail.com");
    }

    @After
	public void tearDown() {
        client.ctrlClear();
    }
    
    // good input tests

   @Test
    public void success() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        int initialPoints = client.pointsBalance("pedro123@gmail.com");
        int finalPoints   = client.addPoints("pedro123@gmail.com", 200);
        assertEquals(initialPoints + 200, finalPoints);
    }


    // bad input tests
    
    @Test(expected = InvalidPointsFault_Exception.class)
    public void addPointsNegativeTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception{
        client.addPoints("nellyfurtado@sapo.pt.com", -10);
    }

    @Test(expected = InvalidPointsFault_Exception.class)
    public void addPointsZeroTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception{
        client.addPoints("ricardo.1.almeida@gmail.com", 0);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void addPointsEmptyTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception{
        client.addPoints("", 50);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void addPointsWhitespaceTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception{
        client.addPoints("miguel @gmail.com", 100);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void addPointsTabTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception{
        client.addPoints("\tteresa@aol.com", 150);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
	public void addPointsNewlineTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception{
		client.addPoints("\ngaspar@hotmail.com", 200);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void addPointsInexistentUserTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        client.addPoints("@hotmail.com", 250);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void addPointsInexistentDomainTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        client.addPoints("gaspar@", 300);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void addPointsInexistentAtSign() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        client.addPoints("gasparhotmail.com", 350);
    }

}
