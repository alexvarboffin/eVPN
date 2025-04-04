package com.walhalla.vpnapp;

import com.walhalla.vpnapp.model.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.walhalla.vpnapp.repository.Constants;

public class MUtils {


    public interface Callback {
        void onError(String error);

        void onResponse(List<Server> serverList);
    }

    String url_check_ip_batch = "http://ip-api.com/batch";


    public void getIpInfo(final List<Server> serverList, Callback callback) {
        JSONArray jsonArray = new JSONArray();

        for (Server server : serverList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("query", server.getIp());
                jsonObject.put("lang", Locale.getDefault().getLanguage());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AndroidNetworking.post(url_check_ip_batch)
                .addJSONArrayBody(jsonArray)
                .setTag("getIpInfo")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //handleResponse
                        if (setIpInfo(response, serverList)) {
                            callback.onResponse(serverList);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        callback.onError(error.getErrorBody());
                    }
                });
    }

    public boolean setIpInfo(JSONArray response, List<Server> serverList) {
        boolean result = false;

        for (int i = 0; i < response.length(); i++) {
            try {

                // ip ==> ipInfo.get("query").toString()

                JSONObject ipInfo = new JSONObject(response.get(i).toString());
                Server server = serverList.get(i);
                server.city = ipInfo.get(Constants.KEY_CITY).toString();
                server.regionName = ipInfo.get(Constants.KEY_REGION_NAME).toString();
                server.lat = ipInfo.getDouble(Constants.KEY_LAT);
                server.lon = ipInfo.getDouble(Constants.KEY_LON);
                result = true;
            } catch (JSONException e) {
                result = false;
                e.printStackTrace();
            }
        }
        return result;
    }
}
