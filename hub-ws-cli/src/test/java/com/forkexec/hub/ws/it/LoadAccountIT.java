package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LoadAccountIT extends BaseIT {
	@AfterClass
    public static void oneTimeTearDown() {
        // clear remote service state after all tests
        client.ctrlClear();
    }
	
	// bad input tests

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserEmptyTest()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("", 10, "5461575023302120");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserWhitespaceTest()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("miguel @gmail.com", 10, "5461575023302120");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserTabTest()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("\tteresa@aol.com", 10, "5461575023302120");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
	public void activateUserNewlineTest()
			throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
		client.loadAccount("\ngaspar@hotmail.com", 10, "5461575023302120");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentUserTest()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("@hotmail.com", 10, "5461575023302120");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentDomainTest()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("gaspar@", 10, "5461575023302120");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void activateUserInexistentAtSign()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("gasparhotmail.com", 10, "5461575023302120");
    }
    
    @Test(expected = InvalidMoneyFault_Exception.class)
    public void activateInvalidMoney()
    		throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("paulo123@hotmail.com", 15, "5461575023302120");
    }

    // Good input 
    /*@Test
	public void activateGoodOne()
			throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount("paulo123@hotmail.com", 10, "5461575023302120");
    }*/
}
