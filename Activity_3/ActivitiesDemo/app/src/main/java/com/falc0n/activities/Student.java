package com.falc0n.activities;

import java.io.Serializable;

/**
 * Created by fAlc0n on 11/3/16.
 */

public class Student implements Serializable {
    private String name, email, department;
    private int mood;

    public Student(String name, String email, String department, int mood) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.mood = mood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }
}
