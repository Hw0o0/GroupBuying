package com.dnlab.groupbuying.Retrofit.chat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatLogs implements Serializable {
    @SerializedName("chatArray")
    private List<ChatLog> chatArray;

    public ChatLogs() {
    }

    public List<ChatLog> getChatArray() {
        return chatArray;
    }

    public void setChatArray(List<ChatLog> chatArray) {
        this.chatArray = chatArray;
    }
}


