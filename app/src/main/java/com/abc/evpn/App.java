package com.abc.evpn;

import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.walhalla.ui.DLog;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class App extends MultiDexApplication {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        // Adding an Network Interceptor for Debugging purpose :
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DLog.d("@x@"
                    + Arrays.toString(getApplicationContext().getDataDir().listFiles()));
        }
        DLog.d("@x@xx"+ Arrays.toString(getApplicationContext().getFilesDir().listFiles()));

        for (File file : new File(this.getApplicationInfo().nativeLibraryDir).listFiles()) {
            DLog.d("@x@xx123"+ file.getAbsolutePath());
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static String getResourceString(int resId) {
        return instance.getString(resId);
    }

    public static App getInstance() {
        return instance;
    }

}
