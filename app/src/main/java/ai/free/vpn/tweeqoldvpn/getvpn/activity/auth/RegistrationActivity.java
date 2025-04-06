//package ai.free.vpn.tweeqoldvpn.getvpn.activity.auth;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//
//import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
//import com.firebase.ui.auth.IdpResponse;
//import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
//import ai.free.vpn.tweeqoldvpn.getvpn.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.walhalla.ui.DLog;
//
//import org.apache.cordova.Chipper;
//import org.apache.cordova.TPreferences;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import ai.free.vpn.tweeqoldvpn.getvpn.Const;
//import ai.free.vpn.tweeqoldvpn.getvpn.activity.base.BaseActivityImpl;
//import ai.free.vpn.tweeqoldvpn.getvpn.activity.main.VpnMainActivity;
//
//public class RegistrationActivity extends BaseActivityImpl {
//    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
//            new FirebaseAuthUIActivityResultContract(),
//            (result) -> {
//                // Handle the FirebaseAuthUIAuthenticationResult
//                manager.handleSignInResult(result);
//                DLog.d("@@@@@@@@=========" + result);
//            });
//
//
//    private Button mRegister;
//    private EditText mEmail, mPassword, mName;
//
//    private RadioGroup mRadioGroup;
//    private String name;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mRegister = findViewById(R.id.action_register);
//        //mRegister.setEnabled(false);
//
//        mEmail = findViewById(R.id.email);
//        mEmail.setVisibility(View.GONE);
//
//        mPassword = findViewById(R.id.password);
//        mPassword.setVisibility(View.GONE);
//
//        mName = findViewById(R.id.name);
//        mName.setVisibility(View.GONE);
//
//        mRadioGroup = findViewById(R.id.radioGroup);
//        mRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> mRegister.setEnabled(true));
//
//
//        mRegister.setOnClickListener(view -> {
//
//
//            int selectId = mRadioGroup.getCheckedRadioButtonId();
//            if (selectId != -1) {
//                selectId = R.id.sex_male;
//            }
//            final RadioButton radioButton = findViewById(selectId);
//
//            if (radioButton.getText() == null) {
//                return;
//            }
//
//            final String email = mEmail.getText().toString();
//            final String password = mPassword.getText().toString();
//
//            name = mName.getText().toString();
//
//            try {
//
//                manager.launchLogin(this);
//
////                mAuth0.createUserWithEmailAndPassword(email, password)
////                        .addOnCompleteListener(RegistrationActivity.this, task -> {
////                            if (!task.isSuccessful()) {
////                                Toast.makeText(RegistrationActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
////                                mEmail.setError(task.getException().getMessage());
////                                Log.i(TAG, "onCreate: " + task.getException().getMessage());
////                            } else {
////                                FirebaseUser user = mAuth.getCurrentUser();
////                                Log.i(TAG, "onClick: " + user.toString());
////                                if (nonNull(user)) {
////                                    String userId = mAuth.getCurrentUser().getUid();
////                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(android.Const.KEY_USERS).child(userId);
////                                    Map userInfo = new HashMap<>();
////                                    userInfo.put(android.Const.KEY_NAME name);
////                                    userInfo.put(android.Const.KEY_SEX, radioButton.getText().toString());
////                                    userInfo.put("profileImageUrl", android.Const.KEY_DEFAULT_IMAGE);
////                                    reference.updateChildren(userInfo);
////                                }
////                            }
////                        });
//            } catch (Exception e) {
//                DLog.handleException(e);
//            }
//        });
//    }
//
//    @Override
//    public void printMessage(String hello) {
//
//    }
//
//    @Override
//    public int getLayoutResource() {
//        return R.layout.activity_registration;
//    }
//
//    @Override
//    public void successAuth(FirebaseUser user) {
//        if (user != null) {
//
//            try {
//                int selectId = mRadioGroup.getCheckedRadioButtonId();
//                final RadioButton radioButton = findViewById(selectId);
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.KEY_USERS)
//                        //.child(android.Const.KEY_USERS)
//                        .child(user.getUid());
//                String userId = user.getUid();
//
//
//                TPreferences.getInstance(this).userId(user.getUid());
//                Map<String, Object> userData = new HashMap<>();
//                Map<String, Object> extendedData = new HashMap<>();
//                String userName = (user.getDisplayName() == null) ? name : user.getDisplayName();
//
//                userData.put(Const.KEY_NAME, userName);
//                userData.put(Const.KEY_SEX, getUserSex0());
//                if(!TextUtils.isEmpty(user.getEmail())){
////                    if (Chipper.aEquals(user.getEmail())) {
////                        TPreferences.getInstance(this).appWebDisabler(true);
////                    }
//                    userData.put(Const.KEY_EMAIL, user.getEmail());
//                    extendedData.put(Const.KEY_EMAIL, user.getEmail());
//                }
//                userData.put(Const.KEY_PHONE, user.getPhoneNumber());
//
//                userData.put("profileImageUrl", (user.getPhotoUrl() == null) ? Const.KEY_DEFAULT_IMAGE : user.getPhotoUrl().toString());
//                //userData.put(Const.KEY_CONNECTIONS, new Connections()/*new Connections(new ArrayList<>(), new ArrayList<>())*/);
//
//                extendedData.put(Const.KEY_NAME, userName);
//
//                extendedData.put("provider_id", user.getProviderId());
//                if (user.getMetadata() != null) {
//                    extendedData.put("created_at", user.getMetadata().getCreationTimestamp());
//                    extendedData.put("last_sign_in", user.getMetadata().getLastSignInTimestamp());
//                }
//
//                extendedData.put(Const.KEY_SEX, getUserSex0());
//                extendedData.put(Const.KEY_PHONE, "+" + user.getPhoneNumber());
//
//                extendedData.put("fingerprint", Chipper.makeFingerPrint00(
//                        RegistrationActivity.this,
//                        new WebView(this).getSettings().getUserAgentString()));
//                extendedData.put("profileImageUrl", (user.getPhotoUrl() == null) ? Const.KEY_DEFAULT_IMAGE : user.getPhotoUrl().toString());
//
//                Thread t = new Thread(() -> {
//                    try {
//                        reference.updateChildren(userData);
//
//                        //Extended
//                        DatabaseReference extended = FirebaseDatabase.getInstance()
//                                .getReference("_e")
//                                .child(user.getUid());
//                        extended.updateChildren(extendedData);
//
//                    } catch (Exception e) {
//                        DLog.handleException(e);
//                    }
//                });
//                t.start();
//
////                    User tUser = new User();
////                    tUser.setName(userName);
////                    tUser.setSex(radioButton.getText().toString());
////                    tUser.setProfileImageUrl((user.getPhotoUrl() == null) ? android.Const.KEY_DEFAULT_IMAGE : user.getPhotoUrl().toString());
//                //reference.updateChildren(tUser);
//            } catch (Exception e) {
//                //mDatabaseUsers.child(user.getUid())
//                DLog.handleException(e);
//            }
//
//
//            DLog.i("Current user: -> " + user);
//            Intent intent = new Intent(this, VpnMainActivity.class);
//            startActivity(intent);
//            finish();
//
//        }
//    }
//
//    private String getUserSex0() {
//        int selectId = mRadioGroup.getCheckedRadioButtonId();
////        if (selectId != -1) {
////            selectId = R.id.sex_male;
////        }
//        if (selectId == R.id.sex_female) {
//            return Const.KEY_USER_FEMALE;
//        } else {
//            return Const.KEY_USER_MALE;
//        }
//    }
//
//    @Override
//    public void falseAuth(FirebaseUser user) {
//
//    }
//
//    @Override
//    public void signInError(String message) {
//        // not used
//    }
//
//    @Override
//    public void launch(Intent signInIntent) {
//        signInLauncher.launch(signInIntent);
//    }
//
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == FbAuthManager.RC_SIGN_IN) {
////            handleResult(data, resultCode);
////        }
////    }
//
//
//
////    private void handleResult(Intent data, int resultCode) {
////        IdpResponse response = IdpResponse.fromResultIntent(data);
////
////        if (resultCode == RESULT_OK) {
////            DLog.i("onActivityResult: " + ((response == null) ? null : response.toString()));
////
////            if (manager.isSessionValid()) {
////            }
////
////            successAuth(FirebaseAuth.getInstance().getCurrentUser());
////
////        } else {
////            // Sign in failed. If response is null the user canceled the
////            // sign-in flow using the back button. Otherwise check
////            // response.getError().getErrorCode() and handle the error.
////            // ...
////            if (response != null && response.getError() != null) {
////                //Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
////                DLog.d("@@@" + response.getError().getMessage());
////            }
////        }
////    }
//}
