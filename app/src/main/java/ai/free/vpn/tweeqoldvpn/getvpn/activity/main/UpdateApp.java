//package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;
//
//import android.content.Context;
//
//import com.google.android.gms.tasks.Task;
//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.UpdateAvailability;
//
//public class UpdateApp {
//
//    public UpdateApp() {
//
//    }
//
//    public void init(Context context) {
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
//
//        // Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // This example applies an immediate update. To apply a flexible update
//                    // instead, pass in AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                // Request the update.
//            }
//        });
//
//    }
//}
