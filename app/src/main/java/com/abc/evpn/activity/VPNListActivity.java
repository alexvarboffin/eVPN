package com.abc.evpn.activity;

import android.content.Intent;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abc.evpn.R;
import com.abc.evpn.adapter.ServerListAdapter;
import com.abc.evpn.model.Server;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;

import java.util.List;

import de.blinkt.openvpn.core.VpnStatus;

public class VPNListActivity extends BaseActivity {
    private ListView listView;
    private ServerListAdapter serverListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpnlist);
        

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }


        AdView mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdMobAdView.loadAd(adRequest);

//        final InterstitialAd mInterstitial = new InterstitialAd(this);
//        mInterstitial.setAdUnitId(getString(R.string.interstitial_ad_unit));
//        mInterstitial.loadAd(new AdRequest.Builder().build());
//        mInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//
//                super.onAdLoaded();
//                if (mInterstitial.isLoaded()) {
//                    mInterstitial.show();
//                }
//            }
//        });

        if (!VpnStatus.isVPNActive())
            connectedServer = null;

        listView = (ListView) findViewById(R.id.list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();

        buildList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void ipInfoResult() {
        serverListAdapter.notifyDataSetChanged();
    }

    private void buildList() {
        String country = getIntent().getStringExtra(MainActivity.EXTRA_COUNTRY);
        final List<Server> serverList = dbHelper.getServersByCountryCode(country);
        serverListAdapter = new ServerListAdapter(this, serverList);

        TextView countryname = (TextView) findViewById(R.id.elapse);
        countryname.setText(country);

        String code = getIntent().getStringExtra(MainActivity.EXTRA_COUNTRY).toLowerCase();
        if (code.equals("do"))
            code = "dom";

        ((ImageView) findViewById(R.id.imgv))
                .setImageResource(
                        getResources().getIdentifier(code,
                                "drawable",
                                getPackageName()));

        listView.setAdapter(serverListAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Server server = serverList.get(position);
            BaseActivity.sendTouchButton("detailsServer");
            Intent intent = new Intent(VPNListActivity.this, VPNInfoActivity.class);
            intent.putExtra(Server.class.getCanonicalName(), server);
            VPNListActivity.this.startActivity(intent);
        });

        getIpInfo(serverList);
    }
}