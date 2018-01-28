package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grabbit.daily_deals.Adapter.ViewPagerAdapter;
import com.grabbit.daily_deals.Fragment.ImageViewFragment;
import com.grabbit.daily_deals.Model.ImageModel;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gokul on 11/28/2016.
 */

public class MerchantDetailsActivity extends AppCompatActivity implements View.OnClickListener, ViewPagerAdapter.pageonClick {

    private String TAG = "MerchantDetailsActivity";

    private ImageView frameLayout;
    private TextView tvTitle;
    private TextView tvAddress;
    private ImageView imgBtn_Back;
    private TextView tvAboutAddress;
    private TextView tvMobile;
    private TextView tvWorkingHours;
    private TextView tvDistance;
    private TextView tvWebsiteName;
    private TextView tvWorkingDays;
    private TextView tvOpeningDays;
    private TextView tvEmail;

    private ImageView imgBtnFacebook;
    private ImageView imgBtnTwitter;
    private ImageView imgBtnInstagram;

    private RelativeLayout llOfferList;
    private RelativeLayout llGalleryView;
    public NearByModel nearByModel;

    TextView tvDescription;

    private Button btnWishList;

    private ViewPager viewPagerCompaign;
    private ViewPager viewPagerGalleryView;

    public String strPagerType;
    public int pagerPosition;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details);
        getSupportActionBar().hide();

        final int pos = getIntent().getExtras().getInt("index_value");
        nearByModel = DashboardActivity.nearByModelList.get(pos);

//        frameLayout = (ImageView) findViewById(R.id.frameLayout);
//        Picasso.with(getApplicationContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getBanner())
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .placeholder(R.drawable.placeholder_banner).fit().into(frameLayout);

        imgBtn_Back = (ImageView) findViewById(R.id.act_details_TV_title_back);
        imgBtn_Back.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.act_details_TV_title_name);
        tvTitle.setText(nearByModel.getBusiness_name());

        tvAboutAddress = (TextView) findViewById(R.id.act_details_TV_address);
        tvAboutAddress.setText(nearByModel.getFullAddress());
        tvAboutAddress.setOnClickListener(this);

        tvMobile = (TextView) findViewById(R.id.act_details_TV_mobile);
        tvMobile.setText("Call @ " + nearByModel.getPhone());
        tvMobile.setOnClickListener(this);

        tvWorkingHours = (TextView) findViewById(R.id.act_details_TV_workingHours);
        tvWorkingHours.setText(nearByModel.getOpen_time() + " To " + nearByModel.getClose_time());

        tvDistance = (TextView) findViewById(R.id.act_details_TV_distance);
        tvDistance.setText("" + nearByModel.getDistance() + " KM");

        tvWebsiteName = (TextView) findViewById(R.id.act_details_TV_website);
        tvWebsiteName.setText(nearByModel.getWebsite());
        tvWebsiteName.setOnClickListener(this);

        tvWorkingDays = (TextView) findViewById(R.id.act_details_TV_weekdays_off);

        imgBtnFacebook = (ImageView) findViewById(R.id.act_details_IV_facebook);
        imgBtnFacebook.setOnClickListener(this);

        imgBtnTwitter = (ImageView) findViewById(R.id.act_details_IV_twitter);
        imgBtnTwitter.setOnClickListener(this);

        imgBtnInstagram = (ImageView) findViewById(R.id.act_details_IV_instagram);
        imgBtnInstagram.setOnClickListener(this);

        llOfferList = (RelativeLayout) findViewById(R.id.activity_marchnat_ll_offerlist);
        llOfferList.setOnClickListener(this);

        llGalleryView = (RelativeLayout) findViewById(R.id.activity_marchnat_ll_galleryview);
        llGalleryView.setOnClickListener(this);

