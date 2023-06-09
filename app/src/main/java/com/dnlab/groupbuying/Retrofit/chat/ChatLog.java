package com.dnlab.groupbuying.Retrofit.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatLog  implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("user")
    private String user;
    @SerializedName("chat")
    private String chat;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
