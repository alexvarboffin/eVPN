package ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.Handler;
import android.os.IBinder;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;

import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ai.free.vpn.tweeqoldvpn.getvpn.BuildConfig;

import ai.free.vpn.tweeqoldvpn.getvpn.App;
import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.walhalla.ui.plugins.Launcher;
import com.walhalla.vpnapp.model.Server;

import ai.free.vpn.tweeqoldvpn.getvpn.VpnUtils;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.VpnAuthBaseActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.main.MainActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.util.PropertiesService;
import ai.free.vpn.tweeqoldvpn.getvpn.util.Stopwatch;
import ai.free.vpn.tweeqoldvpn.getvpn.util.TotalTraffic;


import com.walhalla.ui.DLog;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import at.grabner.circleprogress.CircleProgressView;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConnectionStatus;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VPNLaunchHelper;
import de.blinkt.openvpn.core.VpnStatus;

public class VPNInfoActivityVpnAuth extends VpnAuthBaseActivity implements WaitConnectionAsync.View {

    private static final int START_VPN_PROFILE = 70;

    private static final String KEY_FAST_CONNECTION = "fastConnection";
    private static final String KEY_AUTO_CONNECTION = "autoConnection";


    private BroadcastReceiver br;
    private BroadcastReceiver trafficReceiver;
    public final static String BROADCAST_ACTION = "de.blinkt.openvpn.VPN_STATUS";


    private static OpenVPNService mService; //old
    //protected IOpenVPNAPIService mService = null; //new


    private VpnProfile profile;

    private Server currentServer = null;
    private Button unblockCheck;
    private Button serverConnect;
    private TextView lastLog;
    private ProgressBar connectingProgress;
    private PopupWindow popupWindow;
    private LinearLayout parentLayout;
    private TextView trafficInTotally;
    private TextView trafficOutTotally;
    private TextView trafficIn;
    private TextView trafficOut;
    private ImageButton bookmark;

    //   private static boolean filterAds = false;
    // private static boolean defaultFilterAds = true;

    private boolean autoConnection;
    private boolean fastConnection;
    private Server autoServer;
    public boolean statusConnection = false;

    private boolean firstData = true;

    private WaitConnectionAsync waitConnection;
    private boolean inBackground;
    private static Stopwatch stopwatch;
    private boolean isBindedService = false;
    CircleProgressView mCircleView;
    private MenuItem loginItem;

    //private String mSelectedProfileReason = LaunchVPN.EXTRA_START_REASON;


    public static Intent newInstance(Context context, Server server) {
        Intent intent = new Intent(context, VPNInfoActivityVpnAuth.class);
        intent.putExtra(Server.class.getCanonicalName(), server);
        return intent;
    }

    public static Intent newInstance(Context context, Server server, VpnProfile profile) {
        Intent intent = new Intent(context, VPNInfoActivityVpnAuth.class);
        intent.putExtra(Server.class.getCanonicalName(), server);
        intent.putExtra(VpnProfile.class.getCanonicalName(), profile);
        return intent;
    }

    public static Intent newConnectingInstance(Context context, Server server, boolean fastConnection, boolean autoConnection) {
        Intent intent = new Intent(context, VPNInfoActivityVpnAuth.class);
        intent.putExtra(Server.class.getCanonicalName(), server);
        intent.putExtra(KEY_FAST_CONNECTION, fastConnection);
        intent.putExtra(KEY_AUTO_CONNECTION, autoConnection);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpninfo);

        Handler handler = new Handler();
        waitConnection = new WaitConnectionAsync(this, handler);


        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);


        parentLayout = findViewById(R.id.serverParentLayout);
        connectingProgress = findViewById(R.id.serverConnectingProgress);
        lastLog = findViewById(R.id.serverStatus);

        serverConnect = findViewById(R.id.serverConnect);
        serverConnect.setOnClickListener(v -> {
            sendTouchButton("serverConnect");
            if (checkStatus()) {
                swithVPN(VpnState.STOP);
            } else {
                swithVPN(VpnState.PREPARE);
            }
        });

        String totalIn = String.format(getResources().getString(R.string.traffic_in),
                TotalTraffic.getTotalTraffic(this).get(0));
        trafficInTotally = findViewById(R.id.serverTrafficInTotally);
        trafficInTotally.setText(totalIn);

        String totalOut = String.format(getResources().getString(R.string.traffic_out),
                TotalTraffic.getTotalTraffic(this).get(1));
        trafficOutTotally = (TextView) findViewById(R.id.serverTrafficOutTotally);
        trafficOutTotally.setText(totalOut);

        trafficIn = (TextView) findViewById(R.id.serverTrafficIn);
        trafficIn.setText("");
        trafficOut = (TextView) findViewById(R.id.serverTrafficOut);
        trafficOut.setText("");

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receiveStatus(context, intent);
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        //@@@ registerReceiver(br, intentFilter, null, null);
        // Choose whether the broadcast receiver should be exported and visible to other apps on the device. If this receiver is listening for broadcasts sent from the system or from other apps—even other apps that you own—use the RECEIVER_EXPORTED flag. If instead this receiver is listening only for broadcasts sent by your app, use the RECEIVER_NOT_EXPORTED flag.
        boolean listenToBroadcastsFromOtherApps = false;
        int receiverFlags = (listenToBroadcastsFromOtherApps)
                ? ContextCompat.RECEIVER_EXPORTED : ContextCompat.RECEIVER_NOT_EXPORTED;
        ContextCompat.registerReceiver(this, br, intentFilter, receiverFlags);


        trafficReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receiveTraffic(context, intent);
            }
        };

        //@@@ registerReceiver(trafficReceiver, new IntentFilter(TotalTraffic.TRAFFIC_ACTION));
        IntentFilter filter = new IntentFilter(TotalTraffic.TRAFFIC_ACTION);
        ContextCompat.registerReceiver(this, trafficReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);

        lastLog.setText(R.string.server_not_connected);
        initView(getIntent());
    }

