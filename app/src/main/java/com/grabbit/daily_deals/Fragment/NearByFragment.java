package com.grabbit.daily_deals.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Dash;
import com.grabbit.daily_deals.Activity.DashboardActivity;
import com.grabbit.daily_deals.Activity.DrawerActivity;
import com.grabbit.daily_deals.Activity.LoadDataFromServer;
import com.grabbit.daily_deals.Activity.MerchantDetailsActivity;
import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.CircleImageView1;
import com.grabbit.daily_deals.Utility.Other;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NearByFragment extends Fragment implements RecyclerAdapter.ReturnView, RecyclerAdapter.onItemClickListener, LoadDataFromServer.iGetResponse {
    String TAG = "NearByFragment";
    private RecyclerView frag_nearby_rv;
    private RecyclerAdapter recyclerAdapter;
    LoadDataFromServer loadDataFromServer;
    SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        frag_nearby_rv = (RecyclerView) view.findViewById(R.id.frag_nearby_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter(DrawerActivity.nearByModelList, getActivity().getApplicationContext(), R.layout.item_nearby_adapter, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

//        loadDataFromServer = ((DashboardActivity) getActivity()).loadDataFromServer;
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.drawer_swipe_pull_to_refresh);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadDataFromServer.startFatching();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        imageView = (ImageView) view.findViewById(R.id.fragment_img_no_offers);
        return view;
    }

    public void refreshAdapter() {
        recyclerAdapter = new RecyclerAdapter(DrawerActivity.nearByModelList, getActivity().getApplicationContext(), R.layout.item_nearby_adapter, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        if (DrawerActivity.nearByModelList.size() < 1) {
            imageView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getItemPosition(int position) {
        Intent intent = new Intent(getActivity(), MerchantDetailsActivity.class);
        intent.putExtra("index_value", position);
        intent.putExtra("type", "n");
        getActivity().startActivity(intent);
    }

    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        final NearByModel nearByModel = (DrawerActivity.nearByModelList.get(position));
        ImageView nearyby_item_IMG_place = (ImageView) view.findViewById(R.id.nearyby_item_IMG_place);

        Picasso.with(getContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getBanner())
                .placeholder(R.drawable.placeholder_banner).into(nearyby_item_IMG_place);

        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        nearby_item_TXT_name.setText(nearByModel.getBusiness_name());

        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);
        nearby_item_TXT_address.setText("" + nearByModel.getAddress());

        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        nearby_item_TXT_distace.setText("" + nearByModel.getDistance() + " KM");

//        CircleImageView1 nearby_item_IMG_logo = (CircleImageView1) view.findViewById(R.id.nearby_item_IMG_logo);
//        Picasso.with(getContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getLogo())
//                .placeholder(R.drawable.placeholder_logo)
//                .fit()
//                .into(nearby_item_IMG_logo);
    }

    @Override
    public void getLoadDataResponse(boolean isStatus) {
//        if (isStatus) {
//            Log.d(TAG,"::::: refresh Adapter");
//            DrawerActivity.nearByModelList = new LoadDataFromServer().getMerchantList(((DrawerActivity)getActivity()).filterString);
//            refreshAdapter();
//        }
    }
}
