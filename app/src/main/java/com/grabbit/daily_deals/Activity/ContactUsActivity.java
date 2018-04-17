package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.Other;

/**
 * Created by Gokul on 11/22/2016.
 */
public class ContactUsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    @Override
    public int getActivityLayout() {
        return R.layout.activity_contact_us;
    }

    @Override
    public void initialize() {
        setTitle("Contact Us");
        MapFragment mapFragment = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
    }

    @Override
    public void init(Bundle save) {

        TextView tvWebsite = (TextView) findViewById(R.id.contact_us_tv_website);
        tvWebsite.setOnClickListener(this);

        TextView tvMobile = (TextView) findViewById(R.id.contact_us_tv_mobile);
        tvMobile.setOnClickListener(this);

        TextView tvEmail = (TextView) findViewById(R.id.contact_us_tv_email);
        tvEmail.setOnClickListener(this);

        TextView tvAddress = (TextView) findViewById(R.id.contact_us_tv_address);
        tvAddress.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.contact_us_tv_website:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.grabbit.co.in/"));
                startActivity(i);
                break;

            case R.id.contact_us_tv_mobile:
                Other.callNow(ContactUsActivity.this, "+91 9910605600");
                break;

            case R.id.contact_us_tv_email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;

            case R.id.contact_us_tv_address:
                Other.viewMap("28.6483015", "77.1226343", "Grabbit Media Pvt. Ltd.",
                        ContactUsActivity.this);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLng latLng = new LatLng(28.6483015, 77.1226343);
        map.addMarker(new MarkerOptions().position(latLng).title("Grabbit").snippet("Grabbit"));
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(10f).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
}
