package com.grabbit.daily_deals.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.facebook.internal.Utility;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.FilePath;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

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

    private EditText edEmgPhone1;
    private EditText edEmgPhone2;
    private EditText edEmgPhone3;

    private String selectedFilePath = "";

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

        edEmgPhone1 = (EditText) findViewById(R.id.activity_my_profile_edt_phone1);
        edEmgPhone1.setText(AppPref.getInstance().getEPhone1());

        edEmgPhone2 = (EditText) findViewById(R.id.activity_my_profile_edt_phone2);
        edEmgPhone2.setText(AppPref.getInstance().getEPhone2());

        edEmgPhone3 = (EditText) findViewById(R.id.activity_my_profile_edt_phone3);
        edEmgPhone3.setText(AppPref.getInstance().getEPhone3());

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
                edEmgPhone1.setEnabled(true);
                edEmgPhone1.setFocusableInTouchMode(true);
                edEmgPhone2.setEnabled(true);
                edEmgPhone2.setFocusableInTouchMode(true);
                edEmgPhone3.setEnabled(true);
                edEmgPhone3.setFocusableInTouchMode(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_my_profile_btn_browse:
                boolean status = Other.checkPermission1(getApplicationContext(), Other.PERMISSION_READ_INTERNAL_STORAGE,
                        Other.MY_PERMISSIONS_RESULT_READ_EXTERNAL_STORAGE);
                if (status) {
                    browse();
                }
                break;
            case R.id.activity_my_profile_btn_submit:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("api_key=").append(AppUrl.API_KEY);
                stringBuilder.append("&name=").append("" + activity_my_profile_edt_name.getText().toString());
                stringBuilder.append("&phone=").append("" + activity_my_profile_edt_phone.getText().toString());
                stringBuilder.append("&cus_id=").append("" + AppPref.getInstance().getUserID());
                stringBuilder.append("&emg_phone1=").append("" + edEmgPhone1.getText().toString());
                stringBuilder.append("&emg_phone2=").append("" + edEmgPhone2.getText().toString());
                stringBuilder.append("&emg_phone3=").append("" + edEmgPhone3.getText().toString());

                //   Log.d("Profile Activity ", base64String);
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

    private void imageUpload(final String imagePath) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, AppUrl.UPLOAD_PROFILE_PIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("data");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        smr.addFile("image", imagePath);
        smr.addStringParam("user_id", AppPref.getInstance().getUserID());
        GrabbitApplication.getInstance().addToRequestQueue(smr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri selectedFileUri = getImageUri(getApplicationContext(), photo);
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(selectedFilePath, options);
                activity_my_profile_img_photo.setImageBitmap(bitmap);
                imageUpload(selectedFilePath);
            } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                Uri selectedFileUri = data.getData();
                selectedFilePath = Other.getPath(selectedFileUri, getApplicationContext());
//                selectedFilePath = FilePath.getPath(this, selectedFileUri);
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                Bitmap bitmap = BitmapFactory.decodeFile(selectedFilePath, options);
                activity_my_profile_img_photo.setImageURI(selectedFileUri);
                imageUpload(selectedFilePath);
            }
        } catch (Exception e) {
            Log.w("Error", e.toString());
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Other.MY_PERMISSIONS_RESULT_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    browse();
                } else {
                    //code for deny
                }
                break;
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
                edEmgPhone1.setEnabled(false);
                edEmgPhone2.setEnabled(false);
                edEmgPhone3.setEnabled(false);

                toastMessage(jsonObject.getString("msg"));
                AppPref.getInstance().setUserName(activity_my_profile_edt_name.getText().toString());
                AppPref.getInstance().setUserMobile(activity_my_profile_edt_phone.getText().toString());
                AppPref.getInstance().setEPhone1(edEmgPhone1.getText().toString());
                AppPref.getInstance().setEPhone2(edEmgPhone2.getText().toString());
                AppPref.getInstance().setEPhone3(edEmgPhone3.getText().toString());
                onBackPressed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
