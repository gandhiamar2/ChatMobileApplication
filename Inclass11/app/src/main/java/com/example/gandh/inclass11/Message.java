package com.example.gandh.inclass11;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gandh on 4/10/2017.
 */

public class Message implements Serializable{

    String message_text, user, time;
    Boolean image=false;

   String unique;



    Map<String,Comment> comments = new HashMap<>();

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
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




}
