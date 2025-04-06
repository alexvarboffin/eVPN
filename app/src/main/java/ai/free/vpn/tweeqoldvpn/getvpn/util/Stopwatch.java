package ai.free.vpn.tweeqoldvpn.getvpn.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Stopwatch {

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private final long startTime = System.currentTimeMillis();
    Calendar calendar = Calendar.getInstance();
    public Stopwatch() {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public long getDiff() {
        return System.currentTimeMillis() - startTime;
    }

    public String getElapsedTime() {
        calendar.setTimeInMillis(getDiff());
        return sdf.format(calendar.getTime());
    }
}
