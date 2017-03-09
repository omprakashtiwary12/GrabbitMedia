package com.mentobile.grabbit.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 12/29/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GetWebServiceData, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "LoginActivity";
    private Button btnLogin;
    private Button btnSignup;
    private ImageView btnFacebook;
    private ImageView btnGoogle;
    private EditText edUserName;
    private EditText edPassword;
    private TextView tvForgetPass;
    public GoogleApiClient mGoogleApiClient;
    public static String gcmid = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // facebook sdk initialize
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mentobile.grabbit",
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

        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.login_btn_signup);
        btnSignup.setOnClickListener(this);
        edUserName = (EditText) findViewById(R.id.login_ed_username);
        edPassword = (EditText) findViewById(R.id.login_ed_password);
        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
        tvForgetPass.setPaintFlags(tvForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgetPass.setOnClickListener(this);
        btnGoogle = (ImageView) findViewById(R.id.login_btn_google);
        btnGoogle.setOnClickListener(this);
        btnFacebook = (ImageView) findViewById(R.id.login_btn_google);
        btnFacebook.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {

        try {
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
                Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                AppPref.getInstance().setImageUrl(AppUrl.PROFILE_PIC_URL + AppPref.getInstance().getUserID() + ".jpg");
               //checkBloothConnection();
                sendToThisActivity(DrawerActivity.class);
            }
            else {
                toastMessage(msg);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
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
                    GetDataUsingWService getDataUsingWService = new GetDataUsingWService(LoginActivity.this, AppUrl.LOGIN_URL, 100, content, true, "Logging ...", this);
                    getDataUsingWService.execute();
                }
                break;
            case R.id.login_btn_signup:
                sendToThisActivity(RegisterActivity.class);
                break;
            case R.id.login_tv_forgetpassword:
                forgetPassword();
                break;
            case R.id.login_btn_google:
                signIn();
                break;

        }
    }

    public void sendToThisActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
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
                    toastMessage("Please Enter Number");
                } else {
                    sendToThisActivity(OtpActivity.class, new String[]{"from;forget", "phone;" + fragment_forget_et_email.getText().toString()});
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to Exit !");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private static final int RC_SIGN_IN = 9001;

    // Google client to communicate with Google
    // private GoogleApiClient mGoogleApiClient;

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
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]

    public void uploadresultforGoogle(String name, String email, String login_type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&name=").append(name);
        stringBuilder.append("&email=").append(email);
        stringBuilder.append("&Phone=").append("");
        stringBuilder.append("&password=").append("");
        stringBuilder.append("&login_type=").append(login_type);
        stringBuilder.append("$gcm_id=").append(gcmid);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(LoginActivity.this, AppUrl.REGISTER_URL, 0, content, true, "Loding ..", new GetWebServiceData() {
            @Override
            public void getWebServiceResponse(String result, int serviceCounter) {
                Log.d(TAG, "::::Result " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String msg = jsonObject.getString("msg");
                        toastMessage(msg);
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        jsonObject = jsonArray.getJSONObject(0);
                        String cus_id = jsonObject.getString("cus_id");
                        String phone = jsonObject.getString("phone");
                        String email = jsonObject.getString("email");
                        String name = jsonObject.getString("name");
                        Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                        checkBloothConnection();
                    }
                    finish();
                } catch (Exception e) {

                }
            }
        });
        getDataUsingWService.execute();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            try {
                GoogleSignInAccount acct = result.getSignInAccount();
                uploadresultforGoogle(acct.getDisplayName(), acct.getEmail(), "2");
                AppPref.getInstance().setImageUrl(acct.getPhotoUrl().toString());
                //  Other.saveDataInSharedPreferences("11", acct.getDisplayName(), acct.getEmail(), "");
            } catch (Exception e) {

            }
            signOut();
            revokeAccess();
//            checkBloothConnection();
            //sendToThisActivity(BluetoothActivity.class);
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



      /*//
        Google Login Connection Method
        ------------ End  ---------------
     */


    //facebook integration

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    // private TextView btnLogin;
    private ProgressDialog progressDialog;

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
                                String imageurl = "https://graph.facebook.com/" + object.getString("id").toString() + "/picture?type=small";
                                AppPref.getInstance().setImageUrl(imageurl);
                                uploadresultforGoogle(object.getString("name").toString(), object.getString("email").toString(), "1");
                                //Other.saveDataInSharedPreferences("11", object.getString("name").toString(), object.getString("email").toString(), "");
                                Toast.makeText(LoginActivity.this, "welcome ", Toast.LENGTH_LONG).show();
                                disconnectFromFacebook();
//                                checkBloothConnection();
                                //sendToThisActivity(BluetoothActivity.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        btnFacebook = (ImageView) findViewById(R.id.login_btn_facebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();

    }

    private class GcmAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
               // gcmid = googleCloudMessaging.register("376155352726");
                Log.d(TAG, ":::gcmid" + gcmid);
            } catch (Exception e) {
                Log.d(TAG, "errorinlogin" + e.toString());
            }
            return "";
        }
    }


    //download data

    public void checkBloothConnection() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        if (isEnabled) {
            sendToThisActivity(CategoryActivity.class);
        } else {
            sendToThisActivity(BluetoothActivity.class);
        }
    }


}

