package com.walhalla.admin.adapter.appitem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Entity implements Serializable {

    @SerializedName("as")
    @Expose
    public String as;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("countryCode")
    @Expose
    public String countryCode;
    @SerializedName("isp")
    @Expose
    public String isp;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("org")
    @Expose
    public String org;
    @SerializedName("query")
    @Expose
    public String query;
    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("regionName")
    @Expose
    public String regionName;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("zip")
    @Expose
    public String zip;

    public Entity() {
    }
}