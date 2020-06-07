package com.forkexec.hub.domain;

public class MealId {
	protected String restaurantId;
    protected String menuId;
    
    public MealId(String rId, String mId) {
    	this.restaurantId = rId;
    	this.menuId = mId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String value) {
        this.restaurantId = value;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String value) {
        this.menuId = value;
    }
}