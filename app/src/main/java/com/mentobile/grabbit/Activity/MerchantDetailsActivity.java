package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mentobile.grabbit.Adapter.AdapterForGallery;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.Other;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/28/2016.
 */
public class MerchantDetailsActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ImageView act_details_IV_bg;
    ImageView act_details_IV_bg1;
    TextView act_details_TV_title_name;
    TextView act_details_TV_title_address;
    ImageView act_details_TV_title_back;
    TextView act_details_TV_address;
    TextView act_details_TV_mobile;
    TextView act_details_TV_workingHours;
    TextView act_details_TV_website;
    ImageView act_details_IV_facebook;
    ImageView act_details_IV_twitter;
    ImageView act_details_IV_instagram;
    LinearLayout marchant_ll_about;
    LinearLayout act_details_ll_address;
    LinearLayout act_details_ll_phone;
    LinearLayout act_details_ll_website;
    LinearLayout activity_marchnat_ll_offerlist;
    NearByModel nearByModel;

    int position = 0;
    ImageView img_gallery1, img_gallery2, img_gallery3;
    ViewPager mViewPager;
    List<String> profilePhotos = new ArrayList<String>();
    public static Bitmap bitmap;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_merchant_details;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        act_details_TV_title_back = (ImageView) findViewById(R.id.act_details_TV_title_back);
        act_details_IV_bg = (ImageView) findViewById(R.id.act_details_IV_bg);
        act_details_IV_bg1 = (ImageView) findViewById(R.id.act_details_IV_bg1);
        act_details_IV_bg1.setAlpha(0.8f);
        act_details_TV_title_name = (TextView) findViewById(R.id.act_details_TV_title_name);
        act_details_TV_title_address = (TextView) findViewById(R.id.act_details_TV_title_address);
        act_details_TV_address = (TextView) findViewById(R.id.act_details_TV_address);
        act_details_TV_mobile = (TextView) findViewById(R.id.act_details_TV_mobile);
        act_details_TV_workingHours = (TextView) findViewById(R.id.act_details_TV_workingHours);
        act_details_TV_website = (TextView) findViewById(R.id.act_details_TV_website);
        act_details_IV_facebook = (ImageView) findViewById(R.id.act_details_IV_facebook);
        act_details_IV_twitter = (ImageView) findViewById(R.id.act_details_IV_twitter);
        act_details_IV_instagram = (ImageView) findViewById(R.id.act_details_IV_instagram);
        marchant_ll_about = (LinearLayout) findViewById(R.id.marchant_ll_about);
        act_details_ll_address = (LinearLayout) findViewById(R.id.act_details_ll_address);
        act_details_ll_phone = (LinearLayout) findViewById(R.id.act_details_ll_phone);
        act_details_ll_website = (LinearLayout) findViewById(R.id.act_details_ll_website);
        activity_marchnat_ll_offerlist = (LinearLayout) findViewById(R.id.activity_marchnat_ll_offerlist);
        mViewPager = (ViewPager) findViewById(R.id.marchang_offer_pager);

        img_gallery1 = (ImageView) findViewById(R.id.img_gallery1);
        img_gallery2 = (ImageView) findViewById(R.id.img_gallery2);
        img_gallery3 = (ImageView) findViewById(R.id.img_gallery3);


        act_details_TV_title_back.setOnClickListener(this);
        act_details_TV_website.setOnClickListener(this);
        act_details_IV_instagram.setOnClickListener(this);
        act_details_IV_twitter.setOnClickListener(this);
        act_details_IV_facebook.setOnClickListener(this);
        marchant_ll_about.setOnClickListener(this);
        act_details_ll_address.setOnClickListener(this);
        act_details_ll_phone.setOnClickListener(this);
        act_details_ll_website.setOnClickListener(this);
        activity_marchnat_ll_offerlist.setOnClickListener(this);
        // mViewPager.setOnClickListener(this);


        img_gallery1.setOnClickListener(this);
        img_gallery2.setOnClickListener(this);
        img_gallery3.setOnClickListener(this);


        img_gallery1.setDrawingCacheEnabled(true);
        img_gallery2.setDrawingCacheEnabled(true);
        img_gallery3.setDrawingCacheEnabled(true);
    }

    @Override
    public void init(Bundle save) {
        save = getIntent().getExtras();
        if (save != null) {
            position = Integer.parseInt(save.getString("position"));
            nearByModel = GrabbitApplication.nearByModelList.get(position);
            act_details_TV_title_name.setText(nearByModel.getBusiness_name());
            act_details_TV_title_address.setText(nearByModel.getAddress() + " " + nearByModel.getCity_name() + " " + nearByModel.getState_name() + " " + nearByModel.getPincode());
            act_details_TV_address.setText(nearByModel.getAddress() + " " + nearByModel.getCity_name() + " " + nearByModel.getState_name() + " " + nearByModel.getPincode());
            act_details_TV_mobile.setText(nearByModel.getPhone());
            act_details_TV_workingHours.setText(nearByModel.getOpen_time() + "" + nearByModel.getClose_time());
            act_details_TV_website.setText(nearByModel.getWebsite());
            act_details_TV_title_address.setSelected(true);
            profilePhotos.clear();
            for (int i = 0; i < nearByModel.getOfferImageModels().size(); i++) {
                profilePhotos.add("http://grabbit.co.in/merchant/uploads/design/" + nearByModel.getM_id() + "/" + nearByModel.getOfferImageModels().get(i).getImageName());
            }
            AdapterForGallery adapterViewPager = new AdapterForGallery(this, profilePhotos);
            mViewPager.setAdapter(adapterViewPager);
            mViewPager.setOnPageChangeListener(this);
            nearByModel.getOfferImageModels().get(0).getImageName();
            Picasso.with(this).load("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/banner.png").into(act_details_IV_bg);
            Picasso.with(this).load("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/gallery1.png").fit().into(img_gallery1);
            Picasso.with(this).load("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/gallery2.png").fit().into(img_gallery2);
            Picasso.with(this).load("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/gallery3.png").fit().into(img_gallery3);


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
                Other.sendToThisActivity(this, ImageActivity.class, new String[]{"position;" + position});
                break;
            case R.id.img_gallery1:
                Other.sendToThisActivity(this, GalleryActivity.class, new String[]{"position;" + position});
                break;
            case R.id.img_gallery2:
                Other.sendToThisActivity(this, GalleryActivity.class, new String[]{"position;" + position});
                break;
            case R.id.img_gallery3:
                Other.sendToThisActivity(this, GalleryActivity.class, new String[]{"position;" + position});
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
}
