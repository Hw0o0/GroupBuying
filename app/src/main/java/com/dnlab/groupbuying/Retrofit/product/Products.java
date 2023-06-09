package com.dnlab.groupbuying.Retrofit.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {
    @SerializedName("productArray")
    private List<ProductImformation> productArray;

    public Products() {
    }

    public List<ProductImformation> getProductArray() {
        return productArray;
    }

    public void setProductArray(List<ProductImformation> productArray) {
        this.productArray = productArray;
    }
}
