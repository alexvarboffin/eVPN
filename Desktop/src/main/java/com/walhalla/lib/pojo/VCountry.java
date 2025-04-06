package com.walhalla.lib.pojo;



public class VCountry {
    public String countryLong;
    public String countryShort;

    public VCountry(String countryLong, String countryShort) {
        this.countryLong = countryLong;
        this.countryShort = countryShort;
    }

    @Override
    public String toString() {
        return "VCountry{" +
                "countryLong='" + countryLong + '\'' +
                ", countryShort='" + countryShort + '\'' +
                '}';
    }
}