package ai.free.vpn.tweeqoldvpn.getvpn.tos;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


public class TOSFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_privacypolicy, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
//            upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//            actionBar.setHomeAsUpIndicator(upArrow);
//        }

        WebView webView = view.findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://pharid.com/privacy-policy/");
//        webView.setWebViewClient(new MyWebViewClient());
//        webView.setHorizontalScrollBarEnabled(false);

        webView.loadDataWithBaseURL(null,
                loadFromAsset(getActivity(), "privacy_policy.html"),
                "text/html", "UTF-8", null);

        webView.requestFocus();

//        
//        int year = calendar.get(Calendar.YEAR);
//        ((TextView)view.findViewById(R.id.copyright))
//                .setText(getString(R.string.all_rights_reserved, "" + year));
    }

    public static String loadFromAsset(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

//    private static class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
