package ai.free.vpn.tweeqoldvpn.getvpn.activity;

import android.content.Context;
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

import java.io.InputStream;
import java.util.List;


public class CountryListAdapter
        extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private final List<TObject> countryList;
    private final LayoutInflater inflater;
    private final Context context;
    private Callback callback;

    public CountryListAdapter(Context context, List<TObject> countryList) {
        this.context = context;
        this.countryList = countryList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_config, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TObject obj = countryList.get(position);
        holder.bind(obj, context, callback);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void setOnItemClickListener(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView flag;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(android.R.id.text1);
            flag = itemView.findViewById(R.id.imageFlag);
        }

        public void bind(TObject obj, Context context, Callback callback) {
            name.setText(obj.name);
            try {
//            String raw;
//            if ("".equals(obj.flag)) {
//                raw = "ic_unknown";
//            } else {
//                raw = obj.flag;
//            }
//            int resourceId = context.getResources().getIdentifier(raw, "raw", context.getPackageName());
//            if (resourceId != 0) {
//                InputStream ims = context.getResources().openRawResource(resourceId);
//                Drawable d = Drawable.createFromStream(ims, null);
//                flag.setImageDrawable(d);
//                ims.close();
//            } else {
//                flag.setImageResource(R.drawable.ic_unknown);
//            }

                String raw;
                if ("".equals(obj.flag)) {
                    flag.setImageResource(R.drawable.ic_unknown);
                } else {
                    raw = "raw/" + obj.flag + ".png";
                    InputStream ims = context.getAssets().open((raw).toLowerCase());
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    flag.setImageDrawable(d);
                    ims.close();
                }

                //flag.setImageResource(R.drawable.ic_unknown);
//            R.raw.your_image_file_name
//            flag.setImageResource(d);

//            String fileName = "" + obj.flag + "";
//            int resourceId = context.getResources().getIdentifier(fileName, null, pn);
//            if (resourceId != 0) {
//                flag.setImageResource(resourceId);
//            } else {
//                flag.setImageResource(R.raw.ic_unknown);
//            }

            } catch (Exception ex) {
                DLog.handleException(ex);
            }
            itemView.setOnClickListener(v -> callback.onClick(getLayoutPosition()));
        }
    }
}
