package com.mentobile.grabbit.Activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;

import com.mentobile.grabbit.Utility.FilePath;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;
import com.pkmmte.view.CircularImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
 * Created by Gokul on 11/23/2016.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {
    EditText act_signup_ET_lastName;
    EditText act_signup_ET_mobile;
    EditText act_signup_ET_email;
    EditText act_signup_ET_password;
    CircularImageView activity_register_profile_image;
    Button act_signup_BTN_next;
    String selectedFilePath;
    private static final int SIMPLE_LOGIN_TYPE = 0;
    private static final int FACEBOOK_LOGIN_TYPE = 2;


    private String gcm_id = "";

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
        activity_register_profile_image = (CircularImageView) findViewById(R.id.activity_register_profile_image);
        act_signup_BTN_next = (Button) findViewById(R.id.act_signup_BTN_next);
        act_signup_BTN_next.setOnClickListener(this);
        activity_register_profile_image.setOnClickListener(this);


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
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("api_key=").append(AppUrl.API_KEY);
//            stringBuilder.append("&name=").append(name);
//            stringBuilder.append("&phone=").append(mobile);
//            stringBuilder.append("&email=").append(email);
//            stringBuilder.append("&password=").append(password);
//            stringBuilder.append("&login_type=").append(SIMPLE_LOGIN_TYPE);
//            stringBuilder.append("&gcmid=").append(gcm_id);
//            String content = stringBuilder.toString();
            activity_register_profile_image.setDrawingCacheEnabled(true);
            new ImageAsync(activity_register_profile_image.getDrawingCache(), name, mobile, email, password, gcm_id, "0").execute();
//            GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.REGISTER_URL, 0, content, true, "Registering ...", this);
//            getDataUsingWService.execute();
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
                toastMessage(msg);
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                jsonObject = jsonArray.getJSONObject(0);
                String cus_id = jsonObject.getString("cus_id");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String name = jsonObject.getString("name");
                Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                sendToThisActivity(OtpActivity.class, new String[]{"from;register", "phone;" + act_signup_ET_mobile.getText().toString()});
                sendToThisActivity(DrawerActivity.class);
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


    private class ImageAsync extends AsyncTask<Void, Void, String> {
        Bitmap bitmap;
        String encodedImage;
        String userName;
        String userMobile;
        String email;
        String password;
        String gcm_id;
        String result;
        ProgressDialog pd;

        public ImageAsync(Bitmap bitmap, String userName, String userMobile, String email, String password, String gcm_id, String login_type) {
            this.bitmap = bitmap;
            this.userName = userName;
            this.userMobile = userMobile;
            this.email = email;
            this.password = password;
            this.gcm_id = gcm_id;

        }

        public void onPreExecute() {
            pd = new ProgressDialog(RegisterActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Register Profile ... ");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            encodedImage = getStringImage(bitmap);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppUrl.REGISTER_URL);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("api_key", AppUrl.API_KEY));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("name", userName));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("phone", userMobile));
                nameValuePairs.add(new BasicNameValuePair("photo", encodedImage));
                nameValuePairs.add(new BasicNameValuePair("gcmid", gcm_id));
                nameValuePairs.add(new BasicNameValuePair("login_type", "0"));
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

        public void onPostExecute(String msg1) {
            try {
                pd.dismiss();
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
                    // sendToThisActivity(OtpActivity.class, new String[]{"from;register", "phone;" + act_signup_ET_mobile.getText().toString()});
                    sendToThisActivity(DrawerActivity.class);
                } else {
                    String msg = jsonObject.getString("msg");
                    toastMessage(msg);
                    sendToThisActivity(LoginActivity.class);
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
