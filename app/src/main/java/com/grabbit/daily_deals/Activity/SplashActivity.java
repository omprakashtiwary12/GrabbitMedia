package com.grabbit.daily_deals.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.grabbit.daily_deals.Fragment.NetworkErrorFragment;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONObject;

/**
 * Created by Gokul on 11/15/2016.
 */

public class SplashActivity extends BaseActivity implements LoadDataFromServer.iGetResponse, View.OnClickListener, GetWebServiceData {

    private static final String TAG = "SplashActivity";

    public final static int RESULT_NETWORK = 100;
    public final static int RESULT_BLUETOOTH = 101;

    private LinearLayout splash_button;
    private Button btnLogin;
    private Button btnSignup;

    private Button btnFacebook;
    private Button btnGoogle;

    @Override
    protected void onStop() {
        super.onStop();
        signOutFromGoogle();
        logoutFromFacebook();
    }

    @Override
    public int getActivityLayout() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        return R.layout.activity_splash;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();


        splash_button = (LinearLayout) findViewById(R.id.splash_button);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);

        btnGoogle = (Button) findViewById(R.id.login_btn_google);
        btnGoogle.setOnClickListener(this);
        btnFacebook = (Button) findViewById(R.id.button_facebook);
        btnFacebook.setOnClickListener(this);
        initializeFacebook();
        initializeGoogle();
    }

    @Override
    public void init(Bundle save) {

    }


    @Override
    public void getLoadDataResponse(boolean isStatus) {
        if (isStatus) {
            if (AppPref.getInstance().isLogin()) {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.btn_signup:
                Intent intent1 = new Intent(SplashActivity.this, RegisterActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.login_btn_google:
                signInWithGoogle();
                break;

            case R.id.button_facebook:
                loginByFacebook();
                break;
        }
    }

    private void loginBySocielMedia(String name, String username, String password, String login_by, String photo) {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (username.length() < 1) {
            toastMessage("Please Provide UserName");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name=").append(name);
            stringBuilder.append("&email=").append(username);
            stringBuilder.append("&password=").append(password);
            stringBuilder.append("&login_by=").append(login_by);
            stringBuilder.append("&gcmid=").append(token);
            stringBuilder.append("&photo=").append(photo);
            stringBuilder.append("&api_key=").append(AppUrl.API_KEY);
            String content = stringBuilder.toString();
            GetDataUsingWService getDataUsingWService = new GetDataUsingWService(SplashActivity.this, AppUrl.LOGIN_URL, 0, content, true, "Signing ...", this);
            getDataUsingWService.execute();
        }
    }

    // Sign with Google

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    private void initializeGoogle() {
        Button btnLoginGoogle = (Button) findViewById(R.id.login_btn_google);
        btnLoginGoogle.setOnClickListener(this);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signInWithGoogle() {
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOutFromGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            // updateUI(false);
                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (resultCode == RESULT_BLUETOOTH) {
           // new LoadDataFromServer(this, this).startFatching();
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                String username = "", name = "", photo_url = "";
                try {
                    GoogleSignInAccount googleResult = result.getSignInAccount();
                    username = googleResult.getEmail();
                    name = googleResult.getDisplayName();
                    photo_url = googleResult.getPhotoUrl().toString();
//                    edUserName.setText("" + username);
                    loginBySocielMedia(name, username, "", "GOOGLE", photo_url);

                } catch (Exception e) {
//                    edUserName.setText("" + username);
                    loginBySocielMedia(name, username, "", "GOOGLE", photo_url);
                    //toastMessage("Google Signin Error " + e.getMessage());
                }
            } else {
                toastMessage("Google Signin Error " + resultCode);
            }
        } else {
            callBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // End og Google Login

    // Start with Facebook Login

    //facebook attribute
    private LoginButton btnLoginFacebook;
    private CallbackManager callBackManager;
    private AccessTokenTracker accessTokenTracker;

    private void initializeFacebook() {

        btnLoginFacebook = (LoginButton) findViewById(R.id.login_page_btn_facebook);
        Button buttonFacebook = (Button) findViewById(R.id.button_facebook);
        buttonFacebook.setOnClickListener(this);

        //callback
        callBackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        accessTokenTracker.startTracking();
        btnLoginFacebook.setReadPermissions("public_profile", "email", "user_friends");
    }

    /**
     * FacebookCallBack
     */
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.e("response: ", response + "");
                            String username = "", name = "", profile_picture = "";
                            try {
                                username = "" + object.getString("email");
                                name = "" + object.getString("name");
                                profile_picture = "https://graph.facebook.com/" + object.getString("id").toString() + "/picture?type=large";
                                loginBySocielMedia(name, username, "", "FACEBOOK", profile_picture);
                            } catch (Exception e) {
                                if (username != null) {
                                    //edUserName.setText("" + username);
                                    loginBySocielMedia(name, username, "", "FACEBOOK", profile_picture);
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private void loginByFacebook() {
        btnLoginFacebook.setSoundEffectsEnabled(false);
        btnLoginFacebook.performClick();
        btnLoginFacebook.setPressed(true);
        btnLoginFacebook.invalidate();
        btnLoginFacebook.registerCallback(callBackManager, mCallBack);
    }

    private void logoutFromFacebook() {
        if (accessTokenTracker != null)
            accessTokenTracker.stopTracking();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        try {
            if (serviceCounter == 0) {
                Log.w("LoginActivity", responseData);
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("msg");
                if (status.equalsIgnoreCase("1")) {
                    jsonObject = jsonObject.getJSONObject("details");
                    String cus_id = jsonObject.getString("cus_id");
                    String phone = jsonObject.getString("phone");
                    String email = jsonObject.getString("email");
                    String name = jsonObject.getString("name");
                    String photo_url = jsonObject.getString("photo");
                    String login_type = jsonObject.getString("login_type");
                    String account_status = jsonObject.getString("status");

                    String emg_phone1 = jsonObject.getString("emg_phone1");
                    AppPref.getInstance().setEPhone1(emg_phone1);
                    String emg_phone2 = jsonObject.getString("emg_phone2");
                    AppPref.getInstance().setEPhone2(emg_phone2);
                    String emg_phone3 = jsonObject.getString("emg_phone3");
                    AppPref.getInstance().setEPhone3(emg_phone3);
                    Other.saveDataInSharedPreferences(cus_id, name, email, phone, photo_url);

                    if (account_status.equalsIgnoreCase("ACTIVE")) {
                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        verifyPhoneNumber();
                    }
                } else if (status.equals("2")) {
                    toastMessage(msg);
                }
            }
        } catch (Exception e) {
        }
    }

    private void verifyPhoneNumber() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_forget);
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.fragment_forget_tv_title);
        tvTitle.setText("Verify Phone Number");
        final EditText edPhoneNumber = (EditText) dialog.findViewById(R.id.fragment_forget_et_email);
        Button fragment_forget_cancel = (Button) dialog.findViewById(R.id.fragment_forget_cancel);
        fragment_forget_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button reset_password = (Button) dialog.findViewById(R.id.reset_password);
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edPhoneNumber.getText().toString();
                if (phone.equalsIgnoreCase("")) {
                    toastMessage("Please enter your mobile number");
                } else {
                    sendToThisActivity(OtpActivity.class, new String[]{"from;register", "phone;" + phone});
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    } else
                        finish();

                }
            }
        });
        dialog.show();
    }
}
