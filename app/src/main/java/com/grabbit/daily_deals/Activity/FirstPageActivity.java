package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.grabbit.daily_deals.Fragment.LocationFragment;
import com.grabbit.daily_deals.Fragment.NetworkErrorFragment;
import com.grabbit.daily_deals.LocationUtil.LocationHelper;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.grabbit.daily_deals.currentLocation.CurrentLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.spec.EncodedKeySpec;

import javax.crypto.EncryptedPrivateKeyInfo;

import kotlin.text.Charsets;

public class FirstPageActivity extends BaseActivity implements LoadDataFromServer.iGetResponse,
        GetWebServiceData, LocationFragment.iLocationFragment {
    public final static int RESULT_BLUETOOTH = 101;
    private static final String TAG = "FirstPageActivity";
    private ProgressBar progressBar;
    private boolean isThread = true;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_first_page;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void init(Bundle save) {
        getSupportActionBar().hide();
        // progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "::::Token " + token);
//        String data = "hello world";
//        byte[] dataBytes = data.getBytes(Charsets.UTF_8);
//        String base64String = Base64.encodeToString(dataBytes, 0);
//        Log.d(TAG, "::::: base 64tring " + base64String);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isThread) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isThread = false;
                            Bundle bundle = getIntent().getExtras();
                            if (!Other.isNetworkAvailable(getApplicationContext())) {
                                NetworkErrorFragment networkErrorFragment = new NetworkErrorFragment();
                                getFragmentManager().beginTransaction().
                                        add(android.R.id.content, networkErrorFragment).commit();
                            }
//                            else if (!Other.checkBluetoothConnection() &&
//                                    !AppPref.getInstance().getBleEnableStatus().equalsIgnoreCase("block")) {
//                                Intent intent = new Intent(FirstPageActivity.this, BluetoothActivity.class);
//                                startActivityForResult(intent, RESULT_BLUETOOTH);
//                            }
                            else if (bundle != null && bundle.getBoolean("isProd")) {
                                sendEmergencyMessage();
                            } else {
                                new CurrentLocation().getLocation(FirstPageActivity.this, new CurrentLocation.LocationResult() {
                                    @Override
                                    public void gotLocation(Location location) {
                                        if (location != null) {
                                            AppPref.getInstance().setLat("" + location.getLatitude());
                                            AppPref.getInstance().setLong("" + location.getLongitude());
                                            switchActivity();
                                        } else {
                                            Snackbar.make(findViewById(R.id.activity_first_page), "You will not able to see offers, Please turn on your location.", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, ":::::Permission Result ");
        // If request is cancelled, the result arrays are empty.
        if ((grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            //startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),1);
//            progressBar.setVisibility(View.VISIBLE);
//            LoadDataFromServer loadDataFromServer = new LoadDataFromServer(FirstPageActivity.this, FirstPageActivity.this, false);
//            loadDataFromServer.startFatching();
            switchActivity();
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
//            LocationFragment locationFragment = new LocationFragment();
//            locationFragment.setCommunicator(FirstPageActivity.this);
//            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            fragmentTransaction.add(android.R.id.content, locationFragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
            Snackbar.make(findViewById(R.id.activity_first_page), "You will not able to see offers, Please turn on your location.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void sendEmergencyMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&cus_id=").append("" + AppPref.getInstance().getUserID());
        stringBuilder.append("&current_location=").append(Other.getCurrentAddress(FirstPageActivity.this));
        String content = stringBuilder.toString();
        GetDataUsingWService serviceProfileUpdate =
                new GetDataUsingWService(FirstPageActivity.this, AppUrl.EMERGENCY_MESSAGE, 0, content, true, "Sending...", this);
        serviceProfileUpdate.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        if (responseData != null) {
            try {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //locationHelper.onActivityResult(requestCode,resultCode,data);
        //LoadDataFromServer loadDataFromServer = new LoadDataFromServer(this, this, false);
        //loadDataFromServer.startFatching();
    }

    private void switchActivity() {
        if (AppPref.getInstance().isLogin()) {
            Intent intent = new Intent(FirstPageActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(FirstPageActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void getLoadDataResponse(boolean isStatus) {
        if (isStatus) {
            progressBar.setVisibility(View.GONE);
            if (AppPref.getInstance().isLogin()) {
                Intent intent = new Intent(FirstPageActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(FirstPageActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void getCurrentLocation(String latitute, String longtitute) {
        AppPref.getInstance().setLat("" + latitute);
        AppPref.getInstance().setLong("" + longtitute);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&latitude=").append(latitute);
        stringBuilder.append("&longitude=").append(longtitute);
        String content = stringBuilder.toString();
        Log.d(TAG, ":::::::Load Data " + content);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(FirstPageActivity.this, AppUrl.MERCHANTS_URL, 0, content, false, "Loading ...", FirstPageActivity.this);
        getDataUsingWService.execute();
    }
}
