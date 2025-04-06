package ai.free.vpn.tweeqoldvpn.getvpn;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;


import ai.free.vpn.tweeqoldvpn.getvpn.BuildConfig;
import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.walhalla.ui.DLog;

import java.util.Calendar;
import java.util.Random;

public class OtUtils {

    public static String generateRandomString() {
        int length = new Random().nextInt(3) + 6; // Random length between 6 and 8 (inclusive)
        StringBuilder randomString = new StringBuilder();
        randomString.append((char) (new Random().nextInt(26) + 'A')); // First letter as capital
        for (int i = 1; i < length; i++) {
            randomString.append((char) (new Random().nextInt(26) + 'a')); // Random lowercase letters
        }
        return randomString.toString();
    }

    public static void setCopyright(Context context, TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        textView.setText(context.getString(R.string.all_rights_reserved, "" + year));
    }

//    static int[] aaa = new int[]{
//            /*0 */Intent.FLAG_FROM_BACKGROUND,
///*1 */Intent.FLAG_DEBUG_LOG_RESOLUTION,
///*2 */Intent.FLAG_EXCLUDE_STOPPED_PACKAGES,
///*3 */Intent.FLAG_INCLUDE_STOPPED_PACKAGES,
///*4 */444,
///*5 */444,
///*6 */Intent.FLAG_ACTIVITY_MATCH_EXTERNAL,
///*7 */Intent.FLAG_ACTIVITY_NO_HISTORY,
///*8 */Intent.FLAG_ACTIVITY_SINGLE_TOP,
///*9 */Intent.FLAG_ACTIVITY_NEW_TASK,
///*10*/Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
///*11*/Intent.FLAG_ACTIVITY_CLEAR_TOP,
///*12*/Intent.FLAG_ACTIVITY_FORWARD_RESULT,
///*13*/Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
///*14*/Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
///*15*/Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT,
///*16*/Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
///*17*/Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY,
///*18*/Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET,
///*19*/Intent.FLAG_ACTIVITY_NEW_DOCUMENT,
///*20*/Intent.FLAG_ACTIVITY_NO_USER_ACTION,
///*21*/Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
///*22*/Intent.FLAG_ACTIVITY_NO_ANIMATION,
///*23*/Intent.FLAG_ACTIVITY_CLEAR_TASK,
///*24*/Intent.FLAG_ACTIVITY_TASK_ON_HOME,
///*25*/Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS,
///*26*/Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT,
///*27*/Intent.FLAG_RECEIVER_REGISTERED_ONLY,
///*28*/Intent.FLAG_RECEIVER_REPLACE_PENDING,
///*29*/Intent.FLAG_RECEIVER_FOREGROUND,
///*30*/Intent.FLAG_RECEIVER_NO_ABORT,
//    };

//    public static void bitCheck() {
////        boolean debuggable = (335544320
////                & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
//
//        int numberToCheck = 335544320;
//
//        if ((numberToCheck & Intent.FLAG_ACTIVITY_NEW_TASK) == Intent.FLAG_ACTIVITY_NEW_TASK) {
//            DLog.d("FLAG_ACTIVITY_NEW_TASK установлен в числе");
//        }
//
//        if ((numberToCheck & Intent.FLAG_ACTIVITY_CLEAR_TASK) == Intent.FLAG_ACTIVITY_CLEAR_TASK) {
//            DLog.d("FLAG_ACTIVITY_CLEAR_TASK установлен в числе");
//        }
//
//// Проверка других флагов, например, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT:
//        if ((numberToCheck & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) {
//            DLog.d("FLAG_ACTIVITY_BROUGHT_TO_FRONT установлен в числе");
//        }
//
//        for (int i = 0; i < aaa.length; i++) {
//            if ((numberToCheck & aaa[i]) == aaa[i]) {
//                DLog.d("@@@" + i);
//            }
//        }
//
//    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static String getRandomString(String[] strings) {
        Random random = new Random();
        int index = random.nextInt(strings.length);
        return strings[index];
    }
}
