package ai.free.vpn.tweeqoldvpn.getvpn.activity;

import android.graphics.Color;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import ai.free.vpn.tweeqoldvpn.getvpn.Const;
import ai.free.vpn.tweeqoldvpn.getvpn.OtUtils;
import ai.free.vpn.tweeqoldvpn.getvpn.dns.HttpDownloadTest;
import ai.free.vpn.tweeqoldvpn.getvpn.dns.HttpUploadTest;
import ai.free.vpn.tweeqoldvpn.getvpn.dns.PingTest;
import ai.free.vpn.tweeqoldvpn.getvpn.model.GetSpeedTestHostsHandler;


public class SpeedTestFragment extends Fragment {

    private static final String TAG = "@@@";

    int position = 0;
    int lastPosition = 0;

    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;

    @Override
    public void onResume() {
        super.onResume();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_speedtest, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
//        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        actionBar.setHomeAsUpIndicator(upArrow);

        final Button startButton = view.findViewById(R.id.startButton);
        final DecimalFormat dec = new DecimalFormat("#.##");
        startButton.setText(R.string.begin_test);

        OtUtils.setCopyright(getContext(), view.findViewById(R.id.copyright));


        tempBlackList = new HashSet<>();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();

        startButton.setOnClickListener(v -> {
            startButton.setEnabled(false);


            if (getSpeedTestHostsHandler == null || getSpeedTestHostsHandler.getMapKey().size() == 0) {
                getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                getSpeedTestHostsHandler.start();
            }

            new Thread(new Runnable() {
                RotateAnimation rotate;
                final ImageView barImageView = view.findViewById(R.id.barImageView);
                final TextView pingTextView = view.findViewById(R.id.pingTextView);
                final TextView downloadTextView = view.findViewById(R.id.downloadTextView);
                final TextView uploadTextView = view.findViewById(R.id.uploadTextView);

                @Override
                public void run() {
                    runOnUiThread(() -> {
                        startButton.setText("Selecting best server based on ping...");
                    });


                    int timeCount = 600;
                    while (!getSpeedTestHostsHandler.isFinished()) {
                        timeCount--;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                        if (timeCount <= 0) {
                            runOnUiThread(() -> {
                                Toast.makeText(getActivity(), "No Connection...", Toast.LENGTH_LONG).show();
                                startButton.setEnabled(true);
                                startButton.setTextSize(16);
                                startButton.setText("Restart Test");
                            });
                            getSpeedTestHostsHandler = null;
                            return;
                        }
                    }


                    SparseArray<String> mapKey = getSpeedTestHostsHandler.getMapKey();
                    SparseArray<List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                    double selfLat = getSpeedTestHostsHandler.getSelfLat();
                    double selfLon = getSpeedTestHostsHandler.getSelfLon();
                    double tmp = 19349458;
                    double dist = 0.0;
                    int findServerIndex = 0;

                    for (int i = 0; i < mapKey.size(); i++) {
                        int index = mapKey.keyAt(i);
                        String value = mapKey.valueAt(index);

                        //Log.i(TAG, "run: " + index + "|" + value);


                        if (tempBlackList.contains(mapValue.get(index).get(5))) {
                            continue;
                        }

                        Location source = new Location("Source");
                        source.setLatitude(selfLat);
                        source.setLongitude(selfLon);

                        List<String> ls = mapValue.get(index);
                        Location dest = new Location("Dest");
                        dest.setLatitude(Double.parseDouble(ls.get(0)));
                        dest.setLongitude(Double.parseDouble(ls.get(1)));

                        double distance = source.distanceTo(dest);
                        if (tmp > distance) {
                            tmp = distance;
                            dist = distance;
                            findServerIndex = index;
                        }

                    }

                    String uploadAddr = mapKey.get(findServerIndex);
                    final List<String> info = mapValue.get(findServerIndex);
                    final double distance = dist;

                    runOnUiThread(() -> {
                        if (info != null) {
                            startButton.setTextSize(13);
                            startButton.setText(String.format("Hosted by %s (%s) [%s km]",

                                    (Const.KEY_DEMO) ? OtUtils.generateRandomString() : info.get(5),
                                    (Const.KEY_DEMO) ? OtUtils.generateRandomString() : info.get(3),

                                    new DecimalFormat("#.##").format(distance / 1000)));
                        } else {
                            Log.i(TAG, "run: " + "null");
                        }
                    });


                    final LinearLayout chartPing = view.findViewById(R.id.chartPing);
                    XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                    XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                    pingFill.setColor(Color.parseColor("#4d5a6a"));
                    pingRenderer.addFillOutsideLine(pingFill);
                    pingRenderer.setDisplayChartValues(false);
                    pingRenderer.setShowLegendItem(false);
                    pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                    pingRenderer.setLineWidth(5);
                    final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                    multiPingRenderer.setXLabels(0);
                    multiPingRenderer.setYLabels(0);
                    multiPingRenderer.setZoomEnabled(false);
                    multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
                    multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                    multiPingRenderer.setPanEnabled(true, true);
                    multiPingRenderer.setZoomButtonsVisible(false);
                    multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                    multiPingRenderer.addSeriesRenderer(pingRenderer);


                    final LinearLayout chartDownload = view.findViewById(R.id.chartDownload);
                    XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                    XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                    downloadFill.setColor(Color.parseColor("#4d5a6a"));
                    downloadRenderer.addFillOutsideLine(downloadFill);
                    downloadRenderer.setDisplayChartValues(false);
                    downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
                    downloadRenderer.setShowLegendItem(false);
                    downloadRenderer.setLineWidth(5);
                    final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                    multiDownloadRenderer.setXLabels(0);
                    multiDownloadRenderer.setYLabels(0);
                    multiDownloadRenderer.setZoomEnabled(false);
                    multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
                    multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                    multiDownloadRenderer.setPanEnabled(false, false);
                    multiDownloadRenderer.setZoomButtonsVisible(false);
                    multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                    multiDownloadRenderer.addSeriesRenderer(downloadRenderer);


                    final LinearLayout chartUpload = view.findViewById(R.id.chartUpload);
                    XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                    XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                    uploadFill.setColor(Color.parseColor("#4d5a6a"));
                    uploadRenderer.addFillOutsideLine(uploadFill);
                    uploadRenderer.setDisplayChartValues(false);
                    uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
                    uploadRenderer.setShowLegendItem(false);
                    uploadRenderer.setLineWidth(5);
                    final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                    multiUploadRenderer.setXLabels(0);
                    multiUploadRenderer.setYLabels(0);
                    multiUploadRenderer.setZoomEnabled(false);
                    multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                    multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                    multiUploadRenderer.setPanEnabled(false, false);
                    multiUploadRenderer.setZoomButtonsVisible(false);
                    multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                    multiUploadRenderer.addSeriesRenderer(uploadRenderer);


                    runOnUiThread(() -> {
                        pingTextView.setText("0 ms");
                        chartPing.removeAllViews();
                        downloadTextView.setText("0 Mbps");
                        chartDownload.removeAllViews();
                        uploadTextView.setText("0 Mbps");
                        chartUpload.removeAllViews();
                    });
                    final List<Double> pingRateList = new ArrayList<>();
                    final List<Double> downloadRateList = new ArrayList<>();
                    final List<Double> uploadRateList = new ArrayList<>();
                    Boolean pingTestStarted = false;
                    Boolean pingTestFinished = false;
                    Boolean downloadTestStarted = false;
                    Boolean downloadTestFinished = false;
                    Boolean uploadTestStarted = false;
                    Boolean uploadTestFinished = false;


                    if (info != null) {
                        final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
                        final HttpDownloadTest downloadTest = new HttpDownloadTest(uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
                        final HttpUploadTest uploadTest = new HttpUploadTest(uploadAddr);


                        while (true) {
                            if (!pingTestStarted) {
                                pingTest.start();
                                pingTestStarted = true;
                            }
                            if (pingTestFinished && !downloadTestStarted) {
                                downloadTest.start();
                                downloadTestStarted = true;
                            }
                            if (downloadTestFinished && !uploadTestStarted) {
                                uploadTest.start();
                                uploadTestStarted = true;
                            }


                            if (pingTestFinished) {

                                if (pingTest.getAvgRtt() == 0) {
                                    System.out.println("Ping error...");
                                } else {

                                    runOnUiThread(() -> {
                                        if (pingTextView != null) {
                                            pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                        }
                                    });
                                }
                            } else {
                                pingRateList.add(pingTest.getInstantRtt());

                                runOnUiThread(() ->
                                        {
                                            if (pingTextView != null) {
                                                pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                            }
                                        }
                                );


                                runOnUiThread(() -> {

                                    XYSeries pingSeries = new XYSeries("");
                                    pingSeries.setTitle("");

                                    int count = 0;
                                    List<Double> tmpLs = new ArrayList<>(pingRateList);
                                    for (Double val : tmpLs) {
                                        pingSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(pingSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, multiPingRenderer);
                                    chartPing.addView(chartView, 0);

                                });
                            }


                            if (pingTestFinished) {
                                if (downloadTestFinished) {

                                    if (downloadTest.getFinalDownloadRate() == 0) {
                                        System.out.println("Download error...");
                                    } else {
                                        runOnUiThread(() -> downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps"));
                                    }
                                } else {

                                    double downloadRate = downloadTest.getInstantDownloadRate();
                                    downloadRateList.add(downloadRate);
                                    position = getPositionByRate(downloadRate);

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            rotate.setInterpolator(new LinearInterpolator());
                                            rotate.setDuration(100);
                                            barImageView.startAnimation(rotate);
                                            downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                                        }

                                    });
                                    lastPosition = position;


                                    runOnUiThread(() -> {

                                        XYSeries downloadSeries = new XYSeries("");
                                        downloadSeries.setTitle("");

                                        List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                        int count = 0;
                                        for (Double val : tmpLs) {
                                            downloadSeries.add(count++, val);
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries(downloadSeries);

                                        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, multiDownloadRenderer);
                                        chartDownload.addView(chartView, 0);
                                    });

                                }
                            }


                            if (downloadTestFinished) {
                                if (uploadTestFinished) {

                                    if (uploadTest.getFinalUploadRate() == 0) {
                                        System.out.println("Upload error...");
                                    } else {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                            }
                                        });
                                    }
                                } else {

                                    double uploadRate = uploadTest.getInstantUploadRate();
                                    uploadRateList.add(uploadRate);
                                    position = getPositionByRate(uploadRate);

                                    runOnUiThread(() -> {
                                        rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                        rotate.setInterpolator(new LinearInterpolator());
                                        rotate.setDuration(100);
                                        barImageView.startAnimation(rotate);
                                        uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()) + " Mbps");
                                    });
                                    lastPosition = position;


                                    runOnUiThread(() -> {

                                        XYSeries uploadSeries = new XYSeries("");
                                        uploadSeries.setTitle("");

                                        int count = 0;
                                        List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                        for (Double val : tmpLs) {
                                            if (count == 0) {
                                                val = 0.0;
                                            }
                                            uploadSeries.add(count++, val);
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries(uploadSeries);

                                        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, multiUploadRenderer);
                                        chartUpload.addView(chartView, 0);
                                    });

                                }
                            }


                            if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                                break;
                            }

                            if (pingTest.isFinished()) {
                                pingTestFinished = true;
                            }
                            if (downloadTest.isFinished()) {
                                downloadTestFinished = true;
                            }
                            if (uploadTest.isFinished()) {
                                uploadTestFinished = true;
                            }

                            if (pingTestStarted && !pingTestFinished) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                }
                            } else {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }

                    runOnUiThread(() -> {
                        startButton.setEnabled(true);
                        startButton.setTextSize(16);
                        startButton.setText("Restart Test");
                    });
                }
            }).start();
        });
    }

    private void runOnUiThread(Runnable runnable) {
        if (getActivity() != null && isAdded()) {
            getActivity().runOnUiThread(runnable);
        }
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        final int itemId = item.getItemId();
//        if (itemId == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