//        tvOpeningDays = (TextView) findViewById(R.id.act_details_tv_opne_days);
//        tvOpeningDays.setText("" + nearByModel.getOpening_days());

        tvEmail = (TextView) findViewById(R.id.act_details_TV_email);
        tvEmail.setText("" + nearByModel.getEmail());

        tvDescription = (TextView) findViewById(R.id.viewpager_tv_description);

        if (nearByModel.getOfferImageModels().size() > 0) {
            llOfferList.setVisibility(View.VISIBLE);
            viewPagerCompaign = (ViewPager) findViewById(R.id.marchang_offer_pager);
            setPager(0);
            viewPagerCompaign.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Log.d(TAG, "::::::Position " + position);
                    setPager(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, nearByModel.getOfferImageModels(), this, "offer");
            viewPagerCompaign.setAdapter(viewPagerAdapter);
            viewPagerAdapter.notifyDataSetChanged();
        } else {
            llOfferList.setVisibility(View.GONE);
        }
        if (nearByModel.getGalleyImage().size() > 0) {
            viewPagerGalleryView = (ViewPager) findViewById(R.id.marchang_gallery_view);
            ViewPagerAdapter viewPagerAdapter1 = new ViewPagerAdapter(this, nearByModel.getGalleyImage(), this, "gallery");
            viewPagerGalleryView.setAdapter(viewPagerAdapter1);
            viewPagerAdapter1.notifyDataSetChanged();
        } else {
            llGalleryView.setVisibility(View.GONE);
        }

        if (nearByModel.getFacebook() == null || nearByModel.getFacebook().length() < 10) {
            imgBtnFacebook.setVisibility(View.GONE);
        }
        if (nearByModel.getTwitter() == null || nearByModel.getTwitter().length() < 10) {
            imgBtnTwitter.setVisibility(View.GONE);
        }
        if (nearByModel.getInstagram() == null || nearByModel.getInstagram().length() < 10) {
            imgBtnInstagram.setVisibility(View.GONE);
        }
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        if (nearByModel.getOpening_days().contains(day)) {
            tvWorkingDays.setText("Open Today");
        } else {
            tvWorkingDays.setText("Closed Today");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_details_TV_title_back:
                onBackPressed();
                break;
            case R.id.act_details_TV_website:
                openInBrowser(nearByModel.getWebsite());
                break;
            case R.id.act_details_IV_facebook:
                openInBrowser(nearByModel.getFacebook());
                break;
            case R.id.act_details_IV_twitter:
                openInBrowser(nearByModel.getTwitter());
                break;
            case R.id.act_details_IV_instagram:
                openInBrowser(nearByModel.getInstagram());
                break;
            case R.id.activity_marchnat_ll_offerlist:
//                Other.sendToThisActivity(this, ImageActivity.class, new String[]{"position;" + position});
                break;
            case R.id.marchant_ll_about:
                //showMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getAddress());
                break;
            case R.id.act_details_TV_address:
                Other.viewMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getBusiness_name(),
                        MerchantDetailsActivity.this);
                break;
            case R.id.act_details_TV_mobile:
                Other.callNow(MerchantDetailsActivity.this, "" + nearByModel.getPhone());
                break;
        }
    }

    private void openInBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void setPager(int position) {
        ImageModel imageModel = nearByModel.getOfferImageModels().get(position);
        if (imageModel.getOffer_details().trim().length() > 1) {
            tvDescription.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDescription.setText(Html.fromHtml(imageModel.getOffer_details(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvDescription.setText(Html.fromHtml(imageModel.getOffer_details()));
            }
        } else {
            tvDescription.setVisibility(View.GONE);
        }
    }

    private void addRemoveWishList(String out_id, String condition) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&out_id=").append("" + out_id);
        stringBuilder.append("&status=").append("" + condition);
        String content = stringBuilder.toString();
        Log.d(TAG, "::::Wish List " + content);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.ADDREMOVE_WISHLIST_URL, 0, content, false, "Loading ...", new GetWebServiceData() {
            @Override
            public void getWebServiceResponse(String responseData, int serviceCounter) {
                try {
                    Log.d(TAG, "::::Wish List Response Data " + responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    nearByModel.setWishlist(status);
                    if (nearByModel.getWishlist().equals("1")) {
                        btnWishList.setText("Remove From Wishlist");
                    } else {
                        btnWishList.setText("Add To Wishlist");
                    }
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                }
            }
        });
        getDataUsingWService.execute();
    }

    @Override
    public void viewPageImageClick(String page, int position) {
        strPagerType = page;
        this.pagerPosition = position;
        ImageViewFragment imageViewFragment = new ImageViewFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().add(android.R.id.content, imageViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
