package ai.free.vpn.tweeqoldvpn.getvpn;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.database.FirebaseDatabase;
import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.domain.repository.from_internet.AdvertConfig;
import com.walhalla.ui.DLog;
import com.walhalla.vpnapp.model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class App extends MultiDexApplication {


    public static List<String> list = new ArrayList<>();

    static {
        list.add(AdRequest.DEVICE_ID_EMULATOR);
        list.add("A8A2F7804653E219880030864C1F32E4");
        list.add("5D5A89BC6372A49242D138B9AC352894");
        list.add("B946D111C6F350722C642CBFC64ED0B3");
        list.add("B946D111C6F350722C642CBFC64ED0B3");
    }

    public static Server connectedServer = null;
    public static AdvertAdmobRepository repository;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        try {
//            FirebaseApp.initializeApp(this);
//        } catch (Exception e) {
//            DLog.handleException(e);
//        }
        //MobileAds.initialize(this, getString(R.string.app_id));

//        MobileAds.initialize(this, initializationStatus -> {
//            Map<String, AdapterStatus> map = initializationStatus.getAdapterStatusMap();
//            for (Map.Entry<String, AdapterStatus> entry : map.entrySet()) {
//                DLog.d("--> " + entry.getKey() + " " + entry.getValue());
//            }
//        });

//        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        appOpenAdManager = new AppOpenAdManager(getString(R.string.run1));

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            DLog.handleException(e);
        }

        // Adding an Network Interceptor for Debugging purpose :
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            DLog.d("@x@"
//                    + Arrays.toString(getApplicationContext().getDataDir().listFiles()));
//        }
//        DLog.d("@x@xx"+ Arrays.toString(getApplicationContext().getFilesDir().listFiles()));
//
//        for (File file : new File(this.getApplicationInfo().nativeLibraryDir).listFiles()) {
//            DLog.d("@x@xx123"+ file.getAbsolutePath());
//        }

        //OtUtils.bitCheck();

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(list)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(this, initializationStatus -> {
            //getString(R.string.app_id)
        });

        AdvertConfig config = AdvertConfig.newBuilder()
                .setAppId(getString(R.string.app_id))
                .setBannerId(getString(R.string.b1))
                .build();

        repository = AdvertAdmobRepository.getInstance(config);
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

//    public static String getResourceString(int resId) {
//        return instance.getString(resId);
//    }
}
