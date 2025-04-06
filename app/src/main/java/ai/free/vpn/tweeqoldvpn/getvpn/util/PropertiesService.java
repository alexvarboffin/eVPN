package ai.free.vpn.tweeqoldvpn.getvpn.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import ai.free.vpn.tweeqoldvpn.getvpn.App;

public class PropertiesService {


    private static SharedPreferences prefs;
    private static final String DOWNLOADED_DATA_KEY = "downloaded_data";
    private static final String UPLOADED_DATA_KEY = "uploaded_data";
    private static final String AUTOMATIC_SWITCHING = "automaticSwitching";
    private static final String COUNTRY_PRIORITY = "countryPriority";
    private static final String CONNECT_ON_START = "connectOnStart";
    private static final String AUTOMATIC_SWITCHING_SECONDS = "automaticSwitchingSeconds";
    public static final String SELECTED_COUNTRY = "selectedCountry";

    private static final String SHOW_NOTE = "show_note";

    public PropertiesService(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private synchronized static SharedPreferences getPrefs() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        }
        return prefs;
    }

//    public static SharedPreferences getPrefs() {
//        return prefs;
//    }

    public long getDownloaded() {
        return prefs.getLong(DOWNLOADED_DATA_KEY, 0);
    }

    public void setDownloaded(long count) {
        prefs.edit().putLong(DOWNLOADED_DATA_KEY, count).apply();
    }

    public long getUploaded() {
        return prefs.getLong(UPLOADED_DATA_KEY, 0);
    }

    public void setUploaded(long count) {
        prefs.edit().putLong(UPLOADED_DATA_KEY, count).apply();
    }

    public boolean getConnectOnStart() {
        return prefs.getBoolean(CONNECT_ON_START, false);
    }

    public static boolean getAutomaticSwitching() {
        return getPrefs().getBoolean(AUTOMATIC_SWITCHING, true);
    }

    public int getAutomaticSwitchingSeconds() {

        return prefs.getInt(AUTOMATIC_SWITCHING_SECONDS, 40);
    }

    public static boolean getCountryPriority() {
        return getPrefs().getBoolean(COUNTRY_PRIORITY, false);
    }

    public static String getSelectedCountry() {
        return getPrefs().getString(SELECTED_COUNTRY, null);
    }


    public boolean getShowNote() {
        return prefs.getBoolean(SHOW_NOTE, true);
    }

    public void setShowNote(boolean showNote) {
        prefs.edit().putBoolean(SHOW_NOTE, showNote).apply();
    }


}
