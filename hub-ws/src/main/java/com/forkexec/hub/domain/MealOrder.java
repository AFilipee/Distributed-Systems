package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.List;

public class MealOrder {
	protected String mealOrderId;
    protected List<MealOrderItem> items;
    
    public MealOrder(String id) {
    	this.mealOrderId = id;
    }

    public String getMealOrderId() {
        return mealOrderId;
    }

    public void setMealOrderId(String m) {
        this.mealOrderId = m;
    }

    public List<MealOrderItem> getItems() {
        if (items == null) {
            items = new ArrayList<MealOrderItem>();
        }
        return this.items;
    }
}
