package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.Other;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements RecyclerAdapter.onItemClickListener, LoadDataFromServer.iGetResponse, RecyclerAdapter.ReturnView, View.OnClickListener {

    private static final String TAG = "DashboardActivity";

    public static List<NearByModel> nearByModelList = new ArrayList<NearByModel>();
    private RecyclerView frag_nearby_rv;
    private RecyclerAdapter recyclerAdapter;
    private LoadDataFromServer loadDataFromServer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout tvMyProfile;
    private LinearLayout tvMySetting;
    private LinearLayout tvReferandEarn;
    private LinearLayout tvMyContactUs;

    private Button btnEvents;
    private Button btnElectronics;
    private Button btnHospitality;
    private Button btnRetails;
    private Button btnSaloon_Spa;
    private Button btnTravel;
    private Button btnReal_state;

    private final int CAT_ID_HOSPITALITY = 1;
    private final int CAT_ID_RETAILS = 2;
    private final int CAT_ID_EVENTS = 3;
    private final int CAT_ID_TRAVEL = 4;
    private final int CAT_ID_SALOON_SPA = 5;
    private final int CAT_ID_ELECTRONICS = 6;
    private final int CAT_ID_REAL_STATE = 7;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        loadDataFromServer = new LoadDataFromServer(this, this);

        frag_nearby_rv = (RecyclerView) findViewById(R.id.frag_nearby_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);

        imageView = (ImageView) findViewById(R.id.fragment_img_no_offers);

        tvMyProfile = (LinearLayout) findViewById(R.id.my_profile);
        tvMyProfile.setOnClickListener(this);

        tvMySetting = (LinearLayout) findViewById(R.id.mysetting);
        tvMySetting.setOnClickListener(this);

        tvReferandEarn = (LinearLayout) findViewById(R.id.my_refer);
        tvReferandEarn.setOnClickListener(this);

        tvMyContactUs = (LinearLayout) findViewById(R.id.contact_us);
        tvMyContactUs.setOnClickListener(this);

        btnEvents = (Button) findViewById(R.id.btn_events);
        btnEvents.setOnClickListener(this);
        btnEvents.setText(loadDataFromServer.getOfferCount(CAT_ID_EVENTS));

        btnElectronics = (Button) findViewById(R.id.btn_electronics);
        btnElectronics.setOnClickListener(this);
        btnElectronics.setText(loadDataFromServer.getOfferCount(CAT_ID_ELECTRONICS));

        btnHospitality = (Button) findViewById(R.id.btn_hospitality);
        btnHospitality.setOnClickListener(this);
        btnHospitality.setText(loadDataFromServer.getOfferCount(CAT_ID_HOSPITALITY));

        btnRetails = (Button) findViewById(R.id.btn_retails);
        btnRetails.setOnClickListener(this);
        btnRetails.setText(loadDataFromServer.getOfferCount(CAT_ID_RETAILS));

        btnSaloon_Spa = (Button) findViewById(R.id.btn_saloon_spa);
        btnSaloon_Spa.setOnClickListener(this);
        btnSaloon_Spa.setText(loadDataFromServer.getOfferCount(CAT_ID_SALOON_SPA));

        btnTravel = (Button) findViewById(R.id.btn_travel);
        btnTravel.setOnClickListener(this);
        btnEvents.setText(loadDataFromServer.getOfferCount(CAT_ID_TRAVEL));

        btnReal_state = (Button) findViewById(R.id.btn_real_state);
        btnReal_state.setOnClickListener(this);
        btnReal_state.setText(loadDataFromServer.getOfferCount(CAT_ID_REAL_STATE));

        TextView textOfferCount = (TextView) findViewById(R.id.dashboard_tv_offer_list);
        textOfferCount.setText(" " + loadDataFromServer.getTotalOfferCount() + " Offers ");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.drawer_swipe_pull_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromServer.startFatching();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        uploadCategoryData(3);
    }

    @Override
    public void getItemPosition(int position) {
        Intent intent = new Intent(DashboardActivity.this, MerchantDetailsActivity.class);
        intent.putExtra("index_value", position);
        startActivity(intent);
    }

    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        final NearByModel nearByModel = (nearByModelList.get(position));
        ImageView nearyby_item_IMG_place = (ImageView) view.findViewById(R.id.nearyby_item_IMG_place);
        Picasso.with(getApplicationContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getBanner())
                .placeholder(R.drawable.placeholder_banner).fit().into(nearyby_item_IMG_place);

        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        nearby_item_TXT_name.setText(nearByModel.getBusiness_name());

        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);
        nearby_item_TXT_address.setText("" + nearByModel.getAddress());

        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        nearby_item_TXT_distace.setText("" + nearByModel.getDistance() + " KM");
    }

    @Override
    public void getLoadDataResponse(boolean isStatus) {
        if (isStatus) {
            Log.d(TAG, "::::: refresh Adapter");
            uploadCategoryData(3);
        }
    }

    @Override
    public void onClick(View v) {
        setUnSelected();
        switch (v.getId()) {
            case R.id.my_profile:
                Other.sendToThisActivity(DashboardActivity.this, MyProfileActivity.class);
                break;

            case R.id.mysetting:
                Other.sendToThisActivity(DashboardActivity.this, NotificationActivity.class);
                break;

            case R.id.my_refer:
                Other.sendToThisActivity(DashboardActivity.this, SharingActivity.class);
                break;

            case R.id.contact_us:
                Other.sendToThisActivity(DashboardActivity.this, ContactUsActivity.class);
                break;

            case R.id.btn_events:
                setSelected(btnEvents);
                uploadCategoryData(CAT_ID_EVENTS);
                break;

            case R.id.btn_electronics:

                setSelected(btnElectronics);
                uploadCategoryData(CAT_ID_ELECTRONICS);
                break;

            case R.id.btn_hospitality:
                setSelected(btnHospitality);
                uploadCategoryData(CAT_ID_HOSPITALITY);
                break;

            case R.id.btn_retails:
                setSelected(btnRetails);
                uploadCategoryData(CAT_ID_RETAILS);
                break;

            case R.id.btn_saloon_spa:
                setSelected(btnSaloon_Spa);
                uploadCategoryData(CAT_ID_SALOON_SPA);
                break;

            case R.id.btn_travel:
                setSelected(btnTravel);
                uploadCategoryData(CAT_ID_TRAVEL);
                break;

            case R.id.btn_real_state:
                setSelected(btnReal_state);
                uploadCategoryData(CAT_ID_REAL_STATE);
                break;
        }
    }

    private void setUnSelected() {
        btnEvents.setSelected(false);
        btnEvents.setTextColor(Color.DKGRAY);

        btnHospitality.setSelected(false);
        btnHospitality.setTextColor(Color.DKGRAY);

        btnElectronics.setSelected(false);
        btnElectronics.setTextColor(Color.DKGRAY);

        btnReal_state.setSelected(false);
        btnReal_state.setTextColor(Color.DKGRAY);

        btnTravel.setSelected(false);
        btnTravel.setTextColor(Color.DKGRAY);

        btnSaloon_Spa.setSelected(false);
        btnSaloon_Spa.setTextColor(Color.DKGRAY);

        btnRetails.setSelected(false);
        btnRetails.setTextColor(Color.DKGRAY);
    }

    private void setSelected(Button button) {
        button.setSelected(true);
        button.setTextColor(Color.WHITE);
    }

    private void uploadCategoryData(int cat_id) {
        nearByModelList = new LoadDataFromServer().getMerchantList(cat_id);
        recyclerAdapter = new RecyclerAdapter(nearByModelList, getApplicationContext(), R.layout.item_nearby_adapter, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        if (nearByModelList.size() < 1) {
            imageView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }
}
