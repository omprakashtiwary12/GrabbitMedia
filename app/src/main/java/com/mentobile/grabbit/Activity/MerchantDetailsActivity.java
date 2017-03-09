package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mentobile.grabbit.Adapter.ViewPagerAdapter;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/28/2016.
 */

public class MerchantDetailsActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private String TAG = "MerchantDetailsActivity";
    private ImageView frameLayout;
    private TextView tvTitle;
    private TextView tvAddress;
    private ImageView imgBtn_Back;
    private TextView tvAboutAddress;
    private TextView tvMobile;
    private TextView tvWorkingHours;
    private TextView tvWebsiteName;
    private ImageView imgBtnFacebook;
    private ImageView imgBtnTwitter;
    private ImageView imgBtnInstagram;

    private LinearLayout llAbout;
    private LinearLayout llAddress;
    private LinearLayout llPhone;
    private LinearLayout llWebsite;
    private LinearLayout llOfferList;
    private NearByModel nearByModel;

    private Button btnWishList;

    int position = 0;
    ViewPager viewPagerCompaign;
    ViewPager viewPagerGalleryView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details);
        getSupportActionBar().hide();

        int pos = getIntent().getFlags();
        nearByModel = SplashActivity.nearByModelList.get(pos);

        frameLayout = (ImageView) findViewById(R.id.frameLayout);
        Picasso.with(getApplicationContext()).load(AppUrl.GET_IMAGE + nearByModel.getM_id() + "/" + nearByModel.getBanner())
                .placeholder(R.drawable.placeholder_banner).fit().into(frameLayout);

        imgBtn_Back = (ImageView) findViewById(R.id.act_details_TV_title_back);
        imgBtn_Back.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.act_details_TV_title_name);
        tvTitle.setText(nearByModel.getBusiness_name());

        tvAddress = (TextView) findViewById(R.id.act_details_TV_title_address);
        tvAddress.setText(nearByModel.getFullAddress());
        tvAddress.setSelected(true);

        tvAboutAddress = (TextView) findViewById(R.id.act_details_TV_address);
        tvAboutAddress.setText(nearByModel.getFullAddress());

        tvMobile = (TextView) findViewById(R.id.act_details_TV_mobile);
        tvMobile.setText(nearByModel.getPhone());

        tvWorkingHours = (TextView) findViewById(R.id.act_details_TV_workingHours);
        tvWorkingHours.setText(nearByModel.getOpen_time() + " To " + nearByModel.getClose_time());

        tvWebsiteName = (TextView) findViewById(R.id.act_details_TV_website);
        tvWebsiteName.setText(nearByModel.getWebsite());
        tvWebsiteName.setOnClickListener(this);

        imgBtnFacebook = (ImageView) findViewById(R.id.act_details_IV_facebook);
        imgBtnFacebook.setOnClickListener(this);

        imgBtnTwitter = (ImageView) findViewById(R.id.act_details_IV_twitter);
        imgBtnTwitter.setOnClickListener(this);

        imgBtnInstagram = (ImageView) findViewById(R.id.act_details_IV_instagram);
        imgBtnInstagram.setOnClickListener(this);

        llAbout = (LinearLayout) findViewById(R.id.marchant_ll_about);
        llAbout.setOnClickListener(this);

        llAddress = (LinearLayout) findViewById(R.id.act_details_ll_address);
        llAddress.setOnClickListener(this);

        llPhone = (LinearLayout) findViewById(R.id.act_details_ll_phone);
        llPhone.setOnClickListener(this);

        llWebsite = (LinearLayout) findViewById(R.id.act_details_ll_website);
        llWebsite.setOnClickListener(this);

        llOfferList = (LinearLayout) findViewById(R.id.activity_marchnat_ll_offerlist);
        llOfferList.setOnClickListener(this);

        viewPagerCompaign = (ViewPager) findViewById(R.id.marchang_offer_pager);
        viewPagerCompaign.setOnClickListener(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, nearByModel.getOfferImageModels());
        viewPagerCompaign.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        viewPagerGalleryView = (ViewPager) findViewById(R.id.marchang_gallery_view);
        viewPagerGalleryView.setOnClickListener(this);
        ViewPagerAdapter viewPagerAdapter1 = new ViewPagerAdapter(this, nearByModel.getGalleyImage());
        viewPagerGalleryView.setAdapter(viewPagerAdapter1);
        viewPagerAdapter.notifyDataSetChanged();

        btnWishList = (Button) findViewById(R.id.merchant_detail_wishlist);
        btnWishList.setOnClickListener(this);
        if (nearByModel.getWishlist().equals("1")) {
            btnWishList.setText("Remove From Wishlist");
        } else {
            btnWishList.setText("Add To Wishlist");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_details_TV_title_back:
                finish();
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
                //Other.sendToThisActivity(this, ImageActivity.class, new String[]{"position;" + position});
                break;
            case R.id.marchant_ll_about:
                //showMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getAddress());
                break;
            case R.id.act_details_ll_address:
                showMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getAddress());
                break;
            case R.id.act_details_ll_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + nearByModel.getPhone()));
                startActivity(intent);
                break;
            case R.id.act_details_ll_website:
                String url = nearByModel.getWebsite();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.merchant_detail_wishlist:
                String out_id = nearByModel.getOut_id();
                String wishlist = nearByModel.getWishlist();
                Log.d(TAG,"::::Name "+nearByModel.getBusiness_name());
                addRemoveWishList(out_id, wishlist);
                break;
        }
    }

    public void showMap(String lat, String lng, String name) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + name + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void openInBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void addRemoveWishList(String out_id, String condition) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&out_id=").append(""+out_id);
        stringBuilder.append("&status=").append(""+condition);
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
}
