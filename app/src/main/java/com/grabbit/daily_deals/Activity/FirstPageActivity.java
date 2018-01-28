package com.grabbit.daily_deals.Activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.grabbit.daily_deals.Fragment.NetworkErrorFragment;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstPageActivity extends BaseActivity implements LoadDataFromServer.iGetResponse, GetWebServiceData {

    public final static int RESULT_BLUETOOTH = 101;
    private ProgressBar progressBar;

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Bundle bundle = getIntent().getExtras();
        if (!Other.isNetworkAvailable(getApplicationContext())) {
            NetworkErrorFragment networkErrorFragment = new NetworkErrorFragment();
            getFragmentManager().beginTransaction().
                    add(android.R.id.content, networkErrorFragment).commit();
        } else if (!Other.checkBluetoothConnection()) {
            Intent intent = new Intent(FirstPageActivity.this, BluetoothActivity.class);
            startActivityForResult(intent, RESULT_BLUETOOTH);
        } else if (bundle != null && bundle.getBoolean("isProd")) {
            sendEmergencyMessage();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            LoadDataFromServer loadDataFromServer = new LoadDataFromServer(this, this, false);
            loadDataFromServer.startFatching();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_BLUETOOTH) {
            new LoadDataFromServer(this, this).startFatching();
        }
    }

    @Override
    public void getLoadDataResponse(boolean isStatus) {
        if (isStatus) {
            progressBar.setVisibility(View.GONE);
//            Intent intent = new Intent(FirstPageActivity.this, SensorActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();

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
}
