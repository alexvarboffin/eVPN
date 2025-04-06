package ai.free.vpn.tweeqoldvpn.getvpn.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import ai.free.vpn.tweeqoldvpn.getvpn.OtUtils;

import ai.free.vpn.tweeqoldvpn.getvpn.activity.step.StepActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo.VPNInfoActivityVpnAuth;
import ai.free.vpn.tweeqoldvpn.getvpn.auth.FireBaseAuthManager;
import ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper;
import ai.free.vpn.tweeqoldvpn.getvpn.util.NetworkState;

import com.daimajia.numberprogressbar.NumberProgressBar;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.walhalla.vpnapp.model.Server;

import ai.free.vpn.tweeqoldvpn.getvpn.util.PropertiesService;
import ai.free.vpn.tweeqoldvpn.getvpn.util.Stopwatch;

import com.walhalla.ui.DLog;

public class SplashActivityVpn extends AppCompatActivity {
    public static final int COOL_FLAGH = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_RECEIVER_FOREGROUND;

    //String BASE_FILE_NAME = "0vpngate.csv";

    private NumberProgressBar progressBar;
    private TextView commentsText;
    private static boolean loadStatus = false;
    private Handler updateHandler;

    private final int LOAD_ERROR = 0;
    private final int DOWNLOAD_PROGRESS = 1;
    private final int PARSE_PROGRESS = 2;
    private final int LOADING_SUCCESS = 3;
    private final int SWITCH_TO_RESULT = 4;


    private boolean premiumStage = true;

    private int percentDownload = 0;
    private Stopwatch stopwatch;
    private DBHelper dbHelper;
    private PropertiesService properties;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            DLog.handleException(e);
        }

        setContentView(R.layout.activity_splash);
        OtUtils.setCopyright(this, findViewById(R.id.copyright));
        properties = new PropertiesService(this);
        dbHelper = ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper.newInstance(getApplicationContext());

        progressBar = findViewById(R.id.number_progress_bar);
        commentsText = findViewById(R.id.commentsText);

        if (getIntent().getBooleanExtra("firstPremiumLoad", false)) {
            findViewById(R.id.loaderPremiumText).setVisibility(View.VISIBLE);
        }

        progressBar.setMax(100);

        updateHandler = new Handler(msg -> {

            DLog.d("" + msg.arg1);

            switch (msg.arg1) {

                case LOAD_ERROR: {
                    commentsText.setText(msg.arg2);
                    progressBar.setProgress(100);
                }
                break;
                case DOWNLOAD_PROGRESS: {
                    commentsText.setText(R.string.downloading_csv_text);
                    progressBar.setProgress(msg.arg2);

                }
                break;
                case PARSE_PROGRESS: {
                    commentsText.setText(R.string.parsing_csv_text);
                    progressBar.setProgress(msg.arg2);
                }
                break;

                case LOADING_SUCCESS: {
                    commentsText.setText(R.string.successfully_loaded);
                    progressBar.setProgress(100);
                    Message end = new Message();
                    end.arg1 = SWITCH_TO_RESULT;
                    updateHandler.sendMessageDelayed(end, 500);
                }
                break;

                case SWITCH_TO_RESULT: {

                    if (properties.getConnectOnStart()) {
                        Server randomServer = getRandomServer();
                        if (randomServer != null) {
                            newConnecting(randomServer, true, true);
                        } else {
                            launchMain();
                        }
                    } else {
                        launchMain();
                    }
                }
            }
            return true;
        });
        progressBar.setProgress(0);
    }

    private void launchMain() {
        startActivity(new Intent(SplashActivityVpn.this, StepActivity.class).setFlags(COOL_FLAGH));
    }


    public void newConnecting(Server server, boolean fastConnection, boolean autoConnection) {
        if (server != null) {
            Intent intent = VPNInfoActivityVpnAuth
                    .newConnectingInstance(this, server, fastConnection, autoConnection).setFlags(COOL_FLAGH);
            startActivity(intent);
        }
    }

    public Server getRandomServer() {
        Server randomServer;
        if (properties.getCountryPriority()) {
            randomServer = dbHelper.getGoodRandomServer(properties.getSelectedCountry());
        } else {
            randomServer = dbHelper.getGoodRandomServer(null);
        }
        return randomServer;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkState.isOnline(this)) {
            //if (loadStatus) {
            Intent intent = new Intent(this, StepActivity.class).setFlags(COOL_FLAGH);
            startActivity(intent);
            finish();
            //} else {
            //    loadStatus = true;
            //}
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.network_error))
                    .setMessage(getString(R.string.network_error_message))
                    .setPositiveButton(getString(R.string.ok),
                            (dialog, id) -> {
                                dialog.cancel();
                                finish();
                            })
                    .setNegativeButton(getString(R.string.action_open_connection_settings),
                            (dialog, id) -> {
                                dialog.cancel();
                                try {
                                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    // Обработка исключения, если активность не найдена
                                    e.printStackTrace();
                                }
                            });
            AlertDialog alert = builder
                    .setCancelable(false).create();
            alert.show();
        }
        // /data/user/0/com.abc.evpn/cache/0vpngate.csv
        // /data/user/0/com.abc.evpn/files/0vpngate.csv

