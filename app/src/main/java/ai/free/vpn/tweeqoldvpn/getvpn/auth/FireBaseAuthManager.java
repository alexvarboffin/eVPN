package ai.free.vpn.tweeqoldvpn.getvpn.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import ai.free.vpn.tweeqoldvpn.getvpn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.walhalla.ui.DLog;

import java.util.Arrays;
import java.util.List;


public class FireBaseAuthManager implements AuthContract.AuthPresenter {

    private final AuthContract.AuthView authView;


    public static final int RC_SIGN_IN = 477;

    private String phoneNumber = "111";
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    // Choose authentication providers




    //NOT USE IN VARIABLES ....CRASH...
    //Default FirebaseApp is not initialized in this process
    //private final List<AuthUI.IdpConfig> providers = Arrays.asList(...);


    private final FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            FirebaseAuth mAuth0 = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth0.getCurrentUser();
            final String msg = (user == null) ? "nul" : user.getEmail();
            DLog.d(": " + msg + "\t" + ((authView == null) ? "nul" : authView.getClass().getSimpleName()));
            //mainView.printMessage("User: ->" + msg);
            try {
                if (authView != null) {
                    if (user != null) {
                        authView.successAuth(user);
                        //DLog.d("User logged in");
                    } else {
                        //DLog.d("{} User not logged in {}");
                        authView.falseAuth(user);
                    }
                }
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    };

    public FireBaseAuthManager(@NonNull AuthContract.AuthView authView) {
        this.authView = authView;
        // Initialize Firebase Auth
        //DLog.d("CONNECTED --> " + authView.getClass().getSimpleName());
        //providers.add(new AuthUI.IdpConfig.FacebookBuilder().build());
    }

//    public void launchLogin(Activity activity) {
//        if (mAuth0.getCurrentUser() == null) {
//            //Toast.makeText(activity, "Please ", Toast.LENGTH_SHORT).show();
//
//            activity.startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(providers)
//                            //.setLogo(R.mipmap.ic_launcher)  // Set logo drawable
//                            .setTheme(R.style.AppTheme)      // Set theme
//                            .setTosAndPrivacyPolicyUrls(
//                                    activity.getString(R.string.url_privacy_policy),
//                                    activity.getString(R.string.url_privacy_policy)
//                            )
//                            //.setTheme(R.style.LoginTheme)
//                            .build(),
//                    RC_SIGN_IN);
//        }
//    }

    public void launchLogin(Activity activity) {
        try {

            final List<AuthUI.IdpConfig> providers = Arrays.asList(

                    new AuthUI.IdpConfig.EmailBuilder().build()
                    , new AuthUI.IdpConfig.GoogleBuilder().build()

                    , new AuthUI.IdpConfig.PhoneBuilder()
                            //.setDefaultCountryIso("ua")
                            .build()

                    //OR
//            new AuthUI.IdpConfig
//                    .PhoneBuilder()
//                    .setDefaultNumber("ua", "722287622")
//                    .build()


//
//            new AuthUI.IdpConfig.FacebookBuilder().build(),
//            new AuthUI.IdpConfig.TwitterBuilder().build(),
//            new AuthUI.IdpConfig.MicrosoftBuilder().build(),
//            new AuthUI.IdpConfig.YahooBuilder().build(),
//            new AuthUI.IdpConfig.AppleBuilder().build(),
//            new AuthUI.IdpConfig.AnonymousBuilder().build()
            );

            FirebaseAuth mAuth0 = FirebaseAuth.getInstance();
            if (mAuth0.getCurrentUser() == null) {

                Intent signInIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.mipmap.ic_launcher)  // Set logo drawable
                        .setTheme(R.style.AppTheme)      // Set theme
                        .setTosAndPrivacyPolicyUrls(
                                activity.getString(R.string.url_privacy_policy),
                                activity.getString(R.string.url_privacy_policy)
                        )
                        //.setTheme(R.style.LoginTheme)
                        .build();
                authView.launch(signInIntent);
            } else {
                DLog.d(":::::::::::::::: WWWWWWWWWWW ");
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }

    }

