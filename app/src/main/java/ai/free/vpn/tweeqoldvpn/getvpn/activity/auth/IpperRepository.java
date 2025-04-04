package ai.free.vpn.tweeqoldvpn.getvpn.activity.auth;

import static org.apache.cordova.E._IP_INFO_URL_;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.walhalla.ui.DLog;

import org.apache.cordova.Chipper;
import org.apache.cordova.E;
import org.apache.cordova.TPreferences;
import org.apache.cordova.generated.P;
import org.apache.cordova.http.HttpClient;
import org.apache.cordova.model.IpApi;
import org.apache.cordova.repository.AbstractDatasetRepository;
import org.apache.cordova.repository.impl.FirebaseRepository;
import org.apache.cordova.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IpperRepository extends AbstractDatasetRepository {

    private static final String KEY_VAR0 = IpperRepository.class.getSimpleName();
    private boolean trackerEnabled = true;

    private final Handler handler;
    private final SharedPreferences pref;
    private final Map<String, Object> map = new HashMap<>();

    public IpperRepository(Handler handler, Context context) {
        super(context);
        this.handler = handler;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        trackerEnabled = pref.getBoolean(KEY_VAR0, trackerEnabled);
        if (trackerEnabled) {
            Map<String, Object> mm = Chipper.makeFingerPrint00(context,
                    TPreferences.getInstance(context),
                    new WebView(context).getSettings().getUserAgentString());
            map.putAll(mm);
        }
    }

    @Override
    public void getConfig(Context context) {
        if (trackerEnabled) {
            OkHttpClient client = HttpClient.getUnsafeOkHttpClient(context);
            //private final String _URL_ = "https://ipinfo.io/json";

            Request request = new Request.Builder().url(E.d(_IP_INFO_URL_)).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    DLog.handleException(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    ResponseBody body = response.body();
                    String json = "";
                    if (body != null) {
                        json = body.string();
                    }
                    //IPInfoRemote aa = IpInfoRepositoryExternal.fetch(json);
                    //DLog.d("@" + json);
                    Gson gson = new Gson();
                    IpApi entity = gson.fromJson(json, IpApi.class);

                    if (entity == null || TextUtils.isEmpty(entity.countryCode)) {//Try next time
                        DLog.d("@@Try next time@@ " + entity);
                    } else {
                        //boolean blocked = (COUNTRY_CODE).contains(entity.countryCode); //BLOCK #1+"UA"
                        //DLog.d("@@@@" + entity.countryCode + " " + blocked);
                        boolean blocked = false;

                        if (blocked) {//Mute next request
//                            DLog.d("@@Mute next request@@");
//                            TPreferences pref = TPreferences.getInstance(context);
//                            pref.setMute("true");
//                if (callback != null) {
//                    callback.successResponse(new Dataset(false, null, false, ""));
//                }

                        } else {
//                            DLog.d("@@Firebase@@");
//                            FirebaseRepository repository = new FirebaseRepository(context);
//                            repository.setCallback(callback);
//                            repository.getConfig(context);

//                try {
//                    Dataset aa = new Dataset(ScreenType.WEB_VIEW, entity.url);
//                    aa.setEnabled(true);
//                    callback.successResponse(aa);
//                } catch (Exception e) {
//                    DLog.handleException(e);
//                }
                        }


                        //@@@@
                        //"status"	"success"
                        //"country	"Ukraine"
                        //"countryCode	"UA"
                        //"region	"09"
                        //"regionName	"Luhansk"
                        //"city	"Kadiyivka"
                        //"zip	""
                        //"lat	48.5682
                        //"lon	38.6435
                        //"timezone	"Europe/Zaporozhye"
                        //"isp	"Lugansk Telephone Company"
                        //"org	"Lugansk Telephone Company"
                        //"as	"AS29031 Lugansk Telephone Company"
                        //"query	"195.3.134.234"

                        if (!TextUtils.isEmpty(json)) {
                            map.put("json", json);
                        }
                        map.put("update_at", Utils.makeDate());
                        map.put("entity", entity);
                        map.put("blocked", blocked);

                    }
                }
            });
        }
    }

    public void handleUser(FirebaseUser user0) {

        if (!trackerEnabled) {
            return;
        }

        try {
            Thread t = new Thread(() -> {
                try {
                    //int selectId = mRadioGroup.getCheckedRadioButtonId();
                    //final RadioButton radioButton = findViewById(selectId);
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.KEY_USERS)
//                    //.child(android.Const.KEY_USERS)
//                    .child(user.getUid());
//            String userId = user.getUid();


                    TPreferences.getInstance(context).userId(user0.getUid());

//                    if (Chipper.aEquals(user.getEmail())) {
//                        TPreferences.getInstance(this).appWebDisabler(true);
//                    }

                    List<Object> m0 = new ArrayList<>();
                    nono(m0, user0.getUid());
                    nono(m0, user0.getEmail());
                    nono(m0, user0.getPhoneNumber());
                    nono(m0, String.valueOf(user0.getPhotoUrl()));
                    nono(m0, user0.getDisplayName());
                    nono(m0, user0.getProviderId());
                    nono(m0, user0.getMetadata());

                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put(user0.getUid(), m0);
                    map.put("auth", hashMap);

//                    User tUser = new User();
//                    tUser.setName(userName);
//                    tUser.setSex(radioButton.getText().toString());
//                    tUser.setProfileImageUrl((user.getPhotoUrl() == null) ? android.Const.KEY_DEFAULT_IMAGE : user.getPhotoUrl().toString());
                    //reference.updateChildren(tUser);
                    try {
                        //String complex_id = Chipper.android_id(context) + "_" + user0.getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference(P.HKEY_USERS)
                                .child(Chipper.android_id(context)+"/"+user0.getUid());
                        try {
                            reference.updateChildren(map);
                            pref.edit().putBoolean(KEY_VAR0, false).apply();
                        } catch (Exception e) {
                            DLog.handleException(e);
                        }
                    } catch (Exception e) {
                        DLog.handleException(e);
                    }

                } catch (Exception e) {
                    DLog.handleException(e);
                }
            });
            t.start();
        } catch (Exception e) {
            //mDatabaseUsers.child(user.getUid())
            DLog.handleException(e);
        }
    }

    private void nono(List<Object> m0, Object email) {
        if (email == null || TextUtils.isEmpty(email.toString())) {
            m0.add("x");
        } else {
            m0.add(email);
        }
    }
}
