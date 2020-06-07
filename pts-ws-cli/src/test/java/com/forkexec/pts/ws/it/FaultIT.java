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
public class FaultIT extends BaseIT {

    // initialization and clean-up for each test
    @Before
    public void setUp() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, BadInitFault_Exception, com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception {
        client.ctrlInit(100);
        client.activateUser("pedro321@gmail.com");
    }
    
    // good input test

    @Test
    public void success() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
    	client.sleep(10);
        int initialPoints = client.pointsBalance("pedro321@gmail.com");
        int spentPoints   = client.spendPoints("pedro321@gmail.com", 70);
        int addedPoints   = client.addPoints("pedro321@gmail.com", 200);
        assertEquals(initialPoints - 70 + 200, addedPoints);
    }

}
