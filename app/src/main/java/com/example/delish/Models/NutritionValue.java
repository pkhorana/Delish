package com.example.delish.Models;

public class NutritionValue {
    private Double value;
    private String uom;

    public NutritionValue(Double value, String uom) {
        this.value = value;
        this.uom = uom;
    }

    public double getPureValue() {
        return this.value;
    }

    public String getUOM() {
        return this.uom;
    }

    public String getValue() {
        return "" + this.value + this.uom;
    }
}