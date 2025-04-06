package ai.free.vpn.tweeqoldvpn.getvpn.activity.vpnlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;
import com.walhalla.vpnapp.model.Server;

import ai.free.vpn.tweeqoldvpn.getvpn.Const;
import ai.free.vpn.tweeqoldvpn.getvpn.OtUtils;

public class ServerViewHolder extends RecyclerView.ViewHolder {
    ImageView imageFlag;
    ImageView imageConnect;
    TextView textHostName;
    TextView textIP;
    TextView textCity;
    TextView textCountry;

    public ServerViewHolder(View itemView) {
        super(itemView);
        imageFlag = itemView.findViewById(R.id.imageFlag);
        imageConnect = itemView.findViewById(R.id.imageConnect);
        textHostName = itemView.findViewById(R.id.textHostName);
        textIP = itemView.findViewById(R.id.textIP);
        textCity = itemView.findViewById(R.id.textCity);
        textCountry = itemView.findViewById(R.id.textCountry);
    }

    public void bind(Server server) {

        String tmp = server.getIp();
        String tmpHostName = server.hostName;

//        tmp.replace("2", "**")
//                .replace("1", "**")
//                .replace("8", "**")
//        tmpHostName.replace("vpn", "twvpn")

        textCity.setText((Const.KEY_DEMO) ? OtUtils.generateRandomString() : server.getCity());
        String mm = "" + tmpHostName;
        textHostName.setText(mm);
        textIP.setText(tmp);

    }
}