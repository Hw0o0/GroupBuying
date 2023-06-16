package com.dnlab.groupbuying.Retrofit.product;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;

public class ProductImformation implements Serializable {
    @SerializedName("productAddress")
    private String productAddress;

    @SerializedName("productName")
    private String productName;

    @SerializedName("eventContent")
    private String eventContent;

    @SerializedName("event1")
    private String event1;

    @SerializedName("event2")
    private String event2;

    @SerializedName("personCount")
    private String personCount;

    @SerializedName("productPrice")
    private String productPrice;



    public String getProductAddress() {
        return productAddress;
    }

    public void setProductAddress(String productAddress) {
        this.productAddress = productAddress;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getEvent1() {
        return event1;
    }

    public void setEvent1(String event1) {
        this.event1 = event1;
    }

    public String getEvent2() {
        return event2;
    }

    public void setEvent2(String event2) {
        this.event2 = event2;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }


    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }


}
