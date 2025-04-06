package ai.free.vpn.tweeqoldvpn.getvpn.activity.vpnlist;

import android.content.Context;
import android.content.Intent;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import ai.free.vpn.tweeqoldvpn.getvpn.App;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.VpnAuthBaseActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.vpninfo.VPNInfoActivityVpnAuth;

import com.walhalla.vpnapp.MUtils;
import com.walhalla.vpnapp.model.Server;

import com.walhalla.vpnapp.repository.ServerRepository;

import java.util.List;

import de.blinkt.openvpn.core.VpnStatus;


//Server list

public class VPNListActivityVpnAuth extends VpnAuthBaseActivity {

    private ServerListAdapter serverListAdapter;
    static final String EXTRA_COUNTRY = "country";

    public static void newInstance(Context context, String countryCodeShort) {
        Intent intent = new Intent(context.getApplicationContext(), VPNListActivityVpnAuth.class);
        intent.putExtra(EXTRA_COUNTRY, countryCodeShort);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpnlist);


        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }


        if (!VpnStatus.isVPNActive())
        {
            App.connectedServer = null;
        }

        RecyclerView recyclerView = findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        serverListAdapter = new ServerListAdapter(this);
        recyclerView.setAdapter(serverListAdapter);
        serverListAdapter.setOnItemClickListener(server -> {
            VpnAuthBaseActivity.sendTouchButton("detailsServer");
            Intent intent = VPNInfoActivityVpnAuth.newInstance(VPNListActivityVpnAuth.this, server);
            VPNListActivityVpnAuth.this.startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

        String country0 = getIntent().getStringExtra(EXTRA_COUNTRY);
        if (country0 == null) {
            country0 = "";
        }
        buildList(country0);
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

    private void buildList(String country0) {
        TextView countryname = findViewById(R.id.elapse);
        countryname.setText(country0);
        String code = country0.toLowerCase();
        if (code.equals("do")) {
            code = "dom";
        }

        ImageView aa = findViewById(R.id.imgv);
        setDrawable(aa, code);

        dbHelper.getServersByCountryCode(country0, new ServerRepository.DataCallback<List<Server>>() {
            @Override
            public void successResult(List<Server> data) {
                serverListAdapter.swap(data);
                MUtils m = new MUtils();
                m.getIpInfo(data, VPNListActivityVpnAuth.this);
            }

            @Override
            public void showError(String m) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}