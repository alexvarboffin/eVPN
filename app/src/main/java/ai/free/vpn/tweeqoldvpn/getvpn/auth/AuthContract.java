package ai.free.vpn.tweeqoldvpn.getvpn.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.DefaultLifecycleObserver;

import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseUser;

public interface AuthContract {
    public interface AuthPresenter extends DefaultLifecycleObserver {
        boolean isLogin();

        void logOut(Context context);

        void launchLogin(Activity registrationActivity);


        boolean isSessionValid();

        String currentUId();

        void signIn(Activity activity, String email, String password);

        void handleSignInResult(FirebaseAuthUIAuthenticationResult result);
    }

    public interface AuthView {

        void printMessage(String hello);

        void successAuth(FirebaseUser user);

        void falseAuth(FirebaseUser user);

        void signInError(String message);

        void launch(Intent signInIntent);

        void toast(String message);
    }
}
