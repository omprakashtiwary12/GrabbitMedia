package com.grabbit.daily_deals.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.pkmmte.view.CircularImageView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Gokul on 11/23/2016.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {

    private EditText act_signup_ET_lastName;
    private EditText act_signup_ET_mobile;
    private EditText act_signup_ET_email;
    private EditText act_signup_ET_password;
    private CircularImageView activity_register_profile_image;
    private Button act_signup_BTN_next;
    String selectedFilePath;
    private String User_reg_social_type = "";

    private String token = "";
    private boolean isPassword_Visible;
    private String otp;

    public final static int IMAGE_GALLERY = 100;
    public final static int IMAGE_CAMERA = 101;

    @Override
    public int getActivityLayout() {
        //initialize the SDK facebook before executing any other operations
        FacebookSdk.sdkInitialize(getApplicationContext());
        return R.layout.activity_register;
    }

    @Override
    protected void onStop() {
        super.onStop();
        User_reg_social_type = "";
        signOutFromGoogle();
        logoutFromFacebook();
    }

    @Override
    public void initialize() {
        setTitle("Register");
        getSupportActionBar().hide();
        act_signup_ET_lastName = (EditText) findViewById(R.id.act_signup_ET_lastName);
        act_signup_ET_mobile = (EditText) findViewById(R.id.act_signup_ET_mobile);
        act_signup_ET_email = (EditText) findViewById(R.id.act_signup_ET_email);
        act_signup_ET_password = (EditText) findViewById(R.id.act_signup_ET_password);
        act_signup_ET_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassword_Visible) {
                    isPassword_Visible = false;
                    act_signup_ET_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isPassword_Visible = true;
                    act_signup_ET_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });

        activity_register_profile_image = (CircularImageView) findViewById(R.id.activity_register_profile_image);
        act_signup_BTN_next = (Button) findViewById(R.id.act_signup_BTN_next);
        act_signup_BTN_next.setOnClickListener(this);
        activity_register_profile_image.setOnClickListener(this);
        token = FirebaseInstanceId.getInstance().getToken();

        Button btnLoginGoogle = (Button) findViewById(R.id.login_page_btn_google);
        btnLoginGoogle.setOnClickListener(this);
        initializeFacebook();
        initializeGoogle();
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_page_btn_google:
                User_reg_social_type = "Google";
                signInWithGoogle();
                break;
            case R.id.button_facebook:
                User_reg_social_type = "Facebook";
                loginByFacebook();
                break;

            case R.id.act_signup_BTN_next:
                User_reg_social_type = "Simple";
                save();
                break;
            case R.id.activity_register_profile_image:
                browse();
                break;
        }
    }

    private void save() {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        otp = randomPIN + "";
        String name = act_signup_ET_lastName.getText().toString();
        String mobile = act_signup_ET_mobile.getText().toString();
        String email = act_signup_ET_email.getText().toString();
        String password = act_signup_ET_password.getText().toString();

        if (name.length() < 5) {
            toastMessage("Please Provide name");
            return;
        } else if (mobile.length() != 10) {
            toastMessage("Please Provide Mobile No");
            return;
        } else if (!Other.isValidEmail(email)) {
            toastMessage("Please Provide a Valid E-Mail Id");
            return;
        } else if (password.length() < 5) {
            toastMessage("Please Provide Password");
            return;
        } else {
            //  String encodedImage = getStringImage(activity_register_profile_image.getDrawingCache());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("api_key=").append(AppUrl.API_KEY);
            stringBuilder.append("&email=").append(email);
            stringBuilder.append("&login_type=").append(User_reg_social_type);
            stringBuilder.append("&name=").append(name);
            stringBuilder.append("&password=").append(password);
            stringBuilder.append("&phone=").append(mobile);
            stringBuilder.append("&gcmid=").append(token);
            stringBuilder.append("&photo=").append("");
            stringBuilder.append("&otp=").append(otp);
            String content = stringBuilder.toString();
            activity_register_profile_image.setDrawingCacheEnabled(true);
            GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.REGISTER_URL, 0, content, true, "Registering ...", this);
            getDataUsingWService.execute();
        }
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {

        try {
            Log.w("response_data", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                String msg = jsonObject.getString("msg");
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                jsonObject = jsonArray.getJSONObject(0);
                String cus_id = jsonObject.getString("cus_id");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String name = jsonObject.getString("name");
                String login_type = jsonObject.getString("login_type");
                Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                if (!login_type.equalsIgnoreCase("Simple")) {
                    sendToThisActivity(DashboardActivity.class);
                    finish();
                } else {
                    sendToThisActivity(OtpActivity.class, new String[]{"from;register", "phone;" + phone, "otp;" + otp});
                    finish();
                }
            } else {
                String msg = jsonObject.getString("msg");
                toastMessage(msg);
            }
        } catch (Exception e) {
        }
    }


    private void registerBysocielMedia() {
        String name = act_signup_ET_lastName.getText().toString();
        String email = act_signup_ET_email.getText().toString();
        String password = act_signup_ET_password.getText().toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&email=").append(email);
        stringBuilder.append("&login_type=").append(User_reg_social_type);
        stringBuilder.append("&name=").append(name);
        stringBuilder.append("&password=").append(password);
        stringBuilder.append("&phone=").append("");
        stringBuilder.append("&gcmid=").append(token);
        stringBuilder.append("&photo=").append("");
        String content = stringBuilder.toString();
        activity_register_profile_image.setDrawingCacheEnabled(true);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.REGISTER_URL, 0, content, true, "Registering ...", this);
        getDataUsingWService.execute();
    }

    //upload  image  code
    private void browse() {
        final CharSequence[] items = {"Take Photo", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Select");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, IMAGE_CAMERA);
                } else if (items[item].equals("From Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            IMAGE_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // Sign with Google

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    private void initializeGoogle() {
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

    public static String getPath(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_GALLERY) {
                Uri picUri = data.getData();
                selectedFilePath = getPath(picUri, getApplicationContext());
                activity_register_profile_image.setImageURI(picUri);
                if (selectedFilePath != null) {
                    //imageUpload(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == IMAGE_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                activity_register_profile_image.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri picUri = getImageUri(getApplicationContext(), photo);
                selectedFilePath = getPath(picUri, getApplicationContext());
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                if (selectedFilePath != null) {
                    //imageUpload(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Signed in successfully, show authenticated UI.
                    try {
                        GoogleSignInAccount googleResult = result.getSignInAccount();
                        act_signup_ET_email.setText("" + googleResult.getEmail());
                        act_signup_ET_lastName.setText("" + googleResult.getDisplayName());
                        registerBysocielMedia();
                    } catch (Exception e) {
                    }
                } else {
                }
            } else {
                callBackManager.onActivityResult(requestCode, resultCode, data);
            }
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
                                act_signup_ET_email.setText("" + object.getString("email"));
                                act_signup_ET_lastName.setText("" + object.getString("name"));
                                registerBysocielMedia();
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
        LoginManager.getInstance().logOut();
    }
}
