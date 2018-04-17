package com.grabbit.daily_deals.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.model.Dash;
import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.EndlessRecyclerViewScrollListener;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements RecyclerAdapter.onItemClickListener,
        LoadDataFromServer.iGetResponse, RecyclerAdapter.ReturnView, View.OnClickListener, GetWebServiceData {

    private static final String TAG = "DashboardActivity";

    private List<NearByModel> nearByModelList = new ArrayList<NearByModel>();
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
    private TextView tvNotificationCount;
    TextView tvName;

    private LinearLayout btnEvents;
    private LinearLayout btnElectronics;
    private LinearLayout btnHospitality;
    private LinearLayout btnRetails;
    private LinearLayout btnSaloon_Spa;
    private LinearLayout btnTravel;
    private LinearLayout btnReal_state;

    private TextView tvEvents;
    private TextView tvElectronics;
    private TextView tvHospitality;
    private TextView tvRetails;
    private TextView tvSaloon_Spa;
    private TextView tvTravel;
    private TextView tvReal_state;

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

    private EndlessRecyclerViewScrollListener endlessScrollListener;
    boolean isScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(null);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        imgNotification = (ImageView) findViewById(R.id.notification);
        imgNotification.setOnClickListener(this);

        btnEvents = (LinearLayout) findViewById(R.id.btn_events);
        btnEvents.setOnClickListener(this);
        tvEvents = (TextView) findViewById(R.id.tv_events);

        btnElectronics = (LinearLayout) findViewById(R.id.btn_electronics);
        btnElectronics.setOnClickListener(this);
        tvElectronics = (TextView) findViewById(R.id.tv_electronics);

        btnHospitality = (LinearLayout) findViewById(R.id.btn_hospitality);
        btnHospitality.setOnClickListener(this);
        tvHospitality = (TextView) findViewById(R.id.tv_hospitality);

        btnRetails = (LinearLayout) findViewById(R.id.btn_retails);
        btnRetails.setOnClickListener(this);
        tvRetails = (TextView) findViewById(R.id.tv_retails);

        btnSaloon_Spa = (LinearLayout) findViewById(R.id.btn_saloon_spa);
        btnSaloon_Spa.setOnClickListener(this);
        tvSaloon_Spa = (TextView) findViewById(R.id.tv_saloon_spa);

        btnTravel = (LinearLayout) findViewById(R.id.btn_travel);
        btnTravel.setOnClickListener(this);
        tvTravel = (TextView) findViewById(R.id.tv_travel);

        btnReal_state = (LinearLayout) findViewById(R.id.btn_real_state);
        btnReal_state.setOnClickListener(this);
        tvReal_state = (TextView) findViewById(R.id.tv_real_state);

        fbSOS = (FloatingActionButton) findViewById(R.id.activity_dashboard_fb_sos);
        fbSOS.setOnClickListener(this);

        textOfferCount = (TextView) findViewById(R.id.dashboard_tv_offer_list);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.drawer_swipe_pull_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadDataFromServer != null) {
                    loadDataFromServer = null;
                }
                loadDataFromServer = new LoadDataFromServer(DashboardActivity.this, DashboardActivity.this, false);
                loadDataFromServer.startFatching(0, cat_id_on_swiperfersh);
                swipeRefreshLayout.setRefreshing(true);
            }
        });

