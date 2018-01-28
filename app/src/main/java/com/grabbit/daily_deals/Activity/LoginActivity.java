package com.grabbit.daily_deals.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.cloud.internal.ApiUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 12/29/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceData {
    private final String TAG = "LoginActivity";
    private Button btnLogin;
    private Button btnSignup;
    private Button btnFacebook;
    private Button btnGoogle;
    private EditText edUserName;
    private EditText edPassword;
    private TextView tvForgetPass;
    public static String gcmid = "";
    private ImageButton imgBtnPasswordStatus;
    private boolean isPasswordView;
    private ImageView imgBtn_Back;

    @Override
    protected void onStop() {
        super.onStop();
        signOutFromGoogle();
        logoutFromFacebook();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialize the SDK facebook before executing any other operations
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        setTitle("Login");
        getSupportActionBar().hide();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.grabbit.daily_deals",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", "" + e);
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", "" + e);
        }

        imgBtn_Back = (ImageView) findViewById(R.id.act_details_TV_title_back);
        imgBtn_Back.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.login_btn_signup);
        btnSignup.setOnClickListener(this);
        edUserName = (EditText) findViewById(R.id.login_ed_username);
        edPassword = (EditText) findViewById(R.id.login_ed_password);
        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
        tvForgetPass.setPaintFlags(tvForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgetPass.setOnClickListener(this);
        btnGoogle = (Button) findViewById(R.id.login_btn_google);
        btnGoogle.setOnClickListener(this);
        btnFacebook = (Button) findViewById(R.id.button_facebook);
        btnFacebook.setOnClickListener(this);

        imgBtnPasswordStatus = (ImageButton) findViewById(R.id.login_btn_password_syatus);
        imgBtnPasswordStatus.setOnClickListener(this);

        initializeFacebook();
        initializeGoogle();
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

                    String emg_phone1 = jsonObject.getString("emg_phone1");
                    AppPref.getInstance().setEPhone1(emg_phone1);
                    String emg_phone2 = jsonObject.getString("emg_phone2");
                    AppPref.getInstance().setEPhone2(emg_phone2);
                    String emg_phone3 = jsonObject.getString("emg_phone3");
                    AppPref.getInstance().setEPhone3(emg_phone3);
                    Other.saveDataInSharedPreferences(cus_id, name, email, phone, photo_url);
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();
                } else if (status == "2") {
                    toastMessage(msg);
                } else {
                    String phone = jsonObject.getString("phone");
                    sendToThisActivity(OtpActivity.class, new String[]{"from;login", "phone;" + phone, "otp;" + ""});
                    toastMessage(msg);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_details_TV_title_back:
                onBackPressed();
                break;

            case R.id.login_btn_login:
                String username = edUserName.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                login("", username, password, "Simple", "");
                break;
            case R.id.login_btn_signup:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.login_tv_forgetpassword:
                forgetPassword();
                break;
            case R.id.login_btn_google:
                signInWithGoogle();
                break;

            case R.id.button_facebook:
                loginByFacebook();
                break;

            case R.id.login_btn_password_syatus:
                if (isPasswordView) {
                    imgBtnPasswordStatus.setBackgroundResource(R.drawable.ic_custom_show);
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordView = false;
                } else {
                    imgBtnPasswordStatus.setBackgroundResource(R.drawable.ic_custom_hide);
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordView = true;
                }
                break;
        }
    }

    private void login(String name, String username, String password, String login_by, String photo) {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (username.length() < 1) {
            toastMessage("Please Provide UserName");
        } else if (password.length() < 1) {
            toastMessage("Please Provide Password");
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
            GetDataUsingWService getDataUsingWService = new GetDataUsingWService(LoginActivity.this, AppUrl.LOGIN_URL, 0, content, true, "Logging ...", this);
            getDataUsingWService.execute();
        }
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void forgetPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_forget);
        final EditText fragment_forget_et_email = (EditText) dialog.findViewById(R.id.fragment_forget_et_email);
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
                String input = fragment_forget_et_email.getText().toString();
                if (input.equalsIgnoreCase("")) {
                    toastMessage("Please enter your mobile number");
                } else {
                    sendToThisActivity(OtpActivity.class, new String[]{"from;forget", "phone;" + input});
                }
            }
        });
        dialog.show();
    }

    public void sendToThisActivity(Class activity, String s[]) {
        Intent intent = new Intent(this, activity);
        for (int i = 0; i < s.length; i++) {
            String p[] = s[i].split(";");
            intent.putExtra(p[0], p[1]);
        }
        startActivity(intent);
    }

    //download data
    public void checkBloothConnection() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        if (isEnabled) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
            startActivity(intent);
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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                try {
                    GoogleSignInAccount googleResult = result.getSignInAccount();
                    String username = googleResult.getEmail();
                    String name = googleResult.getDisplayName();
                    String photo_url = googleResult.getPhotoUrl().toString();
                    edUserName.setText("" + username);
                    login(name, username, "123456", "Google", photo_url);

                } catch (Exception e) {
                }
            } else {
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
                            try {
                                String username = "" + object.getString("email");
                                String name = "" + object.getString("name");
                                String profile_picture = "https://graph.facebook.com/" + object.getString("id").toString() + "/picture?type=large";
                                login(name, username, "123456", "Facebook", profile_picture);
                            } catch (Exception e) {
                                e.printStackTrace();
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
}