//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//        loginItem = menu.findItem(R.id.login_menu_item);
//        if (ma.isUserLoggedIn()) {
//            loginItem.setTitle(R.string.action_title_logout);
//        } else {
//            loginItem.setTitle(R.string.action_title_login);
//        }
//    }


    private void initView(Intent intent) {

        autoConnection = intent.getBooleanExtra(KEY_AUTO_CONNECTION, false);
        fastConnection = intent.getBooleanExtra(KEY_FAST_CONNECTION, false);
        currentServer = (Server) intent.getParcelableExtra(Server.class.getCanonicalName());

        Serializable extra = intent.getSerializableExtra(VpnProfile.class.getCanonicalName());
        if (extra != null) {
            profile = (VpnProfile) extra;
            DLog.d("@#@@@@");
        }

        if (currentServer == null) {
            if (App.connectedServer != null) {
                currentServer = App.connectedServer;
            } else {
                onBackPressed();
                return;
            }
        }


        String code = currentServer.getCountryShort().toLowerCase();
        if (code.equals("do"))
            code = "dom";

        ImageView aa = findViewById(R.id.serverFlag);
        setDrawable(aa, code);


        String localeCountryName = localeCountries.get(currentServer.getCountryShort()) != null ?
                localeCountries.get(currentServer.getCountryShort()) : currentServer.getCountryLong();

        TextView countryname = (TextView) findViewById(R.id.elapse);
        countryname.setText(localeCountryName);


        double speedValue = (double) Integer.parseInt(currentServer.getSpeed()) / 1048576;
        speedValue = new BigDecimal(speedValue).setScale(3, RoundingMode.UP).doubleValue();

        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setOnProgressChangedListener(value -> {

        });
        mCircleView.setValue(Integer.parseInt(currentServer.getSpeed()) / 1048576);
        mCircleView.setUnit("Mbps");

        CircleProgressView mCircleView3 = (CircleProgressView) findViewById(R.id.circleView3);
        mCircleView3.setOnProgressChangedListener(value -> {

        });
        if (currentServer.getPing().equals("-")) {
            mCircleView3.setValue(0);
            mCircleView3.setUnit("Ms");
        } else {
            mCircleView3.setValue(Integer.parseInt(currentServer.getPing()));
            mCircleView3.setUnit("Ms");
        }
        CircleProgressView mCircleView2 = (CircleProgressView) findViewById(R.id.circleView2);
        mCircleView2.setOnProgressChangedListener(value -> {

        });

        int mm = 0;
        try {
            mm = Integer.parseInt(currentServer.getNumVpnSessions());
        } catch (Exception e) {
        }
        mCircleView2.setValue(mm);

        if (checkStatus()) {
            serverConnect.setBackground(getResources().getDrawable(R.drawable.button3));
            serverConnect.setText(getString(R.string.server_btn_disconnect));
            ((TextView) findViewById(R.id.serverStatus)).setText(VpnStatus.getLastCleanLogMessage(getApplicationContext()));
        } else {
            serverConnect.setBackground(getResources().getDrawable(R.drawable.vpn_connection_status));
            serverConnect.setText(getString(R.string.server_btn_connect));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initView(intent);
    }

    private void receiveTraffic(Context context, Intent intent) {
        if (checkStatus()) {
            String in = "";
            String out = "";
            if (firstData) {
                firstData = false;
            } else {
                in = String.format(getResources().getString(R.string.traffic_in),
                        intent.getStringExtra(TotalTraffic.DOWNLOAD_SESSION));
                out = String.format(getResources().getString(R.string.traffic_out),
                        intent.getStringExtra(TotalTraffic.UPLOAD_SESSION));
            }

            trafficIn.setText(in);
            trafficOut.setText(out);

            String inTotal = String.format(getResources().getString(R.string.traffic_in),
                    intent.getStringExtra(TotalTraffic.DOWNLOAD_ALL));
            trafficInTotally.setText(inTotal);

            String outTotal = String.format(getResources().getString(R.string.traffic_out),
                    intent.getStringExtra(TotalTraffic.UPLOAD_ALL));
            trafficOutTotally.setText(outTotal);
        }
    }

    private void receiveStatus(Context context, Intent intent) {
        if (checkStatus()) {
            ConnectionStatus status = ConnectionStatus.valueOf(intent.getStringExtra("status"));
            changeServerStatus(status);
            lastLog.setText(VpnStatus.getLastCleanLogMessage(getApplicationContext()));
        }

        if (intent.getStringExtra("detailstatus").equals("NOPROCESS")) {
            try {
                TimeUnit.SECONDS.sleep(1);
                if (!VpnStatus.isVPNActive())
                    prepareStopVPN();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DLog.d("<>");
        if (waitConnection != null)
            waitConnection.cancel(false);

        if (isTaskRoot()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private boolean checkStatus() {
        if (App.connectedServer != null
                && App.connectedServer.hostName.equals(currentServer.hostName)) {
            return VpnStatus.isVPNActive();
        }
        return false;
    }


    private void changeServerStatus(ConnectionStatus status) {

        DLog.d("@@@@@@@@@@@@@" + status);

        switch (status) {
            case LEVEL_CONNECTED:
                statusConnection = true;
                connectingProgress.setVisibility(View.GONE);

                if (!inBackground) {

                    chooseAction();

                }
                serverConnect.setBackground(getResources().getDrawable(R.drawable.button3));
                serverConnect.setText(getString(R.string.server_btn_disconnect));
                break;
            case LEVEL_NOTCONNECTED:
                serverConnect.setBackground(getResources().getDrawable(R.drawable.vpn_connection_status));
                serverConnect.setText(getString(R.string.server_btn_connect));
                break;
            default:
                serverConnect.setBackground(getResources().getDrawable(R.drawable.button3));
                serverConnect.setText(getString(R.string.server_btn_disconnect));
                statusConnection = false;
                connectingProgress.setVisibility(View.VISIBLE);
        }
    }


    private void prepareStopVPN() {
        if (!BuildConfig.DEBUG) {
            try {
                String download = trafficIn.getText().toString();
                download = download.substring(download.lastIndexOf(":") + 2);

            } catch (Exception e) {

            }
        }

        statusConnection = false;
        if (waitConnection != null) {
            waitConnection.cancel(false);
        }
        connectingProgress.setVisibility(View.GONE);
        lastLog.setText(R.string.server_not_connected);
        serverConnect.setBackground(getResources().getDrawable(R.drawable.vpn_connection_status));
        serverConnect.setText(getString(R.string.server_btn_connect));
        App.connectedServer = null;
    }


    private void startVpn() {
        stopwatch = new Stopwatch();
        App.connectedServer = currentServer;
        hideCurrentConnection = true;

        Intent intent = VpnService.prepare(this);

        if (intent != null) {
            VpnStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                    ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
            try {
                startActivityForResult(intent, START_VPN_PROFILE);
            } catch (ActivityNotFoundException ane) {
                VpnStatus.logError(R.string.no_vpn_support_image);
            }
        } else {
            onActivityResult(START_VPN_PROFILE, RESULT_OK, null);
        }
    }

    @Override
    protected void ipInfoResult() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        inBackground = false;

        if (currentServer != null) {
            if (currentServer.getCity() == null) {
                getIpInfo(currentServer);
            }

            if (App.connectedServer != null && currentServer.getIp().equals(App.connectedServer.getIp())) {
                hideCurrentConnection = true;
                invalidateOptionsMenu();
            }
        }


        Intent intent = new Intent(this, OpenVPNService.class);
        intent.setAction(OpenVPNService.START_SERVICE);
        isBindedService = bindService(intent, mConnection, BIND_AUTO_CREATE);

        if (checkStatus()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!checkStatus()) {
                App.connectedServer = null;
                serverConnect.setText(getString(R.string.server_btn_connect));
                serverConnect.setBackground(getResources().getDrawable(R.drawable.vpn_connection_status));
                lastLog.setText(R.string.server_not_connected);
            }
        } else {
            serverConnect.setText(getString(R.string.server_btn_connect));
            serverConnect.setBackground(getResources().getDrawable(R.drawable.vpn_connection_status));
            if (autoConnection) {
                swithVPN(VpnState.PREPARE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        inBackground = true;

        if (isBindedService) {
            isBindedService = false;
            unbindService(mConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        unregisterReceiver(trafficReceiver);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == START_VPN_PROFILE) {
                VPNLaunchHelper.startOpenVpn(profile, getBaseContext());//, mSelectedProfileReason
            }
        }
    }

    private void chooseAction() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.conected, null);

        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );

        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        Button marketButton = view.findViewById(R.id.successPopUpBtnPlayMarket);
        marketButton.setOnClickListener(v -> {
            sendTouchButton("successPopUpBtnPlayMarket");
            Launcher.rateUs(this);
        });

        view.findViewById(R.id.successPopUpBtnBrowser).setOnClickListener(v -> {
            sendTouchButton("successPopUpBtnBrowser");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com")));
        });
        view.findViewById(R.id.successPopUpBtnDesktop).setOnClickListener(v -> {
            sendTouchButton("successPopUpBtnDesktop");
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        });
        view.findViewById(R.id.successPopUpBtnClose).setOnClickListener(v -> {
            sendTouchButton("successPopUpBtnClose");
            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0, 0);

    }


    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            OpenVPNService.LocalBinder binder = (OpenVPNService.LocalBinder) service;
            mService = binder.getService();

//            mService = IOpenVPNAPIService.Stub.asInterface(service);//new api
//            try {
//                // Request permission to use the API
//                Intent i = mService.prepare(getPackageName());
//                if (i!=null) {
//                    startActivityForResult(i, ICS_OPENVPN_PERMISSION);
//                } else {
//                    onActivityResult(ICS_OPENVPN_PERMISSION, Activity.RESULT_OK,null);
//                }
//            } catch (RemoteException e) {
//                DLog.d("openvpn service connection failed: " + e.getMessage());
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
        }

    };


    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.try_another_server_text))
                .setPositiveButton(getString(R.string.try_another_server_ok),
                        (dialog, id) -> {
                            dialog.cancel();
                            swithVPN(VpnState.STOP);
                            autoServer = dbHelper.getSimilarServer(currentServer.getCountryLong(), currentServer.getIp());
                            if (autoServer != null) {
                                newConnecting(autoServer, false, true);
                            } else {
                                onBackPressed();
                            }
                        })
                .setNegativeButton(getString(R.string.try_another_server_no),
                        (dialog, id) -> {
                            if (!statusConnection) {
                                DLog.d("@@@@" + statusConnection);
                                waitConnection.startAsyncTask();
                            }
                            dialog.cancel();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onPostExecute() {
        DLog.d("@@statusConnection@" + statusConnection);

        if (!statusConnection) {
            if (currentServer != null) {
                dbHelper.setInactive(currentServer);
            }

            if (fastConnection) {
                swithVPN(VpnState.STOP);
                newConnecting(getRandomServer(), true, true);
            } else if (PropertiesService.getAutomaticSwitching()) {
                if (!inBackground)
                    showAlert();
            }
        }
    }

    private enum VpnState {
        STOP, PREPARE
    }

    private void swithVPN(VpnState state) {

        DLog.d("@@@@{{STATE}}" + state);

        if (state.equals(VpnState.STOP)) {
            try {
                ProfileManager.setConntectedVpnProfileDisconnected(this);

                //old
                if (mService != null && mService.getManagement() != null) {
                    mService.getManagement().stopVPN(false);
                }

                //new api
//                if (mService != null ) {
//                    mService.disconnect();
//                }
            } catch (Exception e) {
                DLog.handleException(e);
            }
        } else if (state.equals(VpnState.PREPARE)) {
            connectingProgress.setVisibility(View.VISIBLE);
            if (profile == null//&& currentServer.getConfigData() != null
            ) {
                DLog.d("<null>" + currentServer.getConfigData());
                profile = VpnUtils.loadVpnProfileFromBase64(this, currentServer);
            } else {

            }

            if (profile != null) {

                if (!TextUtils.isEmpty(currentServer.mPassword)) {
                    profile.mName = currentServer.getCountryLong();
                    profile.mUsername = currentServer.mUsername;
                    profile.mPassword = currentServer.mPassword;
                    profile.mServerName = currentServer.ip;
                }

//            DLog.d("{{***}}" + profile.mServerName);
//            DLog.d("{{***}}" + profile.mServerPort);
//            DLog.d("{{***}}" + profile.mAlias);
//            DLog.d("{{***}}" + profile.mUsername);
//            DLog.d("{{***}}" + profile.mPassword);

                waitConnection.startAsyncTask();
                serverConnect.setBackground(getResources().getDrawable(R.drawable.button3));
                serverConnect.setText(getString(R.string.server_btn_disconnect));
                startVpn();
            } else {
                connectingProgress.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.server_error_loading_profile), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
