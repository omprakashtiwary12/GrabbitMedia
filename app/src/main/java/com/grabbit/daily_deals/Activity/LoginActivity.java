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
import android.os.Build;
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
import com.grabbit.daily_deals.Utility.BaseActivity;
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
public class LoginActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {
    private final String TAG = "LoginActivity";
    private Button btnLogin;
    private EditText edUserName;
    private EditText edPassword;
    private TextView tvForgetPass;
    public static String gcmid = "";
    private ImageButton imgBtnPasswordStatus;
    private boolean isPasswordView;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_login1;
    }

    @Override
    public void initialize() {
        setContentView(R.layout.activity_login1);
        setTitle("Login");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        edUserName = (EditText) findViewById(R.id.login_ed_username);
        edPassword = (EditText) findViewById(R.id.login_ed_password);
        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
        tvForgetPass.setPaintFlags(tvForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgetPass.setOnClickListener(this);
        imgBtnPasswordStatus = (ImageButton) findViewById(R.id.login_btn_password_syatus);
        imgBtnPasswordStatus.setOnClickListener(this);
    }

    @Override
    public void init(Bundle save) {

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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    } else
                        finish();
                } else if (status.equals("2")) {
                    toastMessage(msg);
                } else {
                    String phone = jsonObject.getString("phone");
                    sendToThisActivity(OtpActivity.class, new String[]{"from;login", "phone;" + phone});
                    toastMessage(msg);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "::::Exception " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                String username = edUserName.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                login("", username, password, "Simple", "");
                break;
            case R.id.login_tv_forgetpassword:
                forgetPassword();
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
            GetDataUsingWService getDataUsingWService = new GetDataUsingWService(LoginActivity.this, AppUrl.LOGIN_URL, 0, content, true, "Signing...", this);
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
}

