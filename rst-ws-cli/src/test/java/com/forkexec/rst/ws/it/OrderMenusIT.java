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
import static org.junit.Assert.assertTrue;


/**
 * Test suite
 */
public class OrderMenusIT extends BaseIT {
    Menu menu         = new Menu();
    MenuId menuId     = new MenuId();
    MenuId menuIdTest = new MenuId();
    MenuInit menuInit = new MenuInit();
    List<MenuInit> initialMenus = new ArrayList<MenuInit>();
    List<Menu> foundMenus   = new ArrayList<Menu>();

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
    //todo
    
    // bad input tests

   /*  @Test(expected = BadMenuIdFault_Exception.class)
    public void orderMenuInvalidIdTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        menuIdTest.setId("3");
        client.orderMenu(menuIdTest, 1);
    } */

    @Test(expected = BadQuantityFault_Exception.class)
    public void orderMenuInvalidQuantityTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        menuIdTest.setId("5");
        client.orderMenu(menuIdTest, -1);
    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void orderMenuNotEnoughQuantityTest() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        menuIdTest.setId("5");
        client.orderMenu(menuIdTest, 350);
    }

}
