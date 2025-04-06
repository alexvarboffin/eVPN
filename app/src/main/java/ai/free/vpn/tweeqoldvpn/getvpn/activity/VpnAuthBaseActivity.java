package ai.free.vpn.tweeqoldvpn.getvpn.activity;


import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import ai.free.vpn.tweeqoldvpn.getvpn.App;
import ai.free.vpn.tweeqoldvpn.getvpn.BuildConfig;
import ai.free.vpn.tweeqoldvpn.getvpn.R;

import ai.free.vpn.tweeqoldvpn.getvpn.activity.auth.ChooseLoginRegistrationActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.settings.SettingsActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo.VPNInfoActivityVpnAuth;
import ai.free.vpn.tweeqoldvpn.getvpn.auth.AuthContract;
import ai.free.vpn.tweeqoldvpn.getvpn.auth.FireBaseAuthManager;
import ai.free.vpn.tweeqoldvpn.getvpn.databinding.ActivityBaseLayoutBinding;
import ai.free.vpn.tweeqoldvpn.getvpn.util.CountriesNames;
import ai.free.vpn.tweeqoldvpn.getvpn.util.PropertiesService;

import ai.free.vpn.tweeqoldvpn.getvpn.util.TotalTraffic;

import com.google.firebase.auth.FirebaseUser;
import com.walhalla.library.activity.BActivityPresenter;
import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;
import com.walhalla.vpnapp.MUtils;
import com.walhalla.vpnapp.model.Server;
import com.walhalla.vpnapp.repository.ServerRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.blinkt.openvpn.core.VpnStatus;


