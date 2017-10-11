package com.mentobile.grabbit.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;
import com.pkmmte.view.CircularImageView;

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
    private static final int SIMPLE_LOGIN_TYPE = 0;
    private static final int FACEBOOK_LOGIN_TYPE = 2;

    private String gcm_id = "";
    private boolean isPassword_Visible;
    private String otp;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initialize() {
        setTitle("Register");
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

        gcm_id = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_signup_BTN_next:
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
            stringBuilder.append("&login_type=").append(SIMPLE_LOGIN_TYPE);
            stringBuilder.append("&name=").append(name);
            stringBuilder.append("&password=").append(password);
            stringBuilder.append("&phone=").append(mobile);
            stringBuilder.append("&gcmid=").append(gcm_id);
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
               // toastMessage(msg);
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                jsonObject = jsonArray.getJSONObject(0);
                String cus_id = jsonObject.getString("cus_id");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String name = jsonObject.getString("name");
                Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                sendToThisActivity(OtpActivity.class, new String[]{"from;register", "phone;" + phone,"otp;"+otp});
                finish();
            } else {
                String msg = jsonObject.getString("msg");
                toastMessage(msg);
                sendToThisActivity(LoginActivity.class);
                finish();
            }
        } catch (Exception e) {
        }
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
                    startActivityForResult(intent, 1);
                } else if (items[item].equals("From Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            2);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        Bitmap photo;
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                photo = (Bitmap) data.getExtras().get("data");
                activity_register_profile_image.setImageBitmap(photo);
            } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                Uri selectedFileUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileUri);
                activity_register_profile_image.setImageBitmap(bitmap);
            }

        } catch (Exception e) {
            Log.w("Error", e.toString());
        }
    }
}
