package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList; 
import java.util.List;
import static org.junit.Assert.assertEquals;
/*
import com.forkexec.rst.ws.InvalidPointsFault_Exception;
import com.forkexec.rsts.ws.InvalidEmailFault_Exception;
*/


/**
 * Test suite
 */
public class GetMenuIT extends BaseIT {

    Menu menu         = new Menu();
    Menu ourMenu      = new Menu();
    MenuId menuIdTest = new MenuId();
    MenuId menuId     = new MenuId();
    MenuInit menuInit = new MenuInit();
    List<MenuInit> initialMenus = new ArrayList<MenuInit>();

    // initialization and clean-up for each test
    @Before
    public void setUp() throws BadInitFault_Exception {
        menuId.setId("5");

        menu.setId(menuId);
        menu.setEntree("Queijo");
        menu.setPlate("Lasanha");
        menu.setDessert("MousseManga");
        menu.setPrice(20);
        menu.setPreparationTime(45);

        menuInit.setMenu(menu);
        menuInit.setQuantity(300);

        initialMenus.add(menuInit);

        client.ctrlInit(initialMenus);
    }

    @After
	public void tearDown() {
        client.ctrlClear();
    }
    
    // good input tests
 /*    @Test
    public void success() throws BadMenuIdFault_Exception {
        MenuId menuIdTest = new MenuId();
        menuIdTest.setId("5");
        ourMenu = client.getMenu(menuIdTest);

        assertEquals(menu.getId().getId(), ourMenu.getId().getId());
        assertEquals(menu.getEntree(), ourMenu.getEntree());
        assertEquals(menu.getPlate(), ourMenu.getPlate());
        assertEquals(menu.getDessert(), ourMenu.getDessert());
        assertEquals(menu.getPrice(), ourMenu.getPrice());
        assertEquals(menu.getPreparationTime(), ourMenu.getPreparationTime());

    } */


    // bad input tests

    @Test(expected = BadMenuIdFault_Exception.class)
    public void getMenuInvalidIdTest() throws BadMenuIdFault_Exception {
        menuIdTest.setId("3");
        client.getMenu(menuIdTest);
    }    

}
