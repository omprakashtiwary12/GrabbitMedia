package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;

import org.json.JSONObject;

/**
 * Created by Gokul on 11/22/2016.
 */
public class SharingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SharingActivity";
    private TextView tvShareMessage;
    private ImageButton imgBtnWhatsApp;
    private ImageButton imgBtnFacebook;
    private ImageButton imgBtnMessage;
    private String shareText;
    private String displayMessage = "";

    @Override
    public int getActivityLayout() {
        return R.layout.activity_sharing;
    }

    @Override
    public void initialize() {
        setTitle("Share App");

        tvShareMessage = (TextView) findViewById(R.id.refer_tv_referral_message);
      //  tvShareMessage.setText("Install & share this app \n Get " + "\u20B9" + " 10.0 in PayTM wallet.");
        imgBtnWhatsApp = (ImageButton) findViewById(R.id.free_ride_imgbtn_whatsapp);
        imgBtnWhatsApp.setOnClickListener(this);
        imgBtnFacebook = (ImageButton) findViewById(R.id.free_ride_imgbtn_facebook);
        imgBtnFacebook.setOnClickListener(this);
        imgBtnMessage = (ImageButton) findViewById(R.id.free_ride_imgbtn_message);
        imgBtnMessage.setOnClickListener(this);
//        shareText = "Download the app get Rs 10/- now in your PayTM wallet and find best offers/deals near you." + "\n\n" + "http://bit.ly/2EqJ8i3";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.SHARING_URL, 0, content, true, "Loading...", new GetWebServiceData() {

            @Override
            public void getWebServiceResponse(String responseData, int serviceCounter) {
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String status = jsonObject.getString("status");
                    shareText = jsonObject.getString("shareText");
                    displayMessage = jsonObject.getString("displayMessage");
                    tvShareMessage.setText("" + displayMessage);
                } catch (Exception e) {
                }
            }
        });
        getDataUsingWService.execute();
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_ride_imgbtn_whatsapp:
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SharingActivity.this, "Some Error Occurred.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.free_ride_imgbtn_facebook:
            case R.id.free_ride_imgbtn_message:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite Friend");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(sharingIntent, "Share with"));
                break;
        }
    }
}
