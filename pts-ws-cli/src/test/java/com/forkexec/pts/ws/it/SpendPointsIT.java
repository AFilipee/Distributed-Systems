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
import com.forkexec.pts.ws.BadInitFault_Exception;


/**
 * Test suite
 */
public class SpendPointsIT extends BaseIT {

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
        int finalPoints   = client.spendPoints("pedro123@gmail.com", 70);
        assertEquals(initialPoints - 70, finalPoints);
    }


    // bad input tests

    @Test(expected = InvalidPointsFault_Exception.class)
    public void spendPointsNegativeTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("nellyfurtado@sapo.pt.com", -10);
    }

    @Test(expected = InvalidPointsFault_Exception.class)
    public void spendPointsZeroTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("ricardo.1.almeida@gmail.com", 0);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void spendPointsEmptyTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("", 50);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void spendPointsWhitespaceTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("miguel @gmail.com", 100);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void spendPointsTabTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("\tteresa@aol.com", 150);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
	public void spendPointsNewlineTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("\ngaspar@hotmail.com", 200);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void spendPointsInexistentUserTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("@hotmail.com", 250);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void spendPointsInexistentDomainTest() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("gaspar@", 300);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void spendPointsInexistentAtSign() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("gasparhotmail.com", 350);
    }

    
    @Test(expected = NotEnoughBalanceFault_Exception.class)
    public void spendPointsNotEnoughBalance() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.spendPoints("pedro123@gmail.com", 500);
    }
    
    
}
