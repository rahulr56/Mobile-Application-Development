package com.chatroom.inclass09;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Raghavan on 31-Oct-16.
 */

public class ParseUtil {
    static LoginResponse parseLoginResponse(String in){
        LoginResponse loginResponse = new Gson().fromJson(in,LoginResponse.class);
        return loginResponse;
    }
    static SignUpResponse parseSignUpResponse(String in){
        SignUpResponse signUpResponse = new Gson().fromJson(in,SignUpResponse.class);
        return signUpResponse;
    }
    static Message parseMessageResponse(String in){
        return new Gson().fromJson(in,Message.class);
    }
}
