package com.mentobile.grabbit.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mentobile.grabbit.Activity.MerchantDetailsActivity;
import com.mentobile.grabbit.Activity.SplashActivity;
import com.mentobile.grabbit.Adapter.RecyclerAdapter;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.CircleImageView1;
import com.mentobile.grabbit.Utility.Other;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/19/2016.
 */

public class WishListFragment extends Fragment implements RecyclerAdapter.ReturnView, RecyclerAdapter.onItemClickListener {

    private static final String TAG = "WishListFragment";
    RecyclerView frag_nearby_rv;
    RecyclerAdapter recyclerAdapter;

    private List<NearByModel> wishListArrayList = new ArrayList<NearByModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        frag_nearby_rv = (RecyclerView) view.findViewById(R.id.frag_nearby_rv);
        frag_nearby_rv = (RecyclerView) view.findViewById(R.id.frag_nearby_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(wishListArrayList, getActivity().getApplicationContext(), R.layout.item_nearby_adapter, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        wishListArrayList.clear();
        for (int i = 0; i < SplashActivity.nearByModelList.size(); i++) {
            NearByModel nearByModel = SplashActivity.nearByModelList.get(i);
            if (nearByModel.getWishlist().equals("1")) {
                wishListArrayList.add(nearByModel);
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void getItemPosition(int position) {
        NearByModel nearByModel = wishListArrayList.get(position);
        int indexValue  = SplashActivity.nearByModelList.indexOf(nearByModel);
        Other.sendToThisActivity(getActivity(), MerchantDetailsActivity.class, indexValue);
        Log.d(TAG,"::::Index Value "+indexValue);
    }

    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        final NearByModel nearByModel = wishListArrayList.get(position);

        ImageView nearyby_item_IMG_place = (ImageView) view.findViewById(R.id.nearyby_item_IMG_place);
        Picasso.with(getContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getBanner())
                .placeholder(R.drawable.placeholder_banner).into(nearyby_item_IMG_place);

        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        nearby_item_TXT_name.setText(nearByModel.getBusiness_name());

        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);
        nearby_item_TXT_address.setText("" + nearByModel.getAddress());

        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        if (nearByModel.getDistance() != null) {
            String distance[] = nearByModel.getDistance().trim().split(" ");
            Double distance_in_km = Double.parseDouble(distance[0]) * 1.60934;
            nearby_item_TXT_distace.setText(new DecimalFormat("##.##").format(distance_in_km) + "KM");
        }
        CircleImageView1 nearby_item_IMG_logo = (CircleImageView1) view.findViewById(R.id.nearby_item_IMG_logo);
        Picasso.with(getContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getLogo())
                .placeholder(R.drawable.placeholder_logo).into(nearby_item_IMG_logo);
    }
}

