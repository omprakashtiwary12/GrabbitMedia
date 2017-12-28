package com.grabbit.daily_deals.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/22/2016.
 */
public class MyProfileActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {

    private ImageButton activity_my_profile_btn_browse;
    private EditText activity_my_profile_edt_phone;
    private EditText activity_my_profile_edt_email;
    private EditText activity_my_profile_edt_name;
    private CircularImageView activity_my_profile_img_photo;

    private Button activity_my_profile_btn_submit;
    private Button activity_my_profile_btn_logout;

    private String base64String;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_my_profile;
    }

    @Override
    public void initialize() {
        setTitle("My Profile");
        activity_my_profile_edt_phone = (EditText) findViewById(R.id.activity_my_profile_edt_phone);
        activity_my_profile_edt_phone.setText(AppPref.getInstance().getUserMobile());

        activity_my_profile_edt_email = (EditText) findViewById(R.id.activity_my_profile_edt_email);
        activity_my_profile_edt_email.setText(AppPref.getInstance().getUserEmail());

        activity_my_profile_edt_name = (EditText) findViewById(R.id.activity_my_profile_edt_name);
        activity_my_profile_edt_name.setText(AppPref.getInstance().getUserName());

        activity_my_profile_btn_browse = (ImageButton) findViewById(R.id.activity_my_profile_btn_browse);
        activity_my_profile_btn_browse.setOnClickListener(this);

        activity_my_profile_btn_submit = (Button) findViewById(R.id.activity_my_profile_btn_submit);
        activity_my_profile_btn_submit.setOnClickListener(this);

        activity_my_profile_btn_logout = (Button) findViewById(R.id.activity_my_profile_btn_logout);
        activity_my_profile_btn_logout.setOnClickListener(this);

        activity_my_profile_img_photo = (CircularImageView) findViewById(R.id.activity_my_profile_img_photo);

        try {
            Picasso.with(this).load(AppPref.getInstance().getImageUrl()).into(activity_my_profile_img_photo);
        } catch (Exception e) {
        }
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_edit:
                activity_my_profile_btn_browse.setVisibility(View.VISIBLE);
                activity_my_profile_btn_submit.setVisibility(View.VISIBLE);
                activity_my_profile_btn_logout.setVisibility(View.GONE);
                activity_my_profile_edt_phone.setEnabled(true);
                activity_my_profile_edt_phone.setFocusableInTouchMode(true);
                activity_my_profile_edt_name.setEnabled(true);
                activity_my_profile_edt_name.setFocusableInTouchMode(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_my_profile_btn_browse:
                browse();
                break;
            case R.id.activity_my_profile_btn_submit:

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("api_key=").append(AppUrl.API_KEY);
                stringBuilder.append("&name=").append("" + activity_my_profile_edt_name.getText().toString());
                stringBuilder.append("&phone=").append("" + activity_my_profile_edt_phone.getText().toString());
                stringBuilder.append("&cus_id=").append("" + AppPref.getInstance().getUserID());
                stringBuilder.append("&photo=").append("" + base64String);

                Log.d("Profile Activity ", base64String);
                String content = stringBuilder.toString();
                GetDataUsingWService serviceProfileUpdate =
                        new GetDataUsingWService(MyProfileActivity.this, AppUrl.PROFILE_UPDATE_URL, 0, content, true, "", this);
                serviceProfileUpdate.execute();
                break;
            case R.id.activity_my_profile_btn_logout:
                AppPref.getInstance().clearSharedPreferenceFile();
                sendToThisActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ContextCompat.checkSelfPermission(MyProfileActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyProfileActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(MyProfileActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(MyProfileActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                base64String = Other.convertBitmapToBase64String(bitmap);
                activity_my_profile_img_photo.setImageBitmap(bitmap);
            } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                Uri selectedFileUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileUri);
                base64String = Other.convertBitmapToBase64String(bitmap);
                activity_my_profile_img_photo.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.w("Error", e.toString());
        }
    }

    private void browse() {
        final CharSequence[] items = {"Take Photo", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
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
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.d("Profile Activity ", responseData);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                activity_my_profile_btn_browse.setVisibility(View.GONE);
                activity_my_profile_btn_submit.setVisibility(View.GONE);
                activity_my_profile_btn_logout.setVisibility(View.VISIBLE);
                activity_my_profile_edt_phone.setEnabled(false);
                activity_my_profile_edt_name.setEnabled(false);
                toastMessage(jsonObject.getString("msg"));
                AppPref.getInstance().setUserName(activity_my_profile_edt_name.getText().toString());
                AppPref.getInstance().setUserMobile(activity_my_profile_edt_phone.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
