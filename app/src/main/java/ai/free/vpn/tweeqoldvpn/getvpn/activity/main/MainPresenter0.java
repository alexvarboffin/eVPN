package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import ai.free.vpn.tweeqoldvpn.getvpn.VpnUtils;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo.VPNInfoActivityVpnAuth;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpnlist.VPNListActivityVpnAuth;

import com.walhalla.ui.DLog;
import com.walhalla.vpnapp.model.Server;
import com.walhalla.vpnapp.repository.ServerRepository;

import java.util.List;

import de.blinkt.openvpn.VpnProfile;

public class MainPresenter0 implements MainContract0.Presenter {

    private final ServerRepository dbHelper;

    String[] codes = new String[]{
            "finland",
            "france",
            "germany",
            "italy",
            "kazakhstan",
            "netherlands",
            "poland",
            "russia",
            "usa"
    };


    private final MainContract0.View view;
    private final AssetManager am;
    private final Context context;

    public MainPresenter0(Context context, MainContract0.View view) {
        this.view = view;
        this.am = context.getAssets();
        this.context = context;
        this.dbHelper = ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper.newInstance(context);
    }

    @Override
    public void loadConfigs() {
        dbHelper.getUniqueCountries(new ServerRepository.DataCallback<List<Server>>() {
            @Override
            public void successResult(List<Server> data) {
                view.showConfigs(data);
            }

            @Override
            public void showError(String aa) {
                view.showError(aa);
            }
        });
    }

    @Override
    public void applyConfig(Server server) {
        if (server != null) {
            //Intent intent = VPNInfoActivity.newConnectingInstance(this, server, fastConnection, autoConnection);
            //startActivity(intent);


            //OpenVpnProfile
            VpnProfile profile = VpnUtils.loadVpnProfileFromAsset(context, server, server.configFileName);

            if (profile != null) {
                DLog.d("@@@" + profile.mServerName);
                DLog.d("@@@" + profile.mServerPort);
                DLog.d("@@@" + profile.mAuthenticationType);
            }

            Intent intent = VPNInfoActivityVpnAuth.newInstance(context, server, profile);
            context.startActivity(intent);

//            server.setIp(profile.getRemoteHost());
//            server.hostName = (profile.getRemoteHost());
        }
    }

    public void applyCountry(Server server) {
        String username = "1223";
        String password = "3333";
        //vp.mUsername = ("username");
        //vp.mPassword = ("password");
        DLog.d("@@" + server);
        VPNListActivityVpnAuth.newInstance(context, server.getCountryShort());
    }

//    private OpenVpnProfile rrrr(String configFileName) {
//        OpenVpnProfile profile = null;
//        try {
////            ConfigParser cp = new ConfigParser();
////            cp.parseConfig(bufferedReader);

////            VpnProfile vp = cp.convertProfile();
////            String serverAddress = vp.mServerName;
////            String serverPort = vp.mServerPort;
////            DLog.d("@@@@@@@"+"configs/" + configFileName+" " + serverAddress + " " + vp.toString() + "" + (bufferedReader == null));
//
//        } catch (IOException e) {
//            DLog.d(""+"configs/" + configFileName);
//            DLog.handleException(e);
//        }
//        return profile;
//    }
}

