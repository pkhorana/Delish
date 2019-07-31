package com.example.delish.View;

public class Item {

    private String name;
    private int price;

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setPrice(int amt) {
        this.price = amt;
    }

    public void setName(String name) {
        this.name = name;
    }
}
