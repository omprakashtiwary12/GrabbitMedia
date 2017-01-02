package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Gokul on 11/28/2016.
 */
public class MerchantDetailsActivity extends BaseActivity implements View.OnClickListener {

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
    NearByModel nearByModel;

    int position = 0;

    ImageView img_offers1, img_offers2, img_offers3;
    ImageView img_gallery1, img_gallery2, img_gallery3;

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
        act_details_IV_bg1.setAlpha(0.7f);
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
        img_offers1 = (ImageView) findViewById(R.id.img_offers1);
        img_offers2 = (ImageView) findViewById(R.id.img_offers2);
        img_offers3 = (ImageView) findViewById(R.id.img_offers3);

        img_gallery1 = (ImageView) findViewById(R.id.img_gallery1);
        img_gallery2 = (ImageView) findViewById(R.id.img_gallery2);
        img_gallery3 = (ImageView) findViewById(R.id.img_gallery3);

        act_details_TV_title_back.setOnClickListener(this);
        act_details_TV_website.setOnClickListener(this);
        act_details_IV_instagram.setOnClickListener(this);
        act_details_IV_twitter.setOnClickListener(this);
        act_details_IV_facebook.setOnClickListener(this);
        marchant_ll_about.setOnClickListener(this);

        img_offers1.setOnClickListener(this);
        img_offers2.setOnClickListener(this);
        img_offers3.setOnClickListener(this);

        img_gallery1.setOnClickListener(this);
        img_gallery2.setOnClickListener(this);
        img_gallery3.setOnClickListener(this);


        img_offers1.setDrawingCacheEnabled(true);
        img_offers2.setDrawingCacheEnabled(true);
        img_offers3.setDrawingCacheEnabled(true);

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
            act_details_TV_title_address.setText(nearByModel.getAddress());
            act_details_TV_address.setText(nearByModel.getAddress());
            act_details_TV_mobile.setText(nearByModel.getPhone());
            act_details_TV_workingHours.setText(nearByModel.getOpen_time());
            act_details_TV_website.setText(nearByModel.getWebsite());
            act_details_TV_title_address.setSelected(true);
            Picasso.with(this).load("http://grabbit.co.in/web_services/merchant_pics/" + nearByModel.getM_id() + ".jpg").into(act_details_IV_bg);

        }
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
            case R.id.img_offers1:
                bitmap = img_offers1.getDrawingCache();
                sendToThisActivity(ImageActivity.class);
                break;
            case R.id.img_offers2:
                bitmap = img_offers2.getDrawingCache();
                sendToThisActivity(ImageActivity.class);
                break;
            case R.id.img_offers3:
                bitmap = img_offers3.getDrawingCache();
                sendToThisActivity(ImageActivity.class);
                break;
            case R.id.img_gallery1:
                bitmap = img_gallery1.getDrawingCache();
                //sendToThisActivity(ImageActivity.class);
                sendToThisActivity(GalleryActivity.class);
                break;
            case R.id.img_gallery2:
                bitmap = img_gallery2.getDrawingCache();
                sendToThisActivity(ImageActivity.class);
                break;
            case R.id.img_gallery3:
                bitmap = img_gallery3.getDrawingCache();
                sendToThisActivity(ImageActivity.class);
                break;
            case R.id.marchant_ll_about:
                showMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getAddress());
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
