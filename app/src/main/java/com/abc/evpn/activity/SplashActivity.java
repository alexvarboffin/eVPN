package com.abc.evpn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.TextView;

import com.abc.evpn.util.NetworkState;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.daimajia.numberprogressbar.NumberProgressBar;

import com.abc.evpn.R;
import com.abc.evpn.model.Server;
import com.abc.evpn.util.PropertiesService;
import com.abc.evpn.util.Stopwatch;
import com.walhalla.ui.DLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class SplashActivity extends BaseActivity {
    String BASE_FILE_NAME = "0vpngate.csv";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (NetworkState.isOnline()) {
            if (loadStatus) {
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                finish();
            } else {
                loadStatus = true;

            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.network_error))
                    .setMessage(getString(R.string.network_error_message))
                    .setNegativeButton(getString(R.string.ok),
                            (dialog, id) -> {
                                dialog.cancel();
                                onBackPressed();
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


        progressBar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        commentsText = (TextView) findViewById(R.id.commentsText);

        if (getIntent().getBooleanExtra("firstPremiumLoad", false))
            ((TextView) findViewById(R.id.loaderPremiumText)).setVisibility(View.VISIBLE);

        progressBar.setMax(100);

        updateHandler = new Handler(msg -> {
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

                    if (PropertiesService.getConnectOnStart()) {
                        Server randomServer = getRandomServer();
                        if (randomServer != null) {
                            newConnecting(randomServer, true, true);
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                }
            }
            return true;
        });
        progressBar.setProgress(0);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // /data/user/0/com.abc.evpn/cache/0vpngate.csv
        // /data/user/0/com.abc.evpn/files/0vpngate.csv

        File file = new File(conf());
        DLog.d("@@" + "@@@" + conf() + " " + file.exists());
        if (!file.exists()) {
            downloadCSVFile();
        }else {
            parseCSVFile();
        }
    }



    private String conf() {
        String out = mm().concat("/").concat(BASE_FILE_NAME);
        return out;
    }
    private String mm() {
        //return getCacheDir().getPath();
        return getFilesDir().getPath();
    }
    @Override
    protected boolean useHomeButton() {
        return false;
    }

    @Override
    protected boolean useMenu() {
        return false;
    }


    //http://list-server-tmp-2.vpngate.net/vpngate.asmx?op=GetVpnGateHostList

    private void downloadCSVFile() {

        String BASE_URL = "http://www.vpngate.net/api/iphone/";

        stopwatch = new Stopwatch();
        DLog.d("@@" + BASE_URL + " ==> " + BASE_FILE_NAME);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Таймаут соединения
                .writeTimeout(30, TimeUnit.SECONDS) // Таймаут записи
                .readTimeout(30, TimeUnit.SECONDS) // Таймаут чтения
                .build();

        AndroidNetworking
                .download(BASE_URL, mm(), BASE_FILE_NAME)
                .setTag("downloadCSV")
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(client)
                .build()
                .setDownloadProgressListener((bytesDownloaded, totalBytes) -> {
                    //DLog.d("@@" +bytesDownloaded + totalBytes);

                    if (totalBytes <= 0) {
                        // when we dont know the file size, assume it is 1200000 bytes :)
                        totalBytes = 1200000;
                    }

                    percentDownload = (int) ((100 * bytesDownloaded) / totalBytes);


                    Message msg = new Message();
                    msg.arg1 = DOWNLOAD_PROGRESS;
                    msg.arg2 = percentDownload;
                    updateHandler.sendMessage(msg);
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        parseCSVFile();
                    }

                    @Override
                    public void onError(ANError error) {
                        DLog.d("@@" + "@@@" + error);
                        Message msg = new Message();
                        msg.arg1 = LOAD_ERROR;
                        msg.arg2 = R.string.network_error;
                        updateHandler.sendMessage(msg);
                    }
                });
    }



    private void parseCSVFile() {
        String out = conf();
        DLog.d("@@" + "@@@" + out);
        BufferedReader reader = null;
        try {
            FileReader r = new FileReader(out);
            reader = new BufferedReader(r);
        } catch (IOException e) {
            //DLog.d("###"+e.getLocalizedMessage());
            e.printStackTrace();
            Message msg = new Message();
            msg.arg1 = LOAD_ERROR;
            msg.arg2 = R.string.csv_file_error;
            updateHandler.sendMessage(msg);
        }
        if (reader != null) {
            try {
                int startLine = 2;
                int type = 0;


                dbHelper.clearTable();


                int counter = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (counter >= startLine) {
                        //DLog.d("###"+line+"___"+type);
                        dbHelper.putLine(line, type);
                    }
                    counter++;
                }
                Message end = new Message();
                end.arg1 = LOADING_SUCCESS;
                updateHandler.sendMessageDelayed(end, 200);
            } catch (Exception e) {
                //DLog.d("###"+e.getLocalizedMessage());
                e.printStackTrace();
                Message msg = new Message();
                msg.arg1 = LOAD_ERROR;
                msg.arg2 = R.string.csv_file_error_parsing;
                updateHandler.sendMessage(msg);
            }
        }
    }
}
