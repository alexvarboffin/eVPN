package com.walhalla.vpnapp.repository;

import com.walhalla.vpnapp.model.Server;

import java.util.List;

public interface ServerRepository {

    String KEY_COUNTRY_SHORT = "countryShort";

    interface DataCallback<T> {
        void successResult(T data);

        void showError(String m);
    }

    void getServersByCountryCode(String country, DataCallback<List<Server>> callback);

    Server getGoodRandomServer(String selectedCountry);

    void update(List<Server> serverList);

    void clearTable();

    void putLine(String line, int type);

    void getUniqueCountries(DataCallback<List<Server>> callback);

    long getCount();

    void setInactive(Server server);

    Server getSimilarServer(String countryLong, String ip);
}
