package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;


import static org.apache.cordova.Cst.Firebase.KEY_DEEP_LINK;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.walhalla.ui.DLog;

import com.walhalla.ui.observer.RateAppModule;

import org.apache.cordova.Cst;
import org.apache.mvp.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.free.vpn.tweeqoldvpn.getvpn.activity.VpnAuthBaseActivity;


public class MainActivity extends VpnAuthBaseActivity {

    private ViewPager2 viewPager2;
    private final List<Integer> indexToPage = new ArrayList<>();

    //private RateAppModule rate0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simle_main);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.mainViewPager);
        viewPager2.setUserInputEnabled(false);
        indexToPage.add(R.id.navigation_home);
        indexToPage.add(R.id.navigation_dashboard);
        indexToPage.add(R.id.navigation_notifications);
        //indexToPage.add(R.id.navigation_3);
        final BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);


        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(mPagerAdapter);
        viewPager2.setOffscreenPageLimit(indexToPage.size());

//        rate0 = new RateAppModule(this);
//        getLifecycle().addObserver(rate0);        //WhiteScree

        //this.viewPager2.post(() -> {
        //Module_U.checkUpdate(getApplicationContext());
        //DLog.d("+AppUpdater");
        //});

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            final Map<String, ?> keys = preferences.getAll();
            final Set<? extends Map.Entry<String, ?>> bbb = keys.entrySet();
            for (Map.Entry<String, ?> entry : bbb) {
                if (Cst.KEY_ORGANIC.equals(key)) {
                    //Log.d(TAG, "ORGANIC_TRIGGER");
                    //wrapContent0Request();
                    return;
                } else if (KEY_DEEP_LINK.equals(key)) {
                    DLog.d("=========>>>>" + key + "::" + entry.getValue());
//                        if (mView.handleDeepLink()) {
//                            mView.PEREHOD_S_DEEPLINKOM();
//                        }
                    return;
                } else if (Const.INTENT_KEY_REFERRER.equals(key)) {
                    String referrer = String.valueOf(entry.getValue());
                    DLog.d("@@@" + key + "::" + referrer);
                    Bundle params = new Bundle();
                    params.putString("utm_source_receiver", referrer);
                    FirebaseAnalytics.getInstance(this).logEvent("referrer_received", params);
                    return;
                }
            }
        });

        //MainAdapter adapter = new MainAdapter(this, preferences, null);

//        UpdateApp aa = new UpdateApp();
//        aa.init(this);

    }


    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Integer position = indexToPage.indexOf(item.getItemId());
        if (position >= 0) {
            menuSelected(position);
            return true;
        }

//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                fm.beginTransaction().hide(active).show(fragment1).commit();
//                active = fragment1;
//                return true;
//
//            case R.id.navigation_dashboard:
//                fm.beginTransaction().hide(active).show(fragment2).commit();
//                active = fragment2;
//                return true;
//
//            case R.id.navigation_notifications:
//                fm.beginTransaction().hide(active).show(fragment3).commit();
//                active = fragment3;
//                return true;
//        }
        return false;
    };

    private void menuSelected(Integer position) {
        handleSelectedSuccess(position);
    }

    private void handleSelectedSuccess(int position) {
        if (viewPager2.getCurrentItem() != position) {
            setItem(position);
        }
    }

    private void setItem(Integer position) {
        viewPager2.setCurrentItem(position);
        //viewModel.push(position);
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        if (rate0 != null) {
//            rate0.appReloadedHandler();
//        }
//        super.onSaveInstanceState(outState);
//    }
}
