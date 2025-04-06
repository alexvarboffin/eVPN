package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

import static com.walhalla.ui.UConst.GOOGLE_PLAY_CONSTANT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import ai.free.vpn.tweeqoldvpn.getvpn.App;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.CountryListAdapter;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.TObject;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.home.MainContract1;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.home.MainPresenter1;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo.VPNInfoActivityVpnAuth;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpnlist.VPNListActivityVpnAuth;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Module_U;
import com.walhalla.vpnapp.model.Server;

import ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper;
import ai.free.vpn.tweeqoldvpn.getvpn.util.CountriesNames;
import ai.free.vpn.tweeqoldvpn.getvpn.util.PropertiesService;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.walhalla.vpnapp.repository.ServerRepository;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Random;


public class Home extends Fragment implements MainContract1.View {
    protected int widthWindow;
    protected int heightWindow;

    DecoView arcView, arcView2;

    private PopupWindow popupWindow;
    private RelativeLayout homeContextRL;

    TextView centree;


    CardView mCardViewShare;
    private MainPresenter1 presenter;

    private Button hello;
    private DBHelper dbHelper;
    private Map<String, String> localeCountries;
    private List<Server> countryList = new ArrayList<>();
    private PropertiesService properties;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter1(this);
        dbHelper = ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper.newInstance(getActivity().getApplicationContext());
        properties = new PropertiesService(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        int count=9;
//        if (savedInstanceState != null) {
//            count = savedInstanceState.getInt("aa");
//        }
//        DLog.d("@@@@@@@@@@@@@@@"+count);
//        MobileAds.initialize(this, initializationStatus -> {
//            //R.string.admob_app_id
//        });
        hello = view.findViewById(R.id.elapse2);
        homeContextRL = view.findViewById(R.id.homeContextRL);
        dbHelper.getUniqueCountries(new ServerRepository.DataCallback<List<Server>>() {
            @Override
            public void successResult(List<Server> data) {
                DLog.d("@@@@@@@@@@@@" + data);
                if (data != null) {
                    countryList = data;
                }
            }

            @Override
            public void showError(String m) {
                DLog.d("@@@@@@@@@@@@" + m);
            }
        });


        if (isVPNNoConnected()) {
            showNoVPNConnected();
        } else {
            showVPNConnected();
        }

        centree = view.findViewById(R.id.centree);
        arcView = view.findViewById(R.id.dynamicArcView2);
        arcView2 = view.findViewById(R.id.dynamicArcView3);

        long totalServ = dbHelper.getCount();

        String totalServers = String.format(getResources().getString(R.string.total_servers), totalServ);
        centree.setText(totalServers);

        arcView2.setVisibility(View.VISIBLE);
        arcView.setVisibility(View.GONE);

        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 0)
                .setInterpolator(new AccelerateInterpolator())
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#00000000"))
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#ffffff"))
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .build();

        int series1Index2 = arcView.addSeries(seriesItem2);
        Random ran2 = new Random();
        int proc = ran2.nextInt(10) + 5;
        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(600)
                .build());


        arcView.addEvent(new DecoEvent.Builder(proc).setIndex(series1Index2).setDelay(2000).setListener(new DecoEvent.ExecuteEventListener() {
            @Override
            public void onEventStart(DecoEvent decoEvent) {


            }

            @Override
            public void onEventEnd(DecoEvent decoEvent) {


                long totalServ = dbHelper.getCount();
                showTotalServers(totalServ);//@@@@@@@@@@@@@@@


            }
        }).build());


        mCardViewShare = view.findViewById(R.id.CardViewShare);

        mCardViewShare.setOnClickListener(v -> {
            final String text = "Check out "
                    + getResources().getString(R.string.app_name)
                    + ", the free app for VPN and proxy with "
                    + getString(R.string.app_name) + ". "
                    + GOOGLE_PLAY_CONSTANT + v.getContext().getPackageName();
            Module_U.shareThisApp(getActivity(), text);
        });


        (view.findViewById(R.id.CardViewMore)).setOnClickListener(v -> {
            Module_U.moreApp(getActivity());
        });

        CardView button1 = view.findViewById(R.id.homeBtnRandomConnection);
        button1.setOnClickListener(v -> {
            sendTouchButton("homeBtnRandomConnection");
            Server randomServer = getRandomServer();
            if (randomServer != null) {
                newConnecting(randomServer, true, true);
            } else {
                String randomError = String.format(getResources().getString(R.string.error_random_country), properties.getSelectedCountry());
                Toast.makeText(getActivity(), randomError, Toast.LENGTH_LONG).show();
            }


        });


        localeCountries = CountriesNames.getCountries();


        CardView button2 = view.findViewById(R.id.homeBtnChooseCountry);
        button2.setOnClickListener(v -> {
            sendTouchButton("homeBtnChooseCountry");
            chooseCountry(localeCountries);
        });


        //CardView button = view.findViewById(R.id.button);
        //button.setOnClickListener(v -> startActivity(new Intent(getActivity(), SpeedTestFragment.class)));


        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        widthWindow = dm.widthPixels;
        heightWindow = dm.heightPixels;
    }

    private boolean isVPNNoConnected() {
        return App.connectedServer == null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isVPNNoConnected()) {
            showNoVPNConnected();
        } else {
            showVPNConnected();
        }
        //@@@    invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


