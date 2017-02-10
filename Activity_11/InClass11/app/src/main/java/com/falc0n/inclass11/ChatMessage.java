package com.falc0n.inclass11;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fAlc0n on 11/14/16.
 */

public class ChatMessage {
    String text;
    String url;
    String user;
    String time;
    ArrayList<ChatMessage> commentList;
    public ChatMessage() {
        commentList = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<ChatMessage> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<ChatMessage> commentList) {
        this.commentList = commentList;
    }
    public void addToLinkedList(ChatMessage message) {
        commentList.add(message);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", time='" + time + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}