//        File file = new File(conf());
//        DLog.d("@@" + "@@@" + conf() + " " + file.exists());
//        if (!file.exists()) {
//            downloadCSVFile();
//        }else {
//            parseCSVFile();
//        }
    }


    //    private String conf() {
//        String out = mm().concat("/").concat(BASE_FILE_NAME);
//        return out;
//    }
//    private String mm() {
//        //return getCacheDir().getPath();
//        return getFilesDir().getPath();
//    }


    //http://list-server-tmp-2.vpngate.net/vpngate.asmx?op=GetVpnGateHostList

//    private void downloadCSVFile() {
//
//        String BASE_URL = "http://www.vpngate.net/api/iphone/";
//
//        stopwatch = new Stopwatch();
//        DLog.d("@@" + BASE_URL + " ==> " + BASE_FILE_NAME);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS) // Таймаут соединения
//                .writeTimeout(30, TimeUnit.SECONDS) // Таймаут записи
//                .readTimeout(30, TimeUnit.SECONDS) // Таймаут чтения
//                .build();
//
//        AndroidNetworking
//                .download(BASE_URL, mm(), BASE_FILE_NAME)
//                .setTag("downloadCSV")
//                .setPriority(Priority.MEDIUM)
//                .setOkHttpClient(client)
//                .build()
//                .setDownloadProgressListener((bytesDownloaded, totalBytes) -> {
//                    //DLog.d("@@" +bytesDownloaded + totalBytes);
//
//                    if (totalBytes <= 0) {
//                        // when we dont know the file size, assume it is 1200000 bytes :)
//                        totalBytes = 1200000;
//                    }
//
//                    percentDownload = (int) ((100 * bytesDownloaded) / totalBytes);
//
//
//                    Message msg = new Message();
//                    msg.arg1 = DOWNLOAD_PROGRESS;
//                    msg.arg2 = percentDownload;
//                    updateHandler.sendMessage(msg);
//                })
//                .startDownload(new DownloadListener() {
//                    @Override
//                    public void onDownloadComplete() {
//                        parseCSVFile();
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        DLog.d("@@" + "@@@" + error);
//                        Message msg = new Message();
//                        msg.arg1 = LOAD_ERROR;
//                        msg.arg2 = R.string.network_error;
//                        updateHandler.sendMessage(msg);
//                    }
//                });
//    }


    private void parseCSVFile() {
//        String out = conf();
//        DLog.d("@@" + "@@@" + out);
//        BufferedReader reader = null;
//        try {
//            FileReader r = new FileReader(out);
//            reader = new BufferedReader(r);
//        } catch (IOException e) {
//            //DLog.d("###"+e.getLocalizedMessage());
//            e.printStackTrace();
//            Message msg = new Message();
//            msg.arg1 = LOAD_ERROR;
//            msg.arg2 = R.string.csv_file_error;
//            updateHandler.sendMessage(msg);
//        }
//        if (reader != null) {
//            try {
//                int startLine = 2;
//                int type = 0;
//
//                dbHelper.clearTable();
//
//                int counter = 0;
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    if (counter >= startLine) {
//                        //DLog.d("###"+line+"___"+type);
//                        dbHelper.putLine(line, type);
//                    }
//                    counter++;
//                }
        Message end = new Message();
        end.arg1 = LOADING_SUCCESS;
        updateHandler.sendMessageDelayed(end, 200);
//            } catch (Exception e) {
//                //DLog.d("###"+e.getLocalizedMessage());
//                e.printStackTrace();
//                Message msg = new Message();
//                msg.arg1 = LOAD_ERROR;
//                msg.arg2 = R.string.csv_file_error_parsing;
//                updateHandler.sendMessage(msg);
//            }
//        }
    }
}
