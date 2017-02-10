package com.cn.expensecalculator;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

/**
 * Created by LibrarayGuest on 9/14/2016.
 */
public class Expense implements Parcelable {
    private String name , category;
    private double amount;


    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri imageUri;

    @Override
    public int describeContents() {
        return 0;
    }

    public Expense(String name, String category, double amount, Date date, Uri imageUri) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", imageUri=" + imageUri +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeDouble(amount);
        dest.writeSerializable(date);
        dest.writeSerializable((Serializable) imageUri.toString());
    }
    public static final Creator<Expense> CREATOR = new Creator<Expense>()
    {
        public Expense createFromParcel(Parcel in){
            return new Expense(in);
        }
        public Expense[] newArray(int size)
        {
            return new Expense[size];
        }
    };
    private Expense ( Parcel in)
    {
        this.name = in.readString();
        this.category = in.readString();
        this.amount = in.readDouble();
        this.date = (Date)in.readSerializable();
        this.imageUri = (Uri.parse((String)in.readSerializable())) ;
    }
}
