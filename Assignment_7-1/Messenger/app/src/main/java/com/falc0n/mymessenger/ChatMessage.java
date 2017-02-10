package com.falc0n.mymessenger;

/**
 * Created by fAlc0n on 11/19/16.
 */

public class ChatMessage {
    private String message,imageUrl,time,userId;

    public ChatMessage(String message, String imageUrl, String time, String userId) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.time = time;
        this.userId = userId;
    }

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", time='" + time + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
