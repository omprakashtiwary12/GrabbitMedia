package com.mentobile.grabbit.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
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
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;

import org.json.JSONArray;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceData, GoogleApiClient.OnConnectionFailedListener {

    private Button btnRegister;
    private final String TAG = "LoginActivity";
    private Button btnLogin;
    private Button btnSignup;
    private EditText edUserName;
    private EditText edPassword;
    private TextView tvForgetPass;
    private ImageView ivFacebook;
    private ImageView ivGoogle;
    public GoogleApiClient mGoogleApiClient;

    public static LoginActivity loginActivity;

    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        loginActivity = this;
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.login_btn_register);
        btnSignup.setOnClickListener(this);

        edUserName = (EditText) findViewById(R.id.login_ed_username);
        edPassword = (EditText) findViewById(R.id.login_ed_password);

        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
        ivFacebook = (ImageView) findViewById(R.id.login_iv_facebook);
        ivGoogle = (ImageView) findViewById(R.id.login_iv_google);
        ivFacebook.setOnClickListener(this);
        ivGoogle.setOnClickListener(this);

        tvForgetPass.setPaintFlags(tvForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgetPass.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void sendToThisActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

//    @Override
//    public int getActivityLayout() {
//        return R.layout.activity_login;
//    }
//
//    @Override
//    public void initialize() {
//
//        loginActivity = this;
//        btnLogin = (Button) findViewById(R.id.login_btn_login);
//        btnLogin.setOnClickListener(this);
//        btnSignup = (Button) findViewById(R.id.login_btn_register);
//        btnSignup.setOnClickListener(this);
//
//        edUserName = (EditText) findViewById(R.id.login_ed_username);
//        edPassword = (EditText) findViewById(R.id.login_ed_password);
//
//        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
//        ivFacebook = (ImageView) findViewById(R.id.login_iv_facebook);
//        ivGoogle = (ImageView) findViewById(R.id.login_iv_google);
//        ivFacebook.setOnClickListener(this);
//        ivGoogle.setOnClickListener(this);
//
//        tvForgetPass.setPaintFlags(tvForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tvForgetPass.setOnClickListener(this);
//
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
////        googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
////        GcmAsync myAsynchTask = new GcmAsync();
////        myAsynchTask.execute();
//
//
//    }
//
//    @Override
//    public void init(Bundle save) {
//
//    }


    JSONObject json = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:

                String username = edUserName.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (username.length() < 1) {
                    toastMessage("Please Provide UserName");
                } else if (password.length() < 1) {
                    toastMessage("Please Provide Password");
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("email=").append(username);
                    stringBuilder.append("&password=").append(password);
                    stringBuilder.append("&api_key=").append(AppUrl.API_KEY);

                    String content = stringBuilder.toString();

                    GetDataUsingWService getDataUsingWService = new GetDataUsingWService(LoginActivity.this, AppUrl.LOGIN_URL, 0, content, true, "Logging ...", this);
                    getDataUsingWService.execute();
                }
                break;
            case R.id.login_btn_register:
                sendToThisActivity(RegisterActivity.class);
                break;
            case R.id.login_tv_forgetpassword:
                forgetPassword();
                break;
            case R.id.login_iv_google:
                signIn();
                break;
        }
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
                // sendToThisActivity(OtpActivity.class, new String[]{"from;forget", "phone;" + fragment_forget_et_email.getText().toString()});
            }
        });
        dialog.show();

    }


    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        try {
            Log.w("LoginActivity", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                String msg = jsonObject.getString("msg");
                toastMessage(msg);
                jsonObject = jsonObject.getJSONObject("details");
                String cus_id = jsonObject.getString("cus_id");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String name = jsonObject.getString("name");
                Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                sendToThisActivity(BluetoothActivity.class);
                //sendToThisActivity(DrawerActivity.class);
            }
        } catch (Exception e) {

        }
    }


    //google  integration

    private static final int RC_SIGN_IN = 9001;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    public void signOut() {
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
            handleSignInResult(result);
        } else {
            //  callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            AppPref.getInstance().setName(acct.getDisplayName());
//            AppPref.getInstance().setEmail(acct.getEmail());
//            AppPref.getInstance().setProfileUrl(acct.getPhotoUrl().toString());
            // uploadresultforGoogle(acct.getDisplayName(), acct.getEmail());
            Other.saveDataInSharedPreferences("11", acct.getDisplayName(), acct.getEmail(), "");
            signOut();
            revokeAccess();
//            sendToActivity(MainActivity.class);
        } else {

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    // facebook  integration


//    private CallbackManager callbackManager;
//    private LoginButton loginButton;
//    // private TextView btnLogin;
//    private ProgressDialog progressDialog;
//
//    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            GraphRequest request = GraphRequest.newMeRequest(
//                    loginResult.getAccessToken(),
//                    new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(
//                                JSONObject object,
//                                GraphResponse response) {
//
//                            Log.e("response: ", response + "");
//                            try {
//                                Other.saveDataInSharedPreferences("1234", object.getString("name").toString(), object.getString("email").toString(), "1234567890");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//
//                    });
//
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,email,gender, birthday");
//            request.setParameters(parameters);
//            request.executeAsync();
//        }
//
//        @Override
//        public void onCancel() {
//            progressDialog.dismiss();
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//            progressDialog.dismiss();
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//        callbackManager = CallbackManager.Factory.create();
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("public_profile", "email", "user_friends");
//        ivFacebook = (ImageView) findViewById(R.id.login_iv_facebook);
//        ivFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                progressDialog = new ProgressDialog(LoginActivity.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();
//
//                loginButton.performClick();
//
//                loginButton.setPressed(true);
//
//                loginButton.invalidate();
//
//                loginButton.registerCallback(callbackManager, mCallBack);
//
//                loginButton.setPressed(false);
//
//                loginButton.invalidate();
//
//            }
//        });
//
//    }
//
//    public void disconnectFromFacebook() {
//
//        if (AccessToken.getCurrentAccessToken() == null) {
//            return; // already logged out
//        }
//
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                .Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//
//                LoginManager.getInstance().logOut();
//
//            }
//        }).executeAsync();
//    }

}
