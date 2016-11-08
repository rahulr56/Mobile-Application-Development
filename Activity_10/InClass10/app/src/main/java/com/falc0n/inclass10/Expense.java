package com.falc0n.inclass10;

import java.io.Serializable;

/**
 * Created by fAlc0n on 11/7/16.
 */

public class Expense implements Serializable{
    String name;
    String amount;
    String category, uid;

    public Expense() {
    }

    public Expense(String name, String amount, String category, String uid) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", category='" + category + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
