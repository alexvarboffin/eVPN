package ai.free.vpn.tweeqoldvpn.getvpn.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import de.blinkt.openvpn.core.OpenVPNService;



public class TotalTraffic {

    public static final String TRAFFIC_ACTION = "traffic_action";

    public static final String DOWNLOAD_ALL = "download_all";
    public static final String DOWNLOAD_SESSION = "download_session";
    public static final String UPLOAD_ALL = "upload_all";
    public static final String UPLOAD_SESSION = "upload_session";

    public static long inTotal;
    public static long outTotal;


    public static void calcTraffic(Context context, long in, long out, long diffIn, long diffOut) {
        List<String> totalTraffic = getTotalTraffic(diffIn, diffOut, context);
        final Resources res = context.getResources();
        Intent traffic = new Intent();
        traffic.setAction(TRAFFIC_ACTION);
        traffic.putExtra(DOWNLOAD_ALL, totalTraffic.get(0));
        traffic.putExtra(DOWNLOAD_SESSION, OpenVPNService.humanReadableByteCount(in, false, res));
        traffic.putExtra(UPLOAD_ALL, totalTraffic.get(1));
        traffic.putExtra(UPLOAD_SESSION, OpenVPNService.humanReadableByteCount(out, false, res));

        context.sendBroadcast(traffic);
    }

    public static List<String> getTotalTraffic(Context context) {
        return getTotalTraffic(0, 0, context);
    }

    public static List<String> getTotalTraffic(long in, long out, Context context) {
        PropertiesService properties = new PropertiesService(context);

        List<String> totalTraffic = new ArrayList<String>();

        if (inTotal == 0)
            inTotal = properties.getDownloaded();

        if (outTotal == 0)
            outTotal = properties.getUploaded();

        inTotal = inTotal + in;
        outTotal = outTotal + out;
        final Resources res = context.getResources();
        totalTraffic.add(OpenVPNService.humanReadableByteCount(inTotal, false, res));
        totalTraffic.add(OpenVPNService.humanReadableByteCount(outTotal, false, res));

        return totalTraffic;
    }

    public static void saveTotal(Context context) {
        PropertiesService properties = new PropertiesService(context);
        if (inTotal != 0)
        {
            properties.setDownloaded(inTotal);
        }

        if (outTotal != 0)
        {
            properties.setUploaded(outTotal);
        }
    }

}