    @Override
    public boolean isSessionValid() {
        FirebaseAuth mAuth0 = FirebaseAuth.getInstance();
        return mAuth0.getCurrentUser() != null;
    }


    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void logOut(Context context) {

        try {
            //FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(context) //!!! - содержит FirebaseAuth.getInstance().signOut()
                    .addOnCompleteListener(task -> {
                        //DLog.d("@@ // Все данные пользователя удалены, теперь покажите форму выбора провайдера");
                        //showSignInOptions();
                    });
        } catch (Exception e) {
            DLog.handleException(e);
        }
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//                    }
//                });

//        try {
//
//            FirebaseApp aa = FirebaseApp.getInstance();
//            AuthUI.getInstance()
//                    .signOut(context)
//                    .addOnCompleteListener(task -> DLog.d("onComplete: " + task.getResult()));
//        } catch (Exception e) {
//            DLog.handleException(e);
//        }


//        Auth.GoogleSignInApi.signOut(context).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        FirebaseAuth.getInstance().signOut();
////                        Intent i1 = new Intent(MainActivity.this, GoogleSignInActivity.class);
////                        startActivity(i1);
////                        Toast.makeText(MainActivity.this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }


    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        AuthContract.AuthPresenter.super.onStart(owner);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        AuthContract.AuthPresenter.super.onCreate(owner);
        FirebaseAuth mAuth0 = FirebaseAuth.getInstance();
        mAuth0.addAuthStateListener(listener);///77
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        AuthContract.AuthPresenter.super.onStop(owner);
        try {
            FirebaseAuth mAuth0 = FirebaseAuth.getInstance();
            mAuth0.removeAuthStateListener(listener);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        AuthContract.AuthPresenter.super.onResume(owner);
        //final FirebaseUser user = mAuth0.getCurrentUser();
        //final String msg = (user == null) ? null : user.getEmail();
        //DLog.d("OnResume, current user: " + msg + "\t");
        //mainView.printMessage("User: ->" + msg);

        if (!isSessionValid()) {
            authView.falseAuth(null);
        }
    }


    //Вход по логину/паролю

    @Override
    public void signIn(Activity activity, String email, String password) {
        //__login_password(activity, email, password);
    }

    private void __login_password(Activity activity, String email, String password) {
        FirebaseAuth mAuth0 = FirebaseAuth.getInstance();
        mAuth0.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()) {
                        authView.signInError(task.getException().getMessage());
                    } else {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Log.i(TAG, "onClick: " + user.toString());
//                        if (nonNull(user)) {
//                            String userId = mAuth.getCurrentUser().getUid();
//                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(android.Const.KEY_USERS).child(userId);
//                            Map userInfo = new HashMap<>();
//                            userInfo.put(android.Const.KEY_NAME name);
//                            userInfo.put(android.Const.KEY_SEX, radioButton.getText().toString());
//                            userInfo.put("profileImageUrl", android.Const.KEY_DEFAULT_IMAGE);
//                            reference.updateChildren(userInfo);
//                        }
                    }
                });

//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    private void signInWithPhoneAuthCredential(Activity activity) {
//
//        AuthCredential credential= FirebaseAuth.getInstance().;
//        mAuth0.signInWithCredential(credential)
//                .addOnCompleteListener(activity, task -> {
//                    if (task.isSuccessful()) {
//                        // Авторизация прошла успешно
//                        FirebaseUser user = task.getResult().getUser();
//                        //Toast.makeText(MainActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Авторизация не удалась
//                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                            // Неверный SMS-код
//                            //Toast.makeText(MainActivity.this, "Неверный SMS-код", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // Ошибка авторизации
//                            //Toast.makeText(MainActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    private void sign000(Activity activity, PhoneAuthCredential credential) {
//        // Sign in with credential
//        FirebaseAuth.getInstance().signInWithCredential(nonAnonymousCredential)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult result) {
//                        // Copy over anonymous user data to signed in user
//                        ...
//                    }
//                });
    }

    public String currentUId() {
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(this);
        if (user != null) {
            return user.getUid();
        }
        return "";
    }


    //sms
//    private void sendVerificationCode(String phoneNumber) {
//        PhoneAuthProvider.getInstance()
//        .verifyPhoneNumber(
//                phoneNumber,
//                60,
//                TimeUnit.SECONDS,
//                this,
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onVerificationCompleted(PhoneAuthCredential credential) {
//                        // Код верификации был получен автоматически (без необходимости вводить вручную)
//                        signInWithPhoneAuthCredential(credential);
//                    }
//
//                    @Override
//                    public void onVerificationFailed(FirebaseException e) {
//                        if (e instanceof FirebaseAuthInvalidUserException) {
//                            // Неверный номер телефона
//                            Toast.makeText(MainActivity.this, "Неверный номер телефона", Toast.LENGTH_SHORT).show();
//                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                            // Неверный формат номера телефона
//                            Toast.makeText(MainActivity.this, "Неверный формат номера телефона", Toast.LENGTH_SHORT).show();
//                        } else if (e instanceof FirebaseTooManyRequestsException) {
//                            // Превышено количество запросов на верификацию
//                            Toast.makeText(MainActivity.this, "Превышено количество запросов на верификацию", Toast.LENGTH_SHORT).show();
//                        }
//
//                        Log.e(TAG, "onVerificationFailed", e);
//                    }
//
//                    @Override
//                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
//                        // Код верификации был успешно отправлен
//                        mVerificationId = verificationId;
//                        mResendToken = token;
//                    }
//                }
//        );
//    }

    @Override
    public void handleSignInResult(FirebaseAuthUIAuthenticationResult result) {
        //FirebaseAuthUIAuthenticationResult
        // {idpResponse=IdpResponse{mUser=
        // User{mProviderId='phone', mEmail='null', mPhoneNumber='+380722287622', mName='null', mPhotoUri=null}, mToken='null', mSecret='null', mIsNewUser='true', mException=null, mPendingCredential=null}, resultCode='-1}
        if (result.getResultCode() == Activity.RESULT_OK) {
            // Аутентификация успешна
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            authView.successAuth(user);
        } else {
            //DLog.d("@@@" + result);
            // Аутентификация не удалась
            IdpResponse response = result.getIdpResponse();
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            if (response != null && response.getError() != null) {
                //Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                DLog.d("@@@" + response.getError().getMessage());
                authView.toast(response.getError().getMessage());
            }
        }
    }
}
