package com.walhalla.admin.adapter.appitem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class So {

    @SerializedName("BOARD")
    @Expose
    public String board;
    @SerializedName("BRAND")
    @Expose
    public String brand;
    @SerializedName("GUID")
    @Expose
    public String guid;
    @SerializedName("MANUFACTURER")
    @Expose
    public String manufacturer;
    @SerializedName("MODEL")
    @Expose
    public String model;
    @SerializedName("OS")
    @Expose
    public String os;
    @SerializedName("UA_WV")
    @Expose
    public String uaWv;
    @SerializedName("USER_AGENT")
    @Expose
    public String userAgent;
    @SerializedName("w")
    @Expose
    public String w;
}
