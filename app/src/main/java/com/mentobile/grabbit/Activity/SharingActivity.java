package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/22/2016.
 */
public class SharingActivity extends BaseActivity {
    Button free_ride_btn_invite_friend;
    String sms = "my text for  sharing  data to  whatsapp";

    @Override
    public int getActivityLayout() {
        return R.layout.activity_sharing;
    }

    @Override
    public void initialize() {
        setTitle("Sharing");
        free_ride_btn_invite_friend = (Button) findViewById(R.id.free_ride_btn_invite_friend);
        free_ride_btn_invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "This  is  a testing  Message");
                startActivity(Intent.createChooser(i, sms));
            }
        });

    }

    @Override
    public void init(Bundle save) {

    }
}
