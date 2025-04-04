package com.walhalla.admin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonParser {

    private static final String FIELD_LIP = "LIP";
    private static final String FIELD_APP = "app";
    private static final String FIELD_APP_STAT = "app_stat";
    private static final String FIELD_AUTH = "auth";
    private static final String FIELD_BLOCKED = "blocked";
    private static final String FIELD_COUNTRY = "country";

    private static final String FIELD_ENTITY = "entity";
    private static final String FIELD_SO = "so";
    private static final String FIELD_TIME_ZONE_NUMBER = "time_zone_number";

    private static final String FIELD_VPN = "vpn";

//    public static void parseJson(String jsonString) {
//        Gson gson = new Gson();
//        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
//        JsonObject bot = jsonObject.getAsJsonObject(FIELD_C07726E98E850084);
//
//        String lip = bot.get(FIELD_LIP).getAsString();
//        JsonElement appElement = bot.get(FIELD_APP);
//        String[] appArray = gson.fromJson(appElement, String[].class);
//        String appStat = bot.get(FIELD_APP_STAT).getAsString();
//        boolean blocked = bot.get(FIELD_BLOCKED).getAsBoolean();
//        String country = bot.get(FIELD_COUNTRY).getAsString();
//        String createAt = bot.get(FIELD_CREATE_AT).getAsString();
//
//        JsonObject authObject = bot.getAsJsonObject(FIELD_AUTH);
//        String authKey = authObject.keySet().iterator().next();
//        JsonElement authElement = authObject.get(authKey);
//        String[] authArray = gson.fromJson(authElement, String[].class);
//
//        JsonObject entityObject = bot.getAsJsonObject(FIELD_ENTITY);
//        String entityAS = entityObject.get("as").getAsString();
//        String entityCity = entityObject.get("city").getAsString();
//        String entityCountry = entityObject.get("country").getAsString();
//        String entityCountryCode = entityObject.get("countryCode").getAsString();
//        String entityIsp = entityObject.get("isp").getAsString();
//        double entityLat = entityObject.get("lat").getAsDouble();
//        double entityLon = entityObject.get("lon").getAsDouble();
//        String entityOrg = entityObject.get("org").getAsString();
//        String entityQuery = entityObject.get("query").getAsString();
//        String entityRegion = entityObject.get("region").getAsString();
//        String entityRegionName = entityObject.get("regionName").getAsString();
//        String entityStatus = entityObject.get("status").getAsString();
//        String entityTimezone = entityObject.get("timezone").getAsString();
//        String entityZip = entityObject.get("zip").getAsString();
//
//        JsonObject soObject = bot.getAsJsonObject(FIELD_SO);
//        String soBoard = soObject.get("BOARD").getAsString();
//        String soBrand = soObject.get("BRAND").getAsString();
//        String soGuid = soObject.get("GUID").getAsString();
//        String soManufacturer = soObject.get("MANUFACTURER").getAsString();
//        String soModel = soObject.get("MODEL").getAsString();
//        String soOs = soObject.get("OS").getAsString();
//        String soUaWv = soObject.get("UA_WV").getAsString();
//        String soUserAgent = soObject.get("USER_AGENT").getAsString();
//        String soW = soObject.get("w").getAsString();
//
//        String timeZoneNumber = bot.get(FIELD_TIME_ZONE_NUMBER).getAsString();
//        String updateAt = bot.get(FIELD_UPDATE_AT).getAsString();
//        boolean vpn = bot.get(FIELD_VPN).getAsBoolean();
//
//        // Выводим полученные значения на экран
//        System.out.println("LIP: " + lip);
//        System.out.print("app: ");
//        for (String app : appArray) {
//            System.out.print(app + ", ");
//        }
//        System.out.println("\napp_stat: " + appStat);
//        System.out.print("auth: ");
//        for (String auth : authArray) {
//            System.out.print(auth + ", ");
//        }
//        System.out.println("\nblocked: " + blocked);
//        System.out.println("country: " + country);
//        System.out.println("create_at: " + createAt);
//
//        // и так далее...
//
//    }

}
