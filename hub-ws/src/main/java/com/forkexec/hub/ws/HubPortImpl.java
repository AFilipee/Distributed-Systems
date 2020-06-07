package com.forkexec.hub.ws;

import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import com.forkexec.hub.domain.*;
import com.forkexec.hub.domain.exceptions.*;
import com.forkexec.rst.ws.BadInitFault;
import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import com.forkexec.rst.ws.MenuOrder;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
// import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.exceptions.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.cli.exceptions.NotEnoughBalanceFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType",
            wsdlLocation = "HubService.wsdl",
            name ="HubWebService",
            portName = "HubPort",
            targetNamespace="http://ws.hub.forkexec.com/",
            serviceName = "HubService"
)
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public void activateAccount(String userId) throws InvalidUserIdFault_Exception {
		// Check argument
		checkUserId(userId);

		PointsClient ptsCli = getPointsClient(getPoints());

		try {
			if (ptsCli != null)
				ptsCli.activateUser(userId);
		}
		catch (EmailAlreadyExistsFault_Exception eaefe) {
			throwInvalidUserId(eaefe.getMessage());
		}
		catch (Exception e) {
			throwInvalidUserId(e.getMessage());
		}
	}

	@Override
	public synchronized void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		// TODO Auto-generated method stub
		// Check argument
		Hub hub = Hub.getInstance();
		try {
			hub.checkUserId(userId); 
			int points = hub.convertMoney(moneyToAdd);
			System.out.println(Integer.toString(moneyToAdd));
			// getCCClient(getCC()).subtractMoney(moneyToAdd, creditCardNumber);
			getPointsClient(getPoints()).addPoints(userId, points);
			
		}
		catch (InvalidUserId_Exception iuie) {
			throwInvalidUserId(iuie.getMessage());
		}
		catch (InvalidMoney_Exception ime) {
			throwInvalidMoney(ime.getMessage());
		}
		catch (InvalidPointsFault_Exception ipfe) {
			throw new RuntimeException(ipfe.getMessage());
		}
		catch (com.forkexec.pts.ws.cli.exceptions.InvalidEmailFault_Exception iefe) {
			throwInvalidUserId(iefe.getMessage());
		}
	}
	
	/** Returns lowest price menus first */
	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		return Hub.getInstance().searchDeal(getFoodList(description));
	}
	
	/** Returns lowest preparation time first */
	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		return Hub.getInstance().searchHungry(getFoodList(description));
	}
	
	@Override
	public synchronized void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		// Check arguments
		Hub hub = Hub.getInstance();
		try {
			hub.checkUserId(userId);
			hub.checkRestaurantId(foodId.getRestaurantId());
			hub.checkMenuId(foodId.getMenuId());
			
			if (getFoodList(foodId.getMenuId()).isEmpty())
				throwInvalidFoodId("Food list cannot be empty");
		}
		catch (InvalidUserId_Exception iuie) {
			throwInvalidUserId(iuie.getMessage());
		}
		catch (InvalidFoodId_Exception ifie) {
			throwInvalidFoodId(ifie.getMessage());
		}
		catch (InvalidTextFault_Exception itfe) {
			throwInvalidFoodId("Invalid Text Fault");
		}
		
		if (foodQuantity <= 0)
			throwInvalidFoodQuantity("foodQuantity must be positive");
		
		// Restaurant Client
		List<RestaurantClient> rstClients = getRestaurantClients(getServers(foodId.getRestaurantId()));
		if(rstClients == null || rstClients.isEmpty())
			throwInvalidFoodId("Invalid foodId");
		
		RestaurantClient rstClient = rstClients.get(0);
		if (rstClient == null)
			throwInvalidFoodId("Invalid foodId");
		
		hub.addMealToCart(userId, foodId, foodQuantity);
	}

	@Override
	public synchronized void clearCart(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().clearCart(userId);
		}
		catch (InvalidUserId_Exception iuie) {
			throwInvalidUserId(iuie.getMessage());
		}
	}

	@Override
	public synchronized FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {
		// Check argument
		Hub hub = Hub.getInstance();
		List<FoodOrderItem> items = null;
		try {
			items = hub.checkCart(userId);
		}
		catch (InvalidUserId_Exception iuie) {
			throwInvalidUserId(iuie.getMessage());
		}
		catch (EmptyCart_Exception ece) {
			throwEmptyCart(ece.getMessage());
		}
		
		RestaurantClient rstClient = null;
		FoodOrder foodOrder = new FoodOrder();

		int totalPrice = 0;
		for (FoodOrderItem foItem : items) {
			List<RestaurantClient> rstClients = getRestaurantClients(getServers(foItem.getFoodId().getRestaurantId()));
			
			if (rstClients == null)
				throw new RuntimeException("RestaurantClients missing");
			
			rstClient = rstClients.get(0);
			try {
				totalPrice += foItem.getFoodQuantity() * rstClient.getMenu(buildMenuIdView(foItem.getFoodId())).getPrice();
			}
			catch (BadMenuIdFault_Exception bmife) {
				throw new RuntimeException(bmife.getMessage());
			}
		}
		
		if (totalPrice > accountBalance(userId))
			throwNotEnoughPoints("Not enough points");
		
		for (FoodOrderItem foItem : items) {
			FoodId foodId = foItem.getFoodId();
			
			// Request the restaurant to update the MenuOrder.Quantity
			MenuOrder mnOrder = null;
			try {
				mnOrder = rstClient.orderMenu(buildMenuIdView(foodId), foItem.getFoodQuantity());
			}
			catch (BadMenuIdFault_Exception | BadQuantityFault_Exception | InsufficientQuantityFault_Exception e) {
				try {
					totalPrice -= foItem.getFoodQuantity() * rstClient.getMenu(buildMenuIdView(foItem.getFoodId())).getPrice();
				}
				catch (BadMenuIdFault_Exception bmife) {
					// Already tested, never happens
					throw new RuntimeException(bmife.getMessage());
				}
			}
			if (mnOrder == null) {
				try {
					totalPrice -= foItem.getFoodQuantity() * rstClient.getMenu(buildMenuIdView(foItem.getFoodId())).getPrice();
				}
				catch (BadMenuIdFault_Exception bmife) {
					// Already tested, never happens
					throw new RuntimeException(bmife.getMessage());
				}
			}
		}
		try {
			getPointsClient(getPoints()).spendPoints(userId, totalPrice);
		}
		catch (com.forkexec.pts.ws.cli.exceptions.InvalidEmailFault_Exception iefe) {
			throwInvalidUserId(iefe.getMessage());
		}
		catch (InvalidPointsFault_Exception ipfe) {
			throw new RuntimeException(ipfe.getMessage());
		}
		catch (NotEnoughBalanceFault_Exception nebfe) {
			throwNotEnoughPoints(nebfe.getMessage());
		}
		hub.removeFromCart(userId);
		return foodOrder;
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {
		Hub hub = Hub.getInstance();
		try {
			hub.checkUserId(userId);
			return getPointsClient(getPoints()).pointsBalance(userId);
		}
		catch (InvalidUserId_Exception iuie) {
			throwInvalidUserId(iuie.getMessage());
		}
		catch (com.forkexec.pts.ws.cli.exceptions.InvalidEmailFault_Exception iefe) {
			throwInvalidUserId(iefe.getMessage());
		}
		return 0;
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		
		// Restaurant Client
		List<RestaurantClient> rstClients = getRestaurantClients(getServers(foodId.getRestaurantId()));
		if(rstClients == null || rstClients.isEmpty())
			throwInvalidFoodId("Invalid foodId");
		
		RestaurantClient rstClient = rstClients.get(0);
		if (rstClient == null)
			throwInvalidFoodId("Invalid foodId");
		
		try {
			return buildFoodView(rstClient.getMenu(buildMenuIdView(foodId)), rstClient);
		}
		catch (BadMenuIdFault_Exception e) {
			throwInvalidFoodId("Invalid foodId");
		}
		return null;
	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) {
		return Hub.getInstance().cartContents(userId);
	}
	
	
	// General operations -----------------------------------------------------
	
	private void checkUserId(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().checkUserId(userId);
		}
		catch (InvalidUserId_Exception iuie) {
			throwInvalidUserId(iuie.getMessage());
		}
	}
	
	/** Returns a List of Food for a given input as argument */
	private List<Food> getFoodList(String description) throws InvalidTextFault_Exception {
		if (description == null)
			throwInvalidText("Menu description cannot be null");
		
		if (description.contains(" "))
			throwInvalidText("Menu description cannot contain whitespaces");
		
		List<RestaurantClient> rstClients = getRestaurantClients(getRestaurants());
		List<Food> foodList = new LinkedList<Food>();
		List<Menu> menuList;
		
		for (RestaurantClient rc : rstClients) {
			try {
				menuList= rc.searchMenus(description);
				
				if(menuList != null)
					for (Menu menu : menuList)
						foodList.add(buildFoodView(menu, rc));
			}
			catch (BadTextFault_Exception btfe) {
				throw new RuntimeException(btfe.getMessage());
			}
		}
		return foodList;
	}
	
	private Collection<UDDIRecord> getRestaurants() {
		return getServers("A18_Restaurant%");
	}
	
	private Collection<UDDIRecord> getPoints() {
		return getServers("A18_Points%");
	}
	
	private Collection<UDDIRecord> getCC() {
		return getServers("CC%");		
	}
	
	private Collection<UDDIRecord> getServers(String rstSet) {
		try {
			return this.endpointManager.getUddiNaming().listRecords(rstSet);
		}
		catch (UDDINamingException uddine) {
			throw new RuntimeException(uddine.getMessage());
		}
	}
	
	private List<RestaurantClient> getRestaurantClients(Collection<UDDIRecord> collUDDI) {
		List<RestaurantClient> rstCliList = new LinkedList<RestaurantClient>();
		
		try {
			for (UDDIRecord rec : collUDDI)
				rstCliList.add(new RestaurantClient(rec.getUrl(), rec.getOrgName()));
			return rstCliList;
		}
		catch (RestaurantClientException rce) {
			throw new RuntimeException(rce.getMessage());
		}
	}
	
	private PointsClient getPointsClient(Collection<UDDIRecord> collUDDI) {
		List<PointsClient> ptsCliList = new LinkedList<PointsClient>();
		
		try {
			for (UDDIRecord rec : collUDDI)
				ptsCliList.add(new PointsClient(rec.getUrl(), rec.getOrgName()));

			if (!ptsCliList.isEmpty())
				return ptsCliList.get(0);

			return null;
		}
		catch (PointsClientException pce) {
			throw new RuntimeException(pce.getMessage());
		}
	}
	
	// TODO
	/*private CCClient getCCClient(Collection<UDDIRecord> collUDDI) {
		List<CCClient> ccCliList = new LinkedList<CCClient>();
		
		try {
			for (UDDIRecord rec : collUDDI)
				ccCliList.add(new CCClient(rec.getUrl(), rec.getOrgName()));

			if (!ccCliList.isEmpty())
				return ccCliList.get(0);

			return null;
		}
		catch (CCClientException ccce) {
			cce.printStackTrace();
			return null;
		}
	}*/
	
	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Hub";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);	
		builder.append(" from ").append(wsName);

		List<RestaurantClient> rstCliList = getRestaurantClients(getRestaurants());
		
		for (RestaurantClient rc : rstCliList)
			builder.append("\n").append(rc.ctrlPing(inputMessage));
		
		// builder.append("\n").append(getPointsClient(getPoints()).ctrlPing(inputMessage));

		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		
		List<RestaurantClient> rstClients = getRestaurantClients(getRestaurants());
		for (RestaurantClient rstCli : rstClients)
			rstCli.ctrlClear();
		
		PointsClient pc = getPointsClient(getPoints());
		if (pc != null)
			pc.ctrlClear();
		
		Hub.getInstance().clear();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		if (initialFoods == null)
			throwInvalidInit("initialFoods must not be null");
		if (initialFoods.isEmpty())
			throwInvalidInit("initialFoods must not be empty");
		
		List<MenuInit> send = new LinkedList<MenuInit>();
		Collection<UDDIRecord> rstList = getRestaurants();
		for (UDDIRecord rst : rstList) {
			Iterator<FoodInit> itr = initialFoods.iterator();
			while (itr.hasNext()) {
				FoodInit fi = itr.next();
				if (fi.getFood().getId().getRestaurantId().equals(rst.getOrgName()))
					send.add(buildMenuInit(fi));
				itr.remove();
			}
			try {
				new RestaurantClient(rst.getUrl(), rst.getOrgName()).ctrlInit(send);
			}
			catch (BadInitFault_Exception | RestaurantClientException e) {
				throwInvalidInit(e.getMessage());
			}
		}
	}
	
	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		if (startPoints < 0)
			throwInvalidInit("startPoints must be positive");
		try {
			PointsClient pc =  getPointsClient(getPoints());
			if (pc != null)
				pc.ctrlInit(startPoints);
		}
		catch (com.forkexec.pts.ws.BadInitFault_Exception e) {
			throwInvalidInit(e.getMessage());
		}
	}


	// View helpers ----------------------------------------------------------

	/** Helpers to convert domain object and views. */
	
	private Food buildFoodView(Menu m, RestaurantClient rc) {
		Food f = new Food();
		FoodId fId = new FoodId();
		MenuId mId = m.getId();
		
		fId.setMenuId(mId.getId());
		fId.setRestaurantId(rc.getWsURL());
		
		f.setId(fId);
		f.setEntree(m.getEntree());
		f.setPlate(m.getPlate());
		f.setDessert(m.getDessert());
		f.setPreparationTime(m.getPreparationTime());
		f.setPrice(m.getPreparationTime());
		
		return f;
	}
	
	private MenuId buildMenuIdView(FoodId foodId) {
		MenuId menuId = new MenuId();
		menuId.setId(foodId.getMenuId());
		return menuId;
	}
	
	private Menu buildMenuView(Food food) {
		Menu menu = new Menu();
		menu.setId(buildMenuIdView(food.getId()));
		menu.setEntree(food.getEntree());
		menu.setPlate(food.getPlate());
		menu.setDessert(food.getDessert());
		menu.setPrice(food.getPrice());
		menu.setPreparationTime(food.getPreparationTime());
		return menu;
	}
	
	private MenuInit buildMenuInit(FoodInit fi) {
		MenuInit mi = new MenuInit();
		mi.setMenu(buildMenuView(fi.getFood()));
		mi.setQuantity(fi.getQuantity());
		return mi;
	}
	
	private FoodOrderItem buildFoodOrderItemView(MenuOrder mnOrder, int qty) {
		FoodId foodId = new FoodId();
		foodId.setMenuId(mnOrder.getMenuId().getId());
		FoodOrderItem foodOrdItm = new FoodOrderItem();
		foodOrdItm.setFoodId(foodId);
		foodOrdItm.setFoodQuantity(qty);
		return foodOrdItm;
	}

	
	// Exception helpers -----------------------------------------------------
	
	/** Helper throw a new InvalidInit exception */
	private void throwInvalidInit(final String message) throws InvalidInitFault_Exception {
		InvalidInitFault faultInfo = new InvalidInitFault();
		faultInfo.setMessage(message);
		throw new InvalidInitFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new EmptyCart exception */
	private void throwEmptyCart(final String message) throws EmptyCartFault_Exception {
		EmptyCartFault faultInfo = new EmptyCartFault();
		faultInfo.setMessage(message);
		throw new EmptyCartFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new NotEnoughPoints exception */
	private void throwNotEnoughPoints(final String message) throws NotEnoughPointsFault_Exception {
		NotEnoughPointsFault faultInfo = new NotEnoughPointsFault();
		faultInfo.setMessage(message);
		throw new NotEnoughPointsFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new InvalidCreditCard exception */
	private void throwInvalidCreditCard(final String message) throws InvalidCreditCardFault_Exception {
		InvalidCreditCardFault faultInfo = new InvalidCreditCardFault();
		faultInfo.setMessage(message);
		throw new InvalidCreditCardFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new InvalidMoney exception */
	private void throwInvalidMoney(final String message) throws InvalidMoneyFault_Exception {
		InvalidMoneyFault faultInfo = new InvalidMoneyFault();
		faultInfo.setMessage(message);
		throw new InvalidMoneyFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new InvalidFoodId exception */
	private void throwInvalidFoodId(final String message) throws InvalidFoodIdFault_Exception {
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.setMessage(message);
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new FoodQuantity exception */
	private void throwInvalidFoodQuantity(final String message) throws InvalidFoodQuantityFault_Exception {
		InvalidFoodQuantityFault faultInfo = new InvalidFoodQuantityFault();
		faultInfo.setMessage(message);
		throw new InvalidFoodQuantityFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new InvalidUserId exception */
	private void throwInvalidUserId(final String message) throws InvalidUserIdFault_Exception {
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.setMessage(message);
		throw new InvalidUserIdFault_Exception(message, faultInfo);
	}
	
	/** Helper throw a new InvalidText exception */
	private void throwInvalidText(final String message) throws InvalidTextFault_Exception {
		InvalidTextFault faultInfo = new InvalidTextFault();
		faultInfo.setMessage(message);
		throw new InvalidTextFault_Exception(message, faultInfo);
	}
}