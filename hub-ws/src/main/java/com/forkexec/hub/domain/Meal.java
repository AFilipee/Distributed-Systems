package com.forkexec.hub.domain;

public class Meal {
	protected MealId id;
    protected String entree;
    protected String plate;
    protected String dessert;
    protected int price;
    protected int preparationTime;
    
    public Meal(MealId mId) {
    	this.id = mId;
    }

    public MealId getId() {
        return id;
    }

    public void setId(MealId value) {
        this.id = value;
    }

    public String getEntree() {
        return entree;
    }

    public void setEntree(String value) {
        this.entree = value;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String value) {
        this.plate = value;
    }

    public String getDessert() {
        return dessert;
    }


    public void setDessert(String value) {
        this.dessert = value;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int value) {
        this.price = value;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int value) {
        this.preparationTime = value;
    }
}