//        cat_id_on_swiperfersh = CAT_ID_HOSPITALITY;
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        frag_nearby_rv.setLayoutManager(llm);
//        endlessScrollListener = new EndlessRecyclerViewScrollListener(llm) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                if (!isScroll) {
//                    if (loadDataFromServer != null) {
//                        loadDataFromServer = null;
//                    }
//                    loadDataFromServer = new LoadDataFromServer(DashboardActivity.this, DashboardActivity.this, false);
//                    loadDataFromServer.startFatching(page, cat_id_on_swiperfersh);
//                    swipeRefreshLayout.setRefreshing(true);
//                    Toast.makeText(DashboardActivity.this, "Load More " + page + " Item " +
//                            totalItemsCount, Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
        //frag_nearby_rv.addOnScrollListener(endlessScrollListener);
        llNoOffer = (LinearLayout) findViewById(R.id.fragment_img_no_offers);
        if (AppPref.getInstance().getEPhone1().length() < 10 && AppPref.getInstance().getEPhone2().length() < 10
                && AppPref.getInstance().getEPhone3().length() < 10 &&
                !AppPref.getInstance().getEmgEnableStatus().equalsIgnoreCase("block")) {
            dialogAddEmergencyNumber();
        }

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //tvNotificationCount = (TextView) findViewById(R.id.activity_dashboard_tv_notification_count);
        loadDataFromServer = new LoadDataFromServer(this, this, false);
        loadDataFromServer.startFatching(0, CAT_ID_HOSPITALITY);
        swipeRefreshLayout.setRefreshing(true);
        //setSelected(btnHospitality, tvHospitality);
    }

    @Override
    public void getItemPosition(int position) {
        MerchantDetailsActivity.nearByModel = nearByModelList.get(position);
        Intent intent = new Intent(DashboardActivity.this, MerchantDetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.drawer_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.menu_noti);
//        LayerDrawable layerDrawable = (LayerDrawable) menuItem.getIcon();
        int mNotificationCount = GrabbitApplication.database.getNotificationCount();
        // tvNotificationCount.setText("" + mNotificationCount);
        //Other.setBadgeCount(getApplicationContext(), layerDrawable, mNotificationCount);
        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_noti:
                Other.sendToThisActivity(DashboardActivity.this, NotificationActivity.class);
                break;
            default:
                break;
        }
        return true;
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

        TextView nearby_item_TXT_view_count = (TextView) view.findViewById(R.id.nearby_item_TXT_view_count);
        nearby_item_TXT_view_count.setText("" + nearByModel.getCount_click());

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
            uploadCategoryData(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_details_TV_title_back:
                onBackPressed();
                break;

            case R.id.my_profile:
                Other.sendToThisActivity(DashboardActivity.this, MyProfileActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.mysetting:
                Other.sendToThisActivity(DashboardActivity.this, SharingActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.my_refer:
                Other.sendToThisActivity(DashboardActivity.this, HelpActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.contact_us:
                Other.sendToThisActivity(DashboardActivity.this, OffersActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.btn_events:
                setSelected(btnEvents, tvEvents);
                uploadCategoryData(CAT_ID_EVENTS);
                break;

            case R.id.btn_electronics:
                setSelected(btnElectronics, tvElectronics);
                uploadCategoryData(CAT_ID_FITNES);
                break;

            case R.id.btn_hospitality:
                setSelected(btnHospitality, tvHospitality);
                uploadCategoryData(CAT_ID_HOSPITALITY);
                break;

            case R.id.btn_retails:
                setSelected(btnRetails, tvRetails);
                uploadCategoryData(CAT_ID_RETAILS);
                break;

            case R.id.btn_saloon_spa:
                setSelected(btnSaloon_Spa, tvSaloon_Spa);
                uploadCategoryData(CAT_ID_SALOON_SPA);
                break;

            case R.id.btn_travel:
                setSelected(btnTravel, tvTravel);
                uploadCategoryData(CAT_ID_TRAVEL);
                break;

            case R.id.btn_real_state:
                setSelected(btnReal_state, tvReal_state);
                uploadCategoryData(CAT_ID_REAL_STATE);
                break;

            case R.id.activity_dashboard_fb_sos:
                sendEmergencyMessage();
                break;
            case R.id.notification:
                Other.sendToThisActivity(DashboardActivity.this, NotificationActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private void setSelected(LinearLayout linearLayout, TextView textView) {
        btnHospitality.setAlpha(1.0f);
        tvHospitality.setTextColor(Color.WHITE);

        btnRetails.setAlpha(1.0f);
        tvRetails.setTextColor(Color.WHITE);

        btnTravel.setAlpha(1.0f);
        tvTravel.setTextColor(Color.WHITE);

        btnSaloon_Spa.setAlpha(1.0f);
        tvSaloon_Spa.setTextColor(Color.WHITE);

        btnEvents.setAlpha(1.0f);
        tvEvents.setTextColor(Color.WHITE);

        btnReal_state.setAlpha(1.0f);
        tvReal_state.setTextColor(Color.WHITE);

        btnElectronics.setAlpha(1.0f);
        tvElectronics.setTextColor(Color.WHITE);

        linearLayout.setAlpha(0.7f);
        textView.setTextColor(Color.GREEN);
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

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        if (responseData != null) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                if (serviceCounter == 0) {

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } else {
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    String phone1 = jsonObject.getString("phone1");
                    String phone2 = jsonObject.getString("phone2");
                    String phone3 = jsonObject.getString("phone3");

                    if (status.equalsIgnoreCase("1")) {
                        AppPref.getInstance().setEPhone1("" + phone1);
                        AppPref.getInstance().setEPhone2("" + phone2);
                        AppPref.getInstance().setEPhone3("" + phone3);
                        Snackbar.make(findViewById(R.id.coordinatorLayout),
                                "" + message, Snackbar.LENGTH_SHORT).show();
                        if (dialog != null)
                            dialog.cancel();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Dialog dialog = null;

    private void dialogAddEmergencyNumber() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_emergency_number);
        final EditText dialogEdEmg1 = (EditText) dialog.findViewById(R.id.activity_my_profile_edt_phone1);
        dialogEdEmg1.setText("" + AppPref.getInstance().getEPhone1());
        final EditText dialogEdEmg2 = (EditText) dialog.findViewById(R.id.activity_my_profile_edt_phone2);
        dialogEdEmg2.setText("" + AppPref.getInstance().getEPhone2());
        final EditText dialogEdEmg3 = (EditText) dialog.findViewById(R.id.activity_my_profile_edt_phone3);
        dialogEdEmg3.setText("" + AppPref.getInstance().getEPhone3());
        final CheckBox chkAskAgain = (CheckBox) dialog.findViewById(R.id.dialog_chk_ask_again);
        chkAskAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkAskAgain.isChecked()) {
                    AppPref.getInstance().setEmgEnableStatus("block");
                } else {
                    AppPref.getInstance().setEmgEnableStatus("");
                }
            }
        });

        Button fragment_forget_cancel = (Button) dialog.findViewById(R.id.fragment_forget_cancel);
        fragment_forget_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button dialog_btn_btn_submit = (Button) dialog.findViewById(R.id.dialog_btn_btn_submit);
        dialog_btn_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strEMG1 = dialogEdEmg1.getText().toString();
                String strEMG2 = dialogEdEmg2.getText().toString();
                String strEMG3 = dialogEdEmg3.getText().toString();
                if (TextUtils.isEmpty(strEMG1) && TextUtils.isEmpty(strEMG2) && TextUtils.isEmpty(strEMG3)) {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Enter emergency number.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("api_key=").append(AppUrl.API_KEY);
                stringBuilder.append("&cus_id=").append("" + AppPref.getInstance().getUserID());
                stringBuilder.append("&emg_phone1=").append("" + dialogEdEmg1.getText().toString());
                stringBuilder.append("&emg_phone2=").append("" + dialogEdEmg2.getText().toString());
                stringBuilder.append("&emg_phone3=").append("" + dialogEdEmg3.getText().toString());
                String content = stringBuilder.toString();
                GetDataUsingWService serviceProfileUpdate =
                        new GetDataUsingWService(DashboardActivity.this, AppUrl.UPDATE_EMERGENCY, 1, content, true, "Updating...", DashboardActivity.this);
                serviceProfileUpdate.execute();
            }
        });
        dialog.show();
    }

   /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    Other.sendToThisActivity(DashboardActivity.this, MyProfileActivity.class);
                    return true;
                case R.id.navigation_share:
                    Other.sendToThisActivity(DashboardActivity.this, SharingActivity.class);
                    return true;
                case R.id.navigation_help:
                    Other.sendToThisActivity(DashboardActivity.this, HelpActivity.class);
                    return true;
                case R.id.navigation_contact_us:
                    Other.sendToThisActivity(DashboardActivity.this, ContactUsActivity.class);
                    return true;
            }
            return false;
        }
    };*/
}
