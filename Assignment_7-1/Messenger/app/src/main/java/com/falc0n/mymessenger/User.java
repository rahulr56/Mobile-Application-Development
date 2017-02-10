package com.falc0n.mymessenger;

import java.io.Serializable;

/**
 * Created by fAlc0n on 11/17/16.
 */

public class User implements Serializable{
    String firstName,lastName,gender;
    String avatarUrl, userId;
    int newMessage;

    public int getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }

    public User(String firstName, String lastName, String gender, String avatarUrl, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
