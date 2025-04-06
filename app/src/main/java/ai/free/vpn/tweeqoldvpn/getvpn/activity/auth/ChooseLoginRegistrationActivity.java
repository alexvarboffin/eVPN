package ai.free.vpn.tweeqoldvpn.getvpn.activity.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import ai.free.vpn.tweeqoldvpn.getvpn.R;
import com.google.firebase.auth.FirebaseUser;
import com.walhalla.ui.DLog;

import org.apache.cordova.constants.TableField;

import ai.free.vpn.tweeqoldvpn.getvpn.activity.SplashActivityVpn;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.base.BaseActivityImpl;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.main.MainActivity;


/**
 * ChooseLoginRegistrationActivity 0
 * RegistrationActivity 1
 */
public class ChooseLoginRegistrationActivity
        extends BaseActivityImpl implements View.OnClickListener {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> {
                // Handle the FirebaseAuthUIAuthenticationResult
                if (manager != null) {
                    manager.handleSignInResult(result);
                }
            });

    private IpperRepository presenter;

    @Override
    public void successAuth(FirebaseUser user) {
        if (user != null) {
            presenter.handleUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(SplashActivityVpn.COOL_FLAGH);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void falseAuth(FirebaseUser user) {
        DLog.e("FirebaseUser: " + user);
    }
    //fui_activity_register_phone

    @Override
    public void signInError(String message) {
        DLog.e("FirebaseUser: " + message);
    }

    @Override
    public void launch(Intent signInIntent) {
        signInLauncher.launch(signInIntent);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//// Remember that you should never show the action bar if the
//// status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        super.onCreate(savedInstanceState);
        presenter = new IpperRepository(new Handler(), this);
        presenter.getConfig(this);

        Button mLogin = findViewById(R.id.login);
        Button mRegister = findViewById(R.id.action_register);


        //FbAuthManager authorization = new FbAuthManager();
        //authorization.signOut(this);
        //authorization.launchLogin(this);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mLogin.setVisibility(View.GONE);

        TextView t2 = findViewById(R.id.text2);
        t2.setText(R.string.app_privacy_policy_bottom);
        t2.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_choose_login_registration;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                this.finish();
        } else if (id == R.id.action_register) {
//                Intent obj = new Intent(this, RegistrationActivity.class);
//                startActivity(obj);
//                this.finish();
            DLog.d("@@");
            if (manager != null) {
                manager.launchLogin(this);
            }
        }
    }
}
