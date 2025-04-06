package ai.free.vpn.tweeqoldvpn.getvpn.activity.vpnlist;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import ai.free.vpn.tweeqoldvpn.getvpn.App;

import com.walhalla.ui.DLog;
import com.walhalla.vpnapp.model.Server;

import ai.free.vpn.tweeqoldvpn.getvpn.util.ConnectionQuality;
import ai.free.vpn.tweeqoldvpn.getvpn.util.CountriesNames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServerListAdapter extends
        RecyclerView.Adapter<ServerViewHolder> {

    private List<Server> serverList = new ArrayList<>();
    private final Context context;
    private final Map<String, String> localeCountries;
    private Callback callback;

    public ServerListAdapter(Context context, List<Server> serverList) {
        this.context = context;
        this.serverList = serverList;
        this.localeCountries = CountriesNames.getCountries();
    }

    public ServerListAdapter(VPNListActivityVpnAuth context) {
        this.context = context;
        this.serverList = new ArrayList<>();
        this.localeCountries = CountriesNames.getCountries();
    }

    @NonNull
    @Override
    public ServerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vpn_row, parent, false);
        return new ServerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServerViewHolder holder, int position) {
        final Server server = serverList.get(position);

        String code = server.getCountryShort().toLowerCase();
        if (code.equals("do"))
            code = "dom";

        getDrawableByCode(holder.imageFlag, code);
        holder.imageConnect.setImageResource(qualityIcon(server.getQuality()));

        holder.bind(server);

        String localeCountryName = localeCountries.get(server.getCountryShort()) != null ?
                localeCountries.get(server.getCountryShort()) : server.getCountryLong();
        holder.textCountry.setText(localeCountryName);

        if (App.connectedServer != null && App.connectedServer.hostName.equals(server.hostName)) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.activeServer));
        } else if (server.getType() == 1) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.additionalServer));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        holder.itemView.setOnClickListener(v -> callback.onClick(server));
    }


    @Override
    public int getItemCount() {
        return serverList.size();
    }

    @SuppressLint("DiscouragedApi")
    private int qualityIcon(int quality) {
        return context.getResources().getIdentifier(ConnectionQuality.getConnectIcon(quality), "drawable", context.getPackageName());
    }

    @SuppressLint("DiscouragedApi")
    private void getDrawableByCode(ImageView flag, String code) {
//        Resources resources = context.getResources();
//        int resourceId = resources.getIdentifier(code, "drawable", context.getPackageName());
//        if (resourceId != 0) {
//            return ContextCompat.getDrawable(context, resourceId);
//        } else {
//            // Return a default drawable if the resource is not found
//            return ContextCompat.getDrawable(context, R.drawable.ic_unknown);
//        }
//        holder.imageFlag.setImageDrawable();
        try {
            String raw;
            if ("".equals(code)) {
                flag.setImageResource(R.drawable.ic_unknown);
            } else {
                raw = "raw/" + code + ".png";
                InputStream ims = flag.getContext().getAssets().open((raw).toLowerCase());
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                flag.setImageDrawable(d);
                ims.close();
            }
        } catch (Exception ex) {
            DLog.handleException(ex);
        }
    }

    public void swap(List<Server> serverList) {
        this.serverList.clear();
        this.serverList.addAll(serverList);
        notifyDataSetChanged();
    }



    public void setOnItemClickListener(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onClick(Server server);
    }
}