//    @Override
//    protected boolean useHomeButton() {
//        return true;
//    }

    @SuppressLint("NonConstantResourceId")
    public void homeOnClick(View view) {
        int id = view.getId();
        if (id == R.id.homeBtnChooseCountry) {
            sendTouchButton("homeBtnChooseCountry");
            chooseCountry(localeCountries);
        } else if (id == R.id.homeBtnRandomConnection) {
            sendTouchButton("homeBtnRandomConnection");
            Server randomServer = getRandomServer();
            if (randomServer != null) {
                newConnecting(randomServer, true, true);
            } else {
                String randomError = String.format(getResources().getString(R.string.error_random_country), properties.getSelectedCountry());
                Toast.makeText(getActivity(), randomError, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void newConnecting(Server server, boolean fastConnection, boolean autoConnection) {
        if (server != null) {
            Intent intent = VPNInfoActivityVpnAuth.newConnectingInstance(getActivity(), server, fastConnection, autoConnection);
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

    public static void sendTouchButton(String button) {
        DLog.d("@[event]@" + button);
    }

    private void chooseCountry(Map<String, String> map) {
        View view = initPopUp(R.layout.choose_country, 0.6f, 0.8f, 0.8f, 0.7f);

        final List<TObject> list = new ArrayList<>();
        for (Server server : countryList) {
            String localeCountryName = map.get(server.getCountryShort()) != null ?
                    map.get(server.getCountryShort()) : server.getCountryLong();
            list.add(new TObject(localeCountryName, server.getCountryShort()));
        }

        RecyclerView recyclerView = view.findViewById(R.id.homeCountryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        CountryListAdapter adapter = new CountryListAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            popupWindow.dismiss();
            Server server = countryList.get(position);
            DLog.d("@@" + server);
            VPNListActivityVpnAuth.newInstance(getActivity(), server.getCountryShort());
        });
        popupWindow.showAtLocation(homeContextRL, Gravity.CENTER, 0, 0);
    }

    private View initPopUp(int resourse,
                           float landPercentW,
                           float landPercentH,
                           float portraitPercentW,
                           float portraitPercentH) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourse, null);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            popupWindow = new PopupWindow(
                    view,
                    (int) (widthWindow * landPercentW),
                    (int) (heightWindow * landPercentH)
            );
        } else {
            popupWindow = new PopupWindow(
                    view,
                    (int) (widthWindow * portraitPercentW),
                    (int) (heightWindow * portraitPercentH)
            );
        }


        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        return view;
    }


//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//
//        super.onSaveInstanceState(outState);
//        outState.putInt("aa", 999);
//        DLog.d("------------------");
//    }
    //onSaveInstanceState onRestoreInstanceState

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        int count = savedInstanceState.getInt("aa");
//        DLog.d("+++++++++++"+count);
//    }


    @Override
    public void showNoVPNConnected() {
        hello.setText(R.string.no_vpn_connected);
        hello.setBackgroundResource(R.drawable.vpn_connection_status);
    }

    @Override
    public void showVPNConnected() {
        hello.setText(R.string.state_connected);
        hello.setBackgroundResource(R.drawable.button3);
    }

    @Override
    public void showTotalServers(long totalServers) {
        if (isAdded()) {
            String totalServers0 = String.format(getResources().getString(R.string.total_servers), totalServers);
            centree.setText(totalServers0);
        }
    }
}
