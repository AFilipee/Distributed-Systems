package com.forkexec.hub.domain;

public class MealOrderItem {
	protected MealId mealId;
    protected int mealQuantity;
    
    public MealOrderItem(MealId mId, int mQty) {
    	this.mealId = mId;
    	this.mealQuantity = mQty;
    }

    public MealId getMealId() {
        return mealId;
    }

    public void setMealId(MealId value) {
        this.mealId = value;
    }

    public int getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(int value) {
        this.mealQuantity = value;
    }
}
