package com.forkexec.hub.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import com.forkexec.hub.domain.exceptions.*;
import com.forkexec.hub.ws.*;


/**
 * Hub
 *
 * A restaurants hub server.
 *
 */
public class Hub {
	
	private Map<String, MealOrder> shoppingCarts = new HashMap<String, MealOrder>();
	
	/** counter applied to MealOrderId.id */
	private int mealOrderCounter;



	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Hub() {
		// Initialization of default values
		mealOrderCounter = 0;
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Hub INSTANCE = new Hub();
	}

	public static synchronized Hub getInstance() {
		return SingletonHolder.INSTANCE;
	}

	
	// Main operations -------------------------------------------------------
	
	public void addMealToCart(String userId, FoodId foodId, int mealQuantity) {
		MealOrder mealOrder = shoppingCarts.get(userId);
		MealId mealId = MealIdCasting(foodId);
		
		// If not first order
		if (mealOrder != null) {
			List<MealOrderItem> items = mealOrder.getItems();
			
			for (MealOrderItem mealOrderItem : items) {
				
				// If already ordered the same item
				if (mealOrderItem.getMealId().equals(mealId.getMenuId())) {
					mealOrderItem.setMealQuantity(mealQuantity + mealOrderItem.getMealQuantity());
					return;
				}
			}
			MealOrderItem item = new MealOrderItem(mealId, mealQuantity);
			mealOrder.items.add(item);
		}
		else {
			MealOrderItem item = new MealOrderItem(mealId, mealQuantity);

			MealOrder fo = new MealOrder(mealOrderCounter++ + " - " + userId);
			fo.items.add(item);
			shoppingCarts.put(userId, fo);
		}
	}
	
	public void clearCart(String userId) throws InvalidUserId_Exception {
		checkUserId(userId);
		
		MealOrder mo = shoppingCarts.get(userId);
		if (mo != null) {
			mo.items.clear();
			shoppingCarts.remove(userId);
		}
	}
	
	public List<FoodOrderItem> cartContents(String userId) {
		MealOrder fo = shoppingCarts.get(userId);
		List<FoodOrderItem> items = new LinkedList<FoodOrderItem>();
		
		for (MealOrderItem item : fo.getItems())
			items.add(FoodOrderItemCasting(item));
		
		return items;
	}

	
	// General operations -----------------------------------------------------
	
	/** Convert money to points */
	public int convertMoney(int money) throws InvalidMoney_Exception {
		switch(money) {
			case 10:
				return 1000;
			case 20:
				return 2100;
			case 30:
				return 3300;
			case 50:
				return 5500;
			default:
				throw new InvalidMoney_Exception("The value is not valid: " + money);
		}
	}
	
	public void removeFromCart(String userId) {
		shoppingCarts.remove(userId);
	}
	
	public void clear() {
		for (MealOrder m : shoppingCarts.values())
			m.items.clear();
		shoppingCarts.clear();
		mealOrderCounter = 0;
	}
	
	public List<Food> searchDeal(List<Food> foodList) throws InvalidTextFault_Exception {
		Collections.sort(foodList, new FoodPriceComparator());
		return foodList;
	}
	
	public List<Food> searchHungry(List<Food> foodList) throws InvalidTextFault_Exception {
		Collections.sort(foodList, new FoodTimeComparator());
		return foodList;
	}
	
	class FoodPriceComparator implements Comparator<Food> {
	    @Override
	    public int compare(Food f1, Food f2) {
	        return f1.getPrice() - f2.getPrice();
	    }
	}
	
	class FoodTimeComparator implements Comparator<Food> {
	    @Override
	    public int compare(Food f1, Food f2) {
	        return f1.getPreparationTime() - f2.getPreparationTime();
	    }
	}
	
	// Argument testers -----------------------------------------------------
	
	public void checkUserId(String userId) throws InvalidUserId_Exception {
		// Check argument
		if (userId == null)
			throw new InvalidUserId_Exception("Invalid email: userId cannot be null");
		
		if (!userId.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"))
			throw new InvalidUserId_Exception("Not a valid email");
	}
	
	public void checkRestaurantId(String rst) throws InvalidFoodId_Exception {
		if (rst == null)
			throw new InvalidFoodId_Exception("restaurantId cannot be null");
		if (rst.isEmpty())
			throw new InvalidFoodId_Exception("restaurantId cannot be empty");
		if (!rst.matches("^[a-zA-Z0-9]*$"))
			throw new InvalidFoodId_Exception("restaurantId must be an alphanumeric string without spaces");
	}
	
	public void checkMenuId(String mn) throws InvalidFoodId_Exception {
		if (mn == null)
			throw new InvalidFoodId_Exception("menuId cannot be null");
		if (mn.isEmpty())
			throw new InvalidFoodId_Exception("menuId cannot be empty");
		if (!mn.matches("^[a-zA-Z0-9]*$"))
			throw new InvalidFoodId_Exception("menuId must be an alphanumeric string without spaces");
	}
	
	public List<FoodOrderItem> checkCart(String userId) throws InvalidUserId_Exception, EmptyCart_Exception {
		checkUserId(userId);
		
		if (shoppingCarts.isEmpty())
			throw new EmptyCart_Exception("Shopping Cart is Empty");
		
		MealOrder mealOrder = shoppingCarts.get(userId);
		
		if (mealOrder == null)
			throw new InvalidUserId_Exception("Invalid User");
		
		List<FoodOrderItem> items = new LinkedList<FoodOrderItem>();
		
		for (MealOrderItem item : mealOrder.getItems())
			items.add(FoodOrderItemCasting(item));
		
		return items;
	}
	
	
	// Domain converters -----------------------------------------------------
	
	public MealId MealIdCasting(FoodId f) {
		return new MealId(f.getRestaurantId(), f.getMenuId());
	}
	
	public FoodOrderItem FoodOrderItemCasting(MealOrderItem mItem) {
		FoodId fId = new FoodId();
		fId.setMenuId(mItem.getMealId().getMenuId());
		fId.setRestaurantId(mItem.getMealId().getRestaurantId());
		
		FoodOrderItem fItem = new FoodOrderItem();
		fItem.setFoodId(fId);
		fItem.setFoodQuantity(mItem.getMealQuantity());
		return fItem;
	}
}