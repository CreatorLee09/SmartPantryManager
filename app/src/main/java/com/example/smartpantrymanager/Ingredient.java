package com.example.smartpantrymanager;

public class Ingredient implements java.io.Serializable{ // to pass activities (add and edit)
    private int id; // id to database
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    // constructor
    public Ingredient(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getters
    public int getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public double getQuantity() {
        return quantity;
    }
    public String getUnit() {
        return unit;
    }
    public String getExpiryDate() {
        return expiryDate;
    }

    // Setters for Update mode
    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}

