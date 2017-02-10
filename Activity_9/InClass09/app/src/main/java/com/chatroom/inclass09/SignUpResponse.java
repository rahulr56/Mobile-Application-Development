package com.chatroom.inclass09;

/**
 * Created by Raghavan on 31-Oct-16.
 */

public class SignUpResponse {

    /**
     * status : ok
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
     * userId : 73
     * userEmail : usetrr@test.net
     * userFname : Bob
     * userLname : Smith
     * userRole : USER
     * message : user34@test.net email is not available, choose another email.
     */

    private String status;
    private String token;
    private int userId;
    private String userEmail;
    private String userFname;
    private String userLname;
    private String userRole;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFname() {
        return userFname;
    }

    public void setUserFname(String userFname) {
        this.userFname = userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
