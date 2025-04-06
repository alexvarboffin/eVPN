package ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo;

import android.os.Handler;

import com.walhalla.ui.DLog;

import java.util.concurrent.TimeUnit;

import ai.free.vpn.tweeqoldvpn.getvpn.util.PropertiesService;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WaitConnectionAsync {

    private ExecutorService executorService;
    private final Handler handler;


    public interface View {
        void onPostExecute();
    }

    private final View view;

    public WaitConnectionAsync(View activity, Handler handler) {
        this.view = activity;
        this.executorService = Executors.newSingleThreadExecutor();
        this.handler = handler;
    }

    public void startAsyncTask() {

        DLog.d("@@" + isExecutorRunning());

        if (executorService.isShutdown()) {
            executorService = Executors.newSingleThreadExecutor();
        }
        try {
            executorService.execute(() -> {
                try {
                    int pref = 120;//PropertiesService.getAutomaticSwitchingSeconds();
                    DLog.d("0 ::pref::: " + pref);
                    TimeUnit.SECONDS.sleep(pref);
                    DLog.d("1 ::pref::: " + pref);
                } catch (InterruptedException e) {
                    DLog.d("InterruptedException: " + e.getLocalizedMessage());
                }

                // When the task is finished, call the 'onPostExecute()' method on the UI thread.
                handler.post(view::onPostExecute);
            });
        } catch (Exception e) {
            DLog.handleException(e);
        }

        // Don't forget to shut down the executor service after its task is completed.
        //cancel(false);
    }

//    public void cancel(boolean b) {
//        try {
//            executorService.shutdown();
//        }catch (Exception e){
//            DLog.handleException(e);
//        }
//    }

    public void cancel(boolean b) {
        try {
            executorService.shutdownNow();
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public boolean isExecutorRunning() {
        DLog.d("@@isExecutorRunning@" + !executorService.isShutdown());
        return !executorService.isShutdown();
    }
}
