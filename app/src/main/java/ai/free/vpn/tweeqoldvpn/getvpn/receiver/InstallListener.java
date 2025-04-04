package ai.free.vpn.tweeqoldvpn.getvpn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;


public class InstallListener extends BroadcastReceiver {

    private static final String KEY_REFERRER = "referrer";

    //String webview_url = Resources.getSystem().getString(R.string.app_url);

    @Override
    public void onReceive(Context context, Intent intent) {
        String rawReferrerString = intent.getStringExtra(KEY_REFERRER);
        if (rawReferrerString != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putString(KEY_REFERRER, rawReferrerString).apply();

            if (BuildConfig.DEBUG) {
                Toast.makeText(context, "onReceive: " + rawReferrerString, Toast.LENGTH_LONG).show();
            }
            Bundle params = new Bundle();
            params.putString("utm_source_receiver", rawReferrerString);
            FirebaseAnalytics.getInstance(context).logEvent("referrer_received", params);

//            HttpGetRequest getRequest = new HttpGetRequest();
//            try {
//                String device_id = Util.phoneId(MainActivity.getAppContext().getApplicationContext());
//                String tracker = MainActivity.getAppContext().getString(R.string.app_url) + "?"
//                        + rawReferrerString.replace("%26", "&")
//                        .replace("%3D", "=")
//                        + "&id=" + device_id + "&event=first_install_from_google_play";
//                if (BuildConfig.DEBUG) {
//                    Log.i(TAG, "REF_TRACK: " + tracker);
//                }
//                getRequest.execute(tracker).get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }else {
            if (BuildConfig.DEBUG) {
                Toast.makeText(context, "@@@: " + rawReferrerString, Toast.LENGTH_LONG).show();
            }
        }
    }
}