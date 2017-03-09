package com.mentobile.grabbit.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/22/2016.
 */
public class MyProfileActivity extends BaseActivity implements View.OnClickListener {

    Button activity_my_profile_btn_browse;
    Button activity_my_profile_btn_save;
    EditText activity_my_profile_edt_phone;
    EditText activity_my_profile_edt_email;
    EditText activity_my_profile_edt_name;
    CircularImageView activity_my_profile_img_photo;
    String selectedFilePath;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_my_profile;
    }

    @Override
    public void initialize() {
        setTitle("My Profile");
        activity_my_profile_edt_phone = (EditText) findViewById(R.id.activity_my_profile_edt_phone);
        activity_my_profile_edt_email = (EditText) findViewById(R.id.activity_my_profile_edt_email);
        activity_my_profile_edt_name = (EditText) findViewById(R.id.activity_my_profile_edt_name);
        activity_my_profile_btn_browse = (Button) findViewById(R.id.activity_my_profile_btn_browse);
        activity_my_profile_btn_save = (Button) findViewById(R.id.activity_my_profile_btn_save);
        activity_my_profile_img_photo = (CircularImageView) findViewById(R.id.activity_my_profile_img_photo);

        activity_my_profile_btn_browse.setOnClickListener(this);
        activity_my_profile_btn_save.setOnClickListener(this);
        activity_my_profile_edt_phone.setText(AppPref.getInstance().getUserMobile());
        activity_my_profile_edt_email.setText(AppPref.getInstance().getUserEmail());
        activity_my_profile_edt_name.setText(AppPref.getInstance().getUserName());

        activity_my_profile_edt_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        activity_my_profile_edt_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        activity_my_profile_edt_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        // Picasso.with(this).load(AppUrl.PROFILE_PIC_URL + AppPref.getInstance().getUserID() + ".jpg").into(activity_my_profile_img_photo);
        try {
            Picasso.with(this).load(AppPref.getInstance().getImageUrl()).into(activity_my_profile_img_photo);
        } catch (Exception e) {

        }
        //Picasso.with(this).load(AppPref.getInstance().getImageUrl()).into(activity_my_profile_img_photo);
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
                edit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void edit() {
        activity_my_profile_btn_browse.setVisibility(View.VISIBLE);
        activity_my_profile_btn_save.setText("Save");

        activity_my_profile_edt_phone.setEnabled(true);
        activity_my_profile_edt_name.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_my_profile_btn_browse:
                browse();
                break;
            case R.id.activity_my_profile_btn_save:
                logoutorsave();
                break;
        }
    }

    private void logoutorsave() {
        if (activity_my_profile_btn_save.getText().toString().equalsIgnoreCase("save")) {
            save();
        } else if (activity_my_profile_btn_save.getText().toString().equalsIgnoreCase("logout")) {
            logout();
        }
    }

    private void logout() {
        AppPref.getInstance().setUserID("");
        AppPref.getInstance().setUserName("");
        AppPref.getInstance().setUserMobile("");
        AppPref.getInstance().setUserEmail("");
        sendToThisActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    private void save() {
        activity_my_profile_img_photo.setDrawingCacheEnabled(true);
        new ImageAsync(activity_my_profile_img_photo.getDrawingCache(), activity_my_profile_edt_name.getText().toString(), activity_my_profile_edt_phone.getText().toString()).execute();

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
        Bitmap photo;
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                photo = (Bitmap) data.getExtras().get("data");
                activity_my_profile_img_photo.setImageBitmap(photo);
//                selectedFilePath = saveToInternalStorage(photo);
//                Uri selectedFileUri = Uri.fromFile(new File(selectedFilePath));
//                selectedFilePath = FilePath.getPath(this, selectedFileUri);
//                Log.i("UpdatePhotoActivity", "Selected File Path:" + selectedFilePath);
//                //activity_register_profile_image.setImageBitmap(photo);
//                new ImageAsync1().execute();
            } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
                Uri selectedFileUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileUri);
                activity_my_profile_img_photo.setImageBitmap(bitmap);
//                selectedFilePath = FilePath.getPath(this, selectedFileUri);
//                Log.i("UpdatePhotoActivity", "Selected File Path:" + selectedFilePath);
//                new ImageAsync1().execute();
                //activity_register_profile_image.setImageURI(selectedFileUri);
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

    private class ImageAsync extends AsyncTask<Void, Void, String> {
        Bitmap bitmap;
        String encodedImage;
        String userName;
        String userMobile;
        String result;
        ProgressDialog pd;

        public ImageAsync(Bitmap bitmap, String userName, String userMobile) {
            this.bitmap = bitmap;
            this.userName = userName;
            this.userMobile = userMobile;
        }

        public void onPreExecute() {
            pd = new ProgressDialog(MyProfileActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Updating Profile ... ");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            encodedImage = getStringImage(bitmap);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppUrl.PROFILE_UPDATE_URL);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("cus_id", AppPref.getInstance().getUserID()));
                nameValuePairs.add(new BasicNameValuePair("picture", encodedImage));
                nameValuePairs.add(new BasicNameValuePair("mobile", userMobile));
                nameValuePairs.add(new BasicNameValuePair("name", userName));
                nameValuePairs.add(new BasicNameValuePair("api_key", AppUrl.API_KEY));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.w("Upload Profile Image", result);

            } catch (Exception e) {

            }

            return null;
        }

        public void onPostExecute(String msg) {
            try {
                pd.dismiss();
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    toastMessage(jsonObject.getString("msg"));
                    AppPref.getInstance().setUserName(activity_my_profile_edt_name.getText().toString());
                    AppPref.getInstance().setUserMobile(activity_my_profile_edt_phone.getText().toString());
                    finish();
                }
            } catch (Exception e) {

            }

        }
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return encodedImage;
    }
}