public abstract class VpnAuthBaseActivity extends AppCompatActivity
        implements MUtils.Callback, AuthContract.AuthView {

    protected AuthContract.AuthPresenter manager;


    protected boolean hideCurrentConnection = false;

    protected int widthWindow;
    protected int heightWindow;

    protected ServerRepository dbHelper;
    protected Map<String, String> localeCountries;
    private BActivityPresenter presenter;
    private ActivityBaseLayoutBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseLayoutBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_base_layout0);
        presenter = new BActivityPresenter(App.repository);
        manager = new FireBaseAuthManager(this);
        getLifecycle().addObserver(manager);

    }

    @Override
    public void setContentView(int layoutResID) {

        super.setContentView(R.layout.activity_base_layout);
        presenter.setupAdAtBottom(binding.bottomButton);

//        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
//        FrameLayout activityContainer = fullLayout.findViewById(R.id.activity_content);
//        getLayoutInflater().inflate(layoutResID, activityContainer, true);
//        super.setContentView(fullLayout);
//        super.setContentView(layoutResID);


        //LinearLayout rootView = findViewById(R.id.root_layout);
        //View contentView = getLayoutInflater().inflate(getContentViewLayoutId(), rootView, false);
        //rootView.addView(contentView);

        FrameLayout contentContainer = findViewById(R.id.content_container);
        View contentView = getLayoutInflater().inflate(layoutResID, contentContainer, false);
        contentContainer.addView(contentView);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (useHomeButton()) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }

        dbHelper = ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper.newInstance(getApplicationContext());
        localeCountries = CountriesNames.getCountries();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        widthWindow = dm.widthPixels;
        heightWindow = dm.heightPixels;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected boolean useToolbar() {
        return true;
    }

    protected boolean useHomeButton() {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        DLog.d("onResume: " + "@" + this.getClass().getSimpleName());
    }


    public void newConnecting(Server server, boolean fastConnection, boolean autoConnection) {
        if (server != null) {
            Intent intent = VPNInfoActivityVpnAuth.newConnectingInstance(this, server, fastConnection, autoConnection);
            startActivity(intent);
        }
    }

    public static void sendTouchButton(String button) {

    }

    protected void ipInfoResult() {
    }

    protected void getIpInfo(Server server) {
        List<Server> serverList = new ArrayList<>();
        serverList.add(server);

        MUtils m = new MUtils();
        m.getIpInfo(serverList, this);
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onResponse(List<Server> serverList) {
        dbHelper.update(serverList);
        ipInfoResult();
    }

    protected void setDrawable(ImageView flag, String code) {
//        aa.setImageResource(
//                getResources().getIdentifier(code,"drawable",getPackageName()));
        try {
            String raw;
            if ("".equals(code)) {
                flag.setImageResource(R.drawable.ic_unknown);
            } else {
                raw = "raw/" + code + ".png";
                InputStream ims = flag.getContext().getAssets().open((raw).toLowerCase());
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                flag.setImageDrawable(d);
                ims.close();
            }
        } catch (Exception ex) {
            DLog.handleException(ex);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        manager = null;
        TotalTraffic.saveTotal(this);
    }

    public Server getRandomServer() {
        Server randomServer;
        if (PropertiesService.getCountryPriority()) {
            randomServer = dbHelper.getGoodRandomServer(PropertiesService.getSelectedCountry());
        } else {
            randomServer = dbHelper.getGoodRandomServer(null);
        }
        return randomServer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.actionCurrentServer);
        if (item != null) {
            if ((App.connectedServer == null
                    || hideCurrentConnection
                    || !VpnStatus.isVPNActive()))
                item.setVisible(false);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
        if (item.getItemId() == R.id.action_log_out) {
            if (manager != null) {
                manager.logOut(this);
            }
            return true;
        }
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;

//            case R.id.actionSpeed:
//                startActivity(new Intent(this, SpeedTestFragment.class));
//                return true;
        } else if (itemId == R.id.actionCurrentServer) {
            if (App.connectedServer != null) {
                startActivity(new Intent(this, VPNInfoActivityVpnAuth.class));
            }
            return true;
        } else if (itemId == R.id.action_settings) {
            sendTouchButton("Settings");
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

//            case R.id.action_not_vibrate:
//                bugResolver();
//                return true;
//            case R.id.action_refresh:
//                return false;
//
//            case android.R.id.home:
//                Intent intent = m1.newIntent(this);
//                startActivity(intent);
//                return true;


//            case R.string.start_test_again:
//                return false;
        } else if (itemId == R.id.action_about) {
            Module_U.aboutDialog(this);
            //throw new RuntimeException("9999");
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, getString(R.string.url_privacy_policy));
            return true;

//            case R.id.action_rate_app:
//                Module_U.rateUs(this);
//                return true;

//            case R.id.action_more_app_01:
//                Module_U.moreApp(this, "com.walhalla.ttloader");
//                return true;

//            case R.id.action_more_app_02:
//                Module_U.moreApp(this, "com.walhalla.vibro");
//                return true;
        } else if (itemId == R.id.action_share_app) {
            Module_U.shareThisApp(this, null);
            return true;

//            case R.id.action_discover_more_app:
//                Module_U.moreApp(this);
//                return true;
        } else if (itemId == R.id.action_exit) {
            this.finish();
            return true;
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this);
            return true;

//            case R.id.action_exit:
//                this.finish();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void signInError(String message) {

    }

    @Override
    public void launch(Intent signInIntent) {

    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    protected void logoutUser() {
        manager.logOut(this);
        falseAuth(null);
        this.finish();
    }

    @Override
    public void printMessage(String data) {
        if (getSupportActionBar() != null && BuildConfig.DEBUG) {
            getSupportActionBar().setTitle("@@@");
            getSupportActionBar().setSubtitle(data);
        }
    }

    @Override
    public void successAuth(FirebaseUser user) {
        DLog.d("@@@" + getClass().getSimpleName() + ", " + user);
    }

    @Override
    public void falseAuth(FirebaseUser user) {
        DLog.d("@@@" + user);
        if (user == null) {
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN);

//            Intent intent = new Intent(this, 0000
////                    LoginActivity.class
//            );
//            startActivity(intent);
            Intent intent = new Intent(this, ChooseLoginRegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}