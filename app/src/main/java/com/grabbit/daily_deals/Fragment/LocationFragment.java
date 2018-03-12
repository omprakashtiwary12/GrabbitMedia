package com.grabbit.daily_deals.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;

import static android.app.Activity.RESULT_OK;

public class LocationFragment extends Fragment implements PlaceSelectionListener, View.OnClickListener {

    private static final int REQUEST_SELECT_PLACE = 1000;
    private iLocationFragment iLocationFragment;
    private EditText edGetLocation;
    private TextView tvSelectAddress;
    private ImageView btnBack;
    private Button btnOkay;

    public interface iLocationFragment {
        public void getCurrentLocation(String latitute, String longtitute);
    }

    @Override
    public void onDetach() {
        iLocationFragment.getCurrentLocation(AppPref.getInstance().getLat(), AppPref.getInstance().getLong());
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public LocationFragment() {
        // Required empty public constructor
    }

    public void setCommunicator(iLocationFragment iLocationFragment) {
        this.iLocationFragment = iLocationFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSelectAddress = (TextView) view.findViewById(R.id.select_address);
        tvSelectAddress.setOnClickListener(this);
        btnBack = (ImageView) view.findViewById(R.id.location_btn_back);
        btnBack.setOnClickListener(this);
        btnOkay = (Button) view.findViewById(R.id.select_address_ok);
        btnOkay.setOnClickListener(this);
        btnOkay.setVisibility(View.GONE);

        edGetLocation = (EditText) view.findViewById(R.id.location_ed_get_current_location);
        edGetLocation.setOnClickListener(this);

    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i("Tag", "Place Selected: " + place.getName());
        String place_name = place.getAddress().toString();
        String lat_long = place.getLatLng().latitude + "," + place.getLatLng().longitude;
        edGetLocation.setText(place_name);
        AppPref.getInstance().setLat("" + place.getLatLng().latitude);
        AppPref.getInstance().setLong("" + place.getLatLng().longitude);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getActivity(), "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_btn_back:
            case R.id.select_address_ok:
            case R.id.select_address:
                getActivity().onBackPressed();
                break;
            case R.id.location_ed_get_current_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
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
                btnOkay.setVisibility(View.VISIBLE);
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
