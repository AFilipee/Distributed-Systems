package com.forkexec.rst.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.ArrayList; 
import java.util.List;

import com.forkexec.rst.ws.*;

/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {

	public int orderId = 0;
	public Map<String, Menu>       menusList = new ConcurrentHashMap<>();
	public Map<String, Integer> menusListQtt = new ConcurrentHashMap<>();

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Restaurant() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Restaurant INSTANCE = new Restaurant();
	}

	public static synchronized Restaurant getInstance() {
		return SingletonHolder.INSTANCE;
	}


	//store the initial menus
	public void init(List<MenuInit> initialMenus){
		for (MenuInit m : initialMenus) {
			String strID = m.getMenu().getId().getId();
			menusList.put(strID, m.getMenu());
			menusListQtt.put(strID, m.getQuantity());
		}
	}

	//clear menus list
	public void reset(){
        menusList.clear();
	}
	

	// Main operations -------------------------------------------------------

	public Boolean existsMenu(MenuId menuId){
		return (menusList.get(menuId) != null);
	}

	//returns a menu based on its ID
	public Menu getMenuFromID(MenuId menuId) throws NoSuchMenu_Exception{
		if (existsMenu(menuId)){
			return menusList.get(menuId);
		}
		else{
			throw new NoSuchMenu_Exception("There's currently no menu with such ID.");
		}
	}

	//returns a list of menus that matches a txt description
	public List<Menu> searchMenusFromTxt(String dTxt){

		List<Menu> listOfMenus = new ArrayList<Menu>(); 
		for (Map.Entry<String, Menu> m : menusList.entrySet()) {
			Menu menu = m.getValue();
			if (menu.getEntree() ==  dTxt || menu.getPlate() == dTxt ||  menu.getDessert() == dTxt){
				listOfMenus.add(menu);
			}
		}
		return listOfMenus;
	}

	public MenuOrder orderMenuFromId(MenuId mID, int qttity) throws CantFulfillOrder_Exception, NoSuchMenu_Exception{
		int currQtt = menusListQtt.get(mID.getId());
		if (currQtt - qttity >= 0){
			if(existsMenu(mID)){
				menusListQtt.put(mID.getId(), currQtt - qttity);
				MenuOrder order         = new MenuOrder();		//create MenuOrder
				MenuOrderId menuOrderId = new MenuOrderId();	//create MenuOrderId to set on our MenuOrder
				menuOrderId.setId(Integer.toString(orderId));	//set
				order.setId(menuOrderId);					 	//set
				order.setMenuId(mID);							//set
				order.setMenuQuantity(qttity);					//set
				orderId++;										//increments order id
				return order;
			}
			else{ throw new NoSuchMenu_Exception("There's currently no menu with such ID."); }
		}
		else{ throw new CantFulfillOrder_Exception("There is not enough in stock"); }
	}
}
