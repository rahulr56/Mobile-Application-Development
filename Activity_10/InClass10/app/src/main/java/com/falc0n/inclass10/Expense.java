package com.falc0n.inclass10;

import java.io.Serializable;

/**
 * Created by fAlc0n on 11/7/16.
 */

public class Expense implements Serializable{
    String name;
    String amount;
    String category,  addedDate;

    public Expense() {
    }

    public Expense(String name, String amount, String category, String addedDate) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.addedDate = addedDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", category='" + category + '\'' +
                ", addedDate='" + addedDate + '\'' +
                '}';
    }
}
