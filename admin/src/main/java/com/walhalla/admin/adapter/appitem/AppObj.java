package com.walhalla.admin.adapter.appitem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class AppObj {

    @SerializedName("LIP")
    @Expose
    public String lip;
    @SerializedName("app")
    @Expose
    public List<String> app;
    @SerializedName("app_stat")
    @Expose
    public String appStat;
//    @SerializedName("auth")
//    @Expose
//    public Map<String, List<String>> auth;
    @SerializedName("blocked")
    @Expose
    public Boolean blocked;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("create_at")
    @Expose
    public String createAt;
    @SerializedName("entity")
    @Expose
    public Entity entity;
    @SerializedName("json")
    @Expose
    public String json;
    @SerializedName("lang")
    @Expose
    public String lang;
    @SerializedName("sim_card")
    @Expose
    public Boolean simCard;
    @SerializedName("so")
    @Expose
    public So so;
    @SerializedName("time_zone_number")
    @Expose
    public String timeZoneNumber;
    @SerializedName("update_at")
    @Expose
    public String updateAt;
    @SerializedName("vpn")
    @Expose
    public Boolean vpn;

    public AppObj() {
    }
}
