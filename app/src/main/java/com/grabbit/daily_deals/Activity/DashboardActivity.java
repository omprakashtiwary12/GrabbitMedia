package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements RecyclerAdapter.onItemClickListener,
        LoadDataFromServer.iGetResponse, RecyclerAdapter.ReturnView, View.OnClickListener, GetWebServiceData {

    private static final String TAG = "DashboardActivity";

    public static List<NearByModel> nearByModelList = new ArrayList<NearByModel>();
    private RecyclerView frag_nearby_rv;
    private RecyclerAdapter recyclerAdapter;
    public LoadDataFromServer loadDataFromServer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout tvMyProfile;
    private LinearLayout tvMySetting;
    private LinearLayout tvReferandEarn;
    private LinearLayout tvMyContactUs;
    private ImageView imgNotification;
    private TextView textOfferCount;
    TextView tvName;

    private Button btnAll;
    private Button btnEvents;
    private Button btnElectronics;
    private Button btnHospitality;
    private Button btnRetails;
    private Button btnSaloon_Spa;
    private Button btnTravel;
    private Button btnReal_state;

    private final int CAT_ID_ALL = 100;
    private final int CAT_ID_HOSPITALITY = 1;
    private final int CAT_ID_RETAILS = 2;
    private final int CAT_ID_EVENTS = 3;
    private final int CAT_ID_TRAVEL = 4;
    private final int CAT_ID_SALOON_SPA = 5;
    private final int CAT_ID_FITNES = 6;
    private final int CAT_ID_REAL_STATE = 7;

    private int cat_id_on_swiperfersh = 100;

    private LinearLayout llNoOffer;
    private VideoView videoView;

    int[] cat_icon = {R.drawable.hospitality, R.drawable.retail,
            R.drawable.event, R.drawable.travel, R.drawable.saloon_spa, R.drawable.fitness, R.drawable.real_estate};

    private ImageView imgBtn_Back;
    private FloatingActionButton fbSOS;

    @Override
    protected void onResume() {
        super.onResume();
        if (loadDataFromServer == null) {
            //loadDataFromServer = new LoadDataFromServer(this, this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        loadDataFromServer = new LoadDataFromServer(this, this, false);
        frag_nearby_rv = (RecyclerView) findViewById(R.id.frag_nearby_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);
        tvName = (TextView) findViewById(R.id.dashboard_tv_name);
        try {
            String name[] = AppPref.getInstance().getUserName().split(" ");
            tvName.setText("Welcome " + name[0]);
        } catch (Exception e) {
            tvName.setText("Welcome " + AppPref.getInstance().getUserName());
        }
        imgBtn_Back = (ImageView) findViewById(R.id.act_details_TV_title_back);
        imgBtn_Back.setOnClickListener(this);

        tvMyProfile = (LinearLayout) findViewById(R.id.my_profile);
        tvMyProfile.setOnClickListener(this);

        tvMySetting = (LinearLayout) findViewById(R.id.mysetting);
        tvMySetting.setOnClickListener(this);

        tvReferandEarn = (LinearLayout) findViewById(R.id.my_refer);
        tvReferandEarn.setOnClickListener(this);

        tvMyContactUs = (LinearLayout) findViewById(R.id.contact_us);
        tvMyContactUs.setOnClickListener(this);

        imgNotification = (ImageView)findViewById(R.id.notification);
        imgNotification.setOnClickListener(this);

        btnAll = (Button) findViewById(R.id.btn_all);
        btnAll.setOnClickListener(this);
        btnAll.setText(getMerchantCount(CAT_ID_ALL));

        btnEvents = (Button) findViewById(R.id.btn_events);
        btnEvents.setText(getMerchantCount(CAT_ID_EVENTS));
        btnEvents.setOnClickListener(this);

        btnElectronics = (Button) findViewById(R.id.btn_electronics);
        btnElectronics.setText(getMerchantCount(CAT_ID_FITNES));
        btnElectronics.setOnClickListener(this);

        btnHospitality = (Button) findViewById(R.id.btn_hospitality);
        btnHospitality.setOnClickListener(this);
        btnHospitality.setText(getMerchantCount(CAT_ID_HOSPITALITY));

        btnRetails = (Button) findViewById(R.id.btn_retails);
        btnRetails.setOnClickListener(this);
        btnRetails.setText(getMerchantCount(CAT_ID_RETAILS));

        btnSaloon_Spa = (Button) findViewById(R.id.btn_saloon_spa);
        btnSaloon_Spa.setOnClickListener(this);
        btnSaloon_Spa.setText(getMerchantCount(CAT_ID_SALOON_SPA));

        btnTravel = (Button) findViewById(R.id.btn_travel);
        btnTravel.setOnClickListener(this);
        btnTravel.setText(getMerchantCount(CAT_ID_TRAVEL));

        btnReal_state = (Button) findViewById(R.id.btn_real_state);
        btnReal_state.setOnClickListener(this);
        btnReal_state.setText(getMerchantCount(CAT_ID_REAL_STATE));

        fbSOS = (FloatingActionButton) findViewById(R.id.activity_dashboard_fb_sos);
        fbSOS.setOnClickListener(this);

        textOfferCount = (TextView) findViewById(R.id.dashboard_tv_offer_list);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.drawer_swipe_pull_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromServer.startFatching();
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        llNoOffer = (LinearLayout) findViewById(R.id.fragment_img_no_offers);
        //        String videoPath = "https://www.grabbit.co.in/presentation/4855060_fa6f22a8f4caddd142cb2746cd92929318323485.mp4";
//        videoView = (VideoView) findViewById(R.id.dashboard_video_view);
//        videoView.setVideoPath(videoPath);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);
        uploadCategoryData(CAT_ID_ALL);
        setSelected(btnAll);
    }

    @Override
    public void getItemPosition(int position) {
        Intent intent = new Intent(DashboardActivity.this, MerchantDetailsActivity.class);
        intent.putExtra("index_value", position);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void getAdapterView(final View view, List objects, final int position, int from) {
        final NearByModel nearByModel = (nearByModelList.get(position));
        ImageView nearyby_item_IMG_place = (ImageView) view.findViewById(R.id.nearyby_item_IMG_place);
        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        if (nearByModel.getBusiness_name().length() > 50) {
            Log.d(TAG, ":::::length " + nearByModel.getBusiness_name().length());
            nearby_item_TXT_name.setText(nearByModel.getBusiness_name() + "...");
        } else {
            nearby_item_TXT_name.setText("" + nearByModel.getBusiness_name());
        }

        TextView nearby_item_TXT_phone = (TextView) view.findViewById(R.id.nearby_item_TXT_phone_number);
        nearby_item_TXT_phone.setText("" + nearByModel.getAddress());

        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);

        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        nearby_item_TXT_distace.setText("" + nearByModel.getDistance() + " KM");

        TextView nearby_item_TXT_deals_count = (TextView) view.findViewById(R.id.nearby_item_TXT_deals_count);
        nearby_item_TXT_deals_count.setText("" + nearByModel.getOfferImageModels().size());

        ImageView nearyby_item_IMG_cat_icon = (ImageView) view.findViewById(R.id.nearby_item_img_cat_icon);
        int cat_id = Integer.parseInt(nearByModel.getCategory_id());
        nearyby_item_IMG_cat_icon.setBackgroundResource(cat_icon[cat_id - 1]);

        if (nearByModel.getOfferImageModels().size() > 0) {
            String name = nearByModel.getOfferImageModels().get(0).getName();
            String offer_description = nearByModel.getOfferImageModels().get(0).getOffer_description();
            nearby_item_TXT_address.setText("" + offer_description);
            Picasso.with(getApplicationContext()).load(name).fit()
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .networkPolicy(NetworkPolicy.NO_CACHE).
                    .placeholder(R.drawable.placeholder_banner).
                    into(nearyby_item_IMG_place);
        }

        TextView textView = (TextView) view.findViewById(R.id.nearby_item_TXT_view_map);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Other.viewMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getBusiness_name(), DashboardActivity.this);
            }
        });
    }

    @Override
    public void getLoadDataResponse(boolean isStatus) {
        if (isStatus) {
            swipeRefreshLayout.setRefreshing(false);
            uploadCategoryData(cat_id_on_swiperfersh);
        }
    }

    @Override
    public void onClick(View v) {
        setUnSelected();
        switch (v.getId()) {
            case R.id.act_details_TV_title_back:
                onBackPressed();
                break;

            case R.id.my_profile:
                Other.sendToThisActivity(DashboardActivity.this, MyProfileActivity.class);
                break;

            case R.id.mysetting:
                Other.sendToThisActivity(DashboardActivity.this, SharingActivity.class);
                break;

            case R.id.my_refer:
                Other.sendToThisActivity(DashboardActivity.this, HelpActivity.class);
                break;

            case R.id.contact_us:
                Other.sendToThisActivity(DashboardActivity.this, ContactUsActivity.class);
                break;

            case R.id.btn_all:
                setSelected(btnAll);
                uploadCategoryData(CAT_ID_ALL);
                break;

            case R.id.btn_events:
                setSelected(btnEvents);
                uploadCategoryData(CAT_ID_EVENTS);
                break;

            case R.id.btn_electronics:
                setSelected(btnElectronics);
                uploadCategoryData(CAT_ID_FITNES);
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

            case R.id.activity_dashboard_fb_sos:
                sendEmergencyMessage();
                break;
            case R.id.notification:
                Other.sendToThisActivity(DashboardActivity.this, NotificationActivity.class);
                break;
        }
    }

    private void setUnSelected() {

        btnAll.setSelected(false);
        btnAll.setTextColor(Color.DKGRAY);

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
        button.setTextColor(Color.RED);
    }

    private void sendEmergencyMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&cus_id=").append("" + AppPref.getInstance().getUserID());
        stringBuilder.append("&current_location=").append(Other.getCurrentAddress(DashboardActivity.this));
        String content = stringBuilder.toString();
        GetDataUsingWService serviceProfileUpdate =
                new GetDataUsingWService(DashboardActivity.this, AppUrl.EMERGENCY_MESSAGE, 0, content, true, "Sending...", this);
        serviceProfileUpdate.execute();
    }

    private void uploadCategoryData(int cat_id) {
        cat_id_on_swiperfersh = cat_id;
        nearByModelList = loadDataFromServer.getMerchantList(cat_id);
        textOfferCount.setText("" + getOfferList(cat_id) + " OFFER");
        recyclerAdapter = new RecyclerAdapter(nearByModelList, getApplicationContext(), R.layout.item_nearby_adapter, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        if (nearByModelList.size() < 1) {
            llNoOffer.setVisibility(View.VISIBLE);
            frag_nearby_rv.setVisibility(View.GONE);
        } else {
            llNoOffer.setVisibility(View.GONE);
            frag_nearby_rv.setVisibility(View.VISIBLE);
        }
    }

    private int getOfferList(int cat_id) {
        nearByModelList = loadDataFromServer.getMerchantList(cat_id);
        int count = 0;
        for (int i = 0; i < nearByModelList.size(); i++) {
            NearByModel nearByModel = nearByModelList.get(i);
            count += nearByModel.getOfferImageModels().size();
        }
        return count;
    }

    private String getMerchantCount(int cat_id) {
        int size = loadDataFromServer.getMerchantList(cat_id).size();
        Log.d(TAG, "::::Cat Id " + cat_id + " Size " + size);
        return "" + size;
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        if (responseData != null) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
