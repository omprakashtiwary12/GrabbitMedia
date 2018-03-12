package com.grabbit.daily_deals.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;
import com.grabbit.daily_deals.Activity.HelpActivity;
import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.Model.HospitalData;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.GetNearbyPlacesData;
import com.grabbit.daily_deals.Utility.Other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HospitalFragment extends Fragment implements RecyclerAdapter.onItemClickListener,
        RecyclerAdapter.ReturnView, GetNearbyPlacesData.iReturnData {

    private RecyclerView frag_nearby_rv;
    private RecyclerAdapter recyclerAdapter;
    private List<HospitalData> nearHospitalList = new ArrayList<HospitalData>();

    private double current_latitude;
    private double current_longitude;


    public HospitalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hospital, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String Hospital = "hospital";
        String url = Other.getUrl(AppPref.getInstance().getLat(), AppPref.getInstance().getLong(), Hospital);
        Object[] DataTransfer = new Object[2];
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(getActivity(), this);
        getNearbyPlacesData.execute(DataTransfer);
        //  Toast.makeText(HelpActivity.this, "Nearby Hospitals", Toast.LENGTH_LONG).show();

        frag_nearby_rv = (RecyclerView) view.findViewById(R.id.recycle_view_hospital_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(nearHospitalList, getActivity(), R.layout.item_nearby_hospital, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);
    }


    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            try {
                Log.d("onPostExecute", "Entered into showing locations " + nearHospitalList.size());
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                String place_id = googlePlace.get("place_id");
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                String rating = googlePlace.get("rating");
                String icon = googlePlace.get("icon");
                String open_now = googlePlace.get("isOpen");
                double distance = 0.0;
                if (lat != 0.0 && lng != 0.0) {
                    current_latitude = Double.valueOf(AppPref.getInstance().getLat());
                    current_longitude = Double.valueOf(AppPref.getInstance().getLong());
                    distance = Other.getDistance(current_latitude, current_longitude, lat, lng);
                }
                HospitalData hospitalData = new HospitalData(place_id, placeName, rating, icon, open_now, vicinity, lat, lng, distance);
                nearHospitalList.add(hospitalData);
            } catch (Exception e) {
                recyclerAdapter.notifyDataSetChanged();
            }
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getItemPosition(int position) {
        HospitalData hospitalData = nearHospitalList.get(position);
        showMap("" + hospitalData.getLatitude(), "" + hospitalData.getLongitude(), hospitalData.getName());
    }

    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        final HospitalData hospitalData = (nearHospitalList.get(position));
//        ImageView nearyby_item_IMG_place = (ImageView) view.findViewById(R.id.nearyby_hospital_item_IMG_place);
//        Picasso.with(getApplicationContext()).load(hospitalData.getIcon())
//                .placeholder(R.drawable.placeholder_banner).fit().into(nearyby_item_IMG_place);

        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        nearby_item_TXT_name.setText(hospitalData.getName());

        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);
        nearby_item_TXT_address.setText("" + hospitalData.getVicinity());

        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        nearby_item_TXT_distace.setText("" + hospitalData.getDistance() + " KM");
    }

    @Override
    public void returnNearPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        ShowNearbyPlaces(nearbyPlacesList);
    }

    public void showMap(String lat, String lng, String name) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + name + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
