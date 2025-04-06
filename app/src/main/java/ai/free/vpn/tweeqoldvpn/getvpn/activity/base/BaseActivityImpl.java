package ai.free.vpn.tweeqoldvpn.getvpn.activity.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ai.free.vpn.tweeqoldvpn.getvpn.auth.AuthContract;
import ai.free.vpn.tweeqoldvpn.getvpn.auth.FireBaseAuthManager;



public abstract class BaseActivityImpl extends AppCompatActivity
        implements AuthBaseActivityI, AuthContract.AuthView {
    protected AuthContract.AuthPresenter manager;

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        manager = new FireBaseAuthManager(this);
        getLifecycle().addObserver(manager);
//        user = mAuth0.getCurrentUser();
//        successAuth(user);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }


    protected void logoutUser() {
        manager.logOut(this);
        falseAuth(null);
        this.finish();
    }

    @Override
    public void printMessage(String data) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("@@@");
            getSupportActionBar().setSubtitle(data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //DLog.d("onResume: " + this.getClass().getSimpleName());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // NOT SET NULL !!! manager = null;
    }

    /**
     *
     * ChooseLoginRegistrationActivity
     * LoginActivity
     *  RegistrationActivity
     */
//    @Override
//    public void successAuth(FirebaseUser user) {
//        DLog.i("Current user: -> " + user);
//        if (user != null) {
//            Intent intent = new Intent(this, AuthActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}
