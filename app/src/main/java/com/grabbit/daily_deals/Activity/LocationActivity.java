package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.grabbit.daily_deals.Fragment.LocationFragment;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.BaseActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grabbit.daily_deals.currentLocation.CurrentLocation;

public class LocationActivity extends BaseActivity implements PlaceSelectionListener, View.OnClickListener {
    private static final int REQUEST_SELECT_PLACE = 1000;
    private EditText edGetLocation;

    private iLocationService locationService;

    public interface iLocationService {
        public void getCurrentLocation(double latitute, double longtitute);
    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_location;
    }

    @Override
    public void initialize() {
        //locationService = (iLocationService) this;
        edGetLocation = (EditText) findViewById(R.id.location_ed_get_current_location);
        edGetLocation.setOnClickListener(this);
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i("Tag", "Place Selected: " + place.getName());
        String place_name = place.getAddress().toString();
        String lat_long = place.getLatLng().latitude + "," + place.getLatLng().longitude;
        edGetLocation.setText(place_name);
        Intent intent = getIntent();
        setResult(1, intent);
        //locationService.getCurrentLocation(place.getLatLng().latitude, place.getLatLng().longitude);
        finish();
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_ed_get_current_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(LocationActivity.this);
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
