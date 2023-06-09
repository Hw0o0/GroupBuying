package com.dnlab.groupbuying.Retrofit.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class ChatLog  implements Serializable {
    @SerializedName("user")
    private String user;
    @SerializedName("chat")
    private String chat;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
