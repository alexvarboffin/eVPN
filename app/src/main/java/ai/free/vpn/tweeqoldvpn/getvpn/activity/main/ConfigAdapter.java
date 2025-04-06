package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.walhalla.ui.DLog;
import com.walhalla.vpnapp.model.Server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigAdapter
        extends RecyclerView.Adapter<ConfigAdapter.ViewHolder> {

    Clb clb;

    public ConfigAdapter(Clb clb) {
        this.clb = clb;
    }

    public interface Clb {

        void onItemClick(Server configFileName);
    }

    private List<Server> configList = new ArrayList<>();

    public void setConfigs(List<Server> configList) {
        this.configList = configList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Server configFileName = configList.get(position);
        holder.bind(configFileName);
    }

    @Override
    public int getItemCount() {
        return configList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView configNameTextView;
        public ImageView flag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            configNameTextView = itemView.findViewById(android.R.id.text1);
            flag = itemView.findViewById(R.id.imageFlag);
            itemView.setOnClickListener(this);
        }

        public void bind(Server server) {
            //configNameTextView.setText(server.getCity());
            configNameTextView.setText(server.getCountryLong());
            try {
                String raw = "";
                String code = server.getCountryShort();
                if ("".equals(code)) {
                    flag.setImageResource(R.drawable.ic_unknown);
                } else {
                    raw = "raw/" + code.toLowerCase() + ".png";
                    InputStream ims = itemView.getContext().getAssets().open((raw).toLowerCase());
                    Drawable d = Drawable.createFromStream(ims, null);
                    flag.setImageDrawable(d);
                    ims.close();
                }

            } catch (Exception ex) {
                DLog.handleException(ex);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Server configFileName = configList.get(position);
                clb.onItemClick(configFileName);
            }
        }
    }
}
