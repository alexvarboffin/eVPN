package com.abc.evpn;

import android.content.Context;
import android.util.Base64;

import com.abc.evpn.model.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.ProfileManager;

public class VpnUtils {

    public static VpnProfile loadVpnProfile(Context context, Server currentServer) {
        byte[] data;
        try {
            data = Base64.decode(currentServer.getConfigData(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        ConfigParser cp = new ConfigParser();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(data));
        try {
            cp.parseConfig(isr);
            VpnProfile vpnProfile = cp.convertProfile();
            vpnProfile.mName = currentServer.getCountryLong();
            ProfileManager.getInstance(context).addProfile(vpnProfile);
            return vpnProfile;
        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
            return null;
        }
    }
}
