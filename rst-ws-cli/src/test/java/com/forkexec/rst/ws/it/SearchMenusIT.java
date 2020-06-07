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
public class SearchMenusIT extends BaseIT {
    Menu menu         = new Menu();
    MenuId menuId     = new MenuId();
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
    @Test
    public void success() throws BadTextFault_Exception {
        foundMenus = client.searchMenus("Mousse");
        for (Menu m : foundMenus) {
            assertTrue(m.getDessert().contains("Mousse"));
        }
    }

    // bad input tests

   /*  @Test(expected = BadTextFault_Exception.class)
    public void searchMenusEmptyTest() throws BadTextFault_Exception {
        client.searchMenus("");
    } */

    @Test(expected = BadTextFault_Exception.class)
    public void searchMenusWhitespaceTest() throws BadTextFault_Exception {
        client.searchMenus(" ");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchMenusWithWhitespaceTest() throws BadTextFault_Exception {
        client.searchMenus("mousse de manga");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchMenusWithEndWhitespaceTest() throws BadTextFault_Exception {
        client.searchMenus("mousse ");
    }


}
