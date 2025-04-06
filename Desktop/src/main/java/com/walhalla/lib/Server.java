package com.walhalla.lib;


public class Server
{


    //Get ipInfo Server info
    //================================

    public String city;
    public String regionName;

    public double lat;
    public double lon;
    public String mPassword;
    public String mUsername;
    public int id;

    //Get ipInfo Server info
    //================================


    public Server() {}

    public String hostName;
    public String configFileName;//ovpn file
    private String ip;
    private String score;
    private String ping;
    private String speed;
    public String countryLong;
    public String countryShort;
    private String numVpnSessions = "0";
    private String uptime;
    private String totalUsers;
    private String totalTraffic;
    private String logType;
    private String operator;
    private String message;
    private String configData;
    private int quality;

    private int type;


    public Server(String hostName) {
        this.hostName = hostName;
    }

    public Server(String hostName, String ip, String score, String ping, String speed, String countryLong, String countryShort, String numVpnSessions, String uptime, String totalUsers, String totalTraffic, String logType, String operator, String message, String configData, int quality, String city, int type, String regionName, double lat, double lon) {
        this.hostName = hostName;
        this.ip = ip;
        this.score = score;
        this.ping = ping;
        this.speed = speed;
        this.countryLong = countryLong;
        this.countryShort = countryShort;
        this.numVpnSessions = numVpnSessions;
        this.uptime = uptime;
        this.totalUsers = totalUsers;
        this.totalTraffic = totalTraffic;
        this.logType = logType;
        this.operator = operator;
        this.message = message;
        this.configData = configData;
        this.quality = quality;
        this.city = city;
        this.type = type;
        this.regionName = regionName;
        this.lat = lat;
        this.lon = lon;
    }

    public Server(String datum, String datum1, String datum2,
                  String datum3, String datum4, String datum5,
                  String datum6, String datum7, String datum8,
                  String datum9, String datum10, String datum11,
                  String datum12, String datum13, String datum14, int type) {
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getCountryLong() {
        return countryLong;
    }

    public void setCountryLong(String countryLong) {
        this.countryLong = countryLong;
    }

    public String getCountryShort() {
        return countryShort;
    }

    public void setCountryShort(String countryShort) {
        this.countryShort = countryShort;
    }

    public String getNumVpnSessions() {
        return numVpnSessions;
    }

    public void setNumVpnSessions(String numVpnSessions) {
        this.numVpnSessions = numVpnSessions;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(String totalUsers) {
        this.totalUsers = totalUsers;
    }

    public String getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(String totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


}
