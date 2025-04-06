package ai.free.vpn.tweeqoldvpn.getvpn;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import ai.free.vpn.tweeqoldvpn.getvpn.BuildConfig;
import com.walhalla.ui.DLog;
import com.walhalla.vpnapp.model.Server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.ProfileManager;

public class VpnUtils {

    //The client certificate file (dummy).

    public static VpnProfile loadVpnProfileFromBase64(Context context, Server currentServer) {
        byte[] data;
        try {
            data = Base64.decode(currentServer.getConfigData(), Base64.DEFAULT);
        } catch (Exception e) {
            DLog.handleException(e);
            return null;
        }

        ConfigParser cp = new ConfigParser();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(data));
        try {
            if (BuildConfig.DEBUG) {
                //print(data);//The client certificate file
            }
            cp.parseConfig(isr);

            VpnProfile vpnProfile = cp.convertProfile();
            vpnProfile.mName = currentServer.getCountryLong();
            ProfileManager.getInstance(context).addProfile(vpnProfile);
            return vpnProfile;
        } catch (IOException | ConfigParser.ConfigParseError e) {
            DLog.handleException(e);
            return null;
        }
    }

    private static void print(byte[] data) {
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(data));
        BufferedReader br = new BufferedReader(isr);
        try {
            String line = "";
            while (line != null){
                line = br.readLine();
                DLog.d(line);
            }
        } catch (Exception memoryError) {
            DLog.d(memoryError.getLocalizedMessage());
        }
    }


    //@@@@@@@@@@@ ЭТОТ
    public static VpnProfile loadVpnProfileFromAsset(Context context, Server currentServer, String configFileName) {
        AssetManager am = context.getAssets();
        try {
            InputStream inputStream = am.open("configs/" + configFileName);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            ConfigParser cp = new ConfigParser();
            InputStreamReader isr = new InputStreamReader(inputStream);
            cp.parseConfig(isr);
            VpnProfile profile = cp.convertProfile();
            profile.mName = currentServer.getCountryLong();
            ProfileManager.getInstance(context).addProfile(profile);
            return profile;
        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static OpenVpnProfile parse0(AssetManager am, String configFileName) throws IOException {
//        OpenVpnProfile profile = new OpenVpnProfile();
//
//
//        InputStream inputStream = am.open("configs/" + configFileName);
//        //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            StringBuilder caCertBuilder = new StringBuilder();
//            StringBuilder tlsAuthKeyBuilder = new StringBuilder();
//            StringBuilder clientCertBuilder = new StringBuilder();
//            StringBuilder privateKeyBuilder = new StringBuilder();
//
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("remote ")) {
//                    String[] parts = line.split(" ");
//                    String remoteHost = parts[1];
//                    int remotePort = Integer.parseInt(parts[2]);
//                    profile.setRemoteHost(remoteHost);
//                    profile.setRemotePort(remotePort);
//                } else if (line.startsWith("proto ")) {
//                    String[] parts = line.split(" ");
//                    String protocol = parts[1];
//                    profile.setProtocol(protocol);
//                } else if (line.startsWith("auth-user-pass")) {
//                    profile.setAuthUserPass(true);
//                } else if (line.startsWith("<ca>")) {
//                    line = reader.readLine();
//                    while (!line.equals("</ca>")) {
//                        caCertBuilder.append(line);
//                        line = reader.readLine();
//                    }
//                    profile.setCaCertificate(caCertBuilder.toString());
//                } else if (line.startsWith("<tls-auth>")) {
//                    line = reader.readLine();
//                    while (!line.equals("</tls-auth>")) {
//                        tlsAuthKeyBuilder.append(line);
//                        line = reader.readLine();
//                    }
//                    profile.setTlsAuthKey(tlsAuthKeyBuilder.toString());
//                } else if (line.startsWith("<cert>")) {
//                    line = reader.readLine();
//                    while (!line.equals("</cert>")) {
//                        clientCertBuilder.append(line);
//                        line = reader.readLine();
//                    }
//                    profile.setClientCertificate(clientCertBuilder.toString());
//                } else if (line.startsWith("<key>")) {
//                    line = reader.readLine();
//                    while (!line.equals("</key>")) {
//                        privateKeyBuilder.append(line);
//                        line = reader.readLine();
//                    }
//                    profile.setPrivateKey(privateKeyBuilder.toString());
//                }
//            }
//        } catch (IOException e) {
//            DLog.handleException(e);
//        }
//
//        return profile;
//    }

//    public static OpenVpnProfile parseA(String filePath) {

//        String str = "";
//        while (true) {
//            String readLine = bufferedReader.readLine();
//            if (readLine == null) {
//                break;
//            }
//            str = str + readLine + "\n";
//        }
//        bufferedReader.readLine();
//
//        return profile;
//    }
}
