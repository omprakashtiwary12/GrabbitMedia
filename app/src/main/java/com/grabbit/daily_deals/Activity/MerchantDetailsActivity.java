package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grabbit.daily_deals.Adapter.ViewPagerAdapter;
import com.grabbit.daily_deals.Fragment.ImageViewFragment;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.Model.ImageModel;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gokul on 11/28/2016.
 */

public class MerchantDetailsActivity extends AppCompatActivity implements View.OnClickListener, ViewPagerAdapter.pageonClick {

    private String TAG = "MerchantDetailsActivity";

    private ImageView frameLayout;
    private TextView tvTitle;
    private TextView tvAddress;
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
    public  static NearByModel nearByModel = new NearByModel();

    private TextView tvDescription;
    private LinearLayout llMapClick;

    private Button btnWishList;

    private ViewPager viewPagerCompaign;
    private ViewPager viewPagerGalleryView;

    public String strPagerType;
    public int pagerPosition;
    private boolean isShowAllDescription;
    private String short_description = "";
    private String full_description = "";
    private int pos_value;

    private String shareString = "";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("" + nearByModel.getBusiness_name());
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

        llMapClick = (LinearLayout) findViewById(R.id.location1);
        llMapClick.setOnClickListener(this);

        tvDescription = (TextView) findViewById(R.id.viewpager_tv_description);
        tvDescription.setOnClickListener(this);

        if (nearByModel.getOfferImageModels().size() > 0) {
            LinearLayout sliderDotspanel = (LinearLayout) findViewById(R.id.slider_dots_offer);
            final int dotscount = nearByModel.getOfferImageModels().size();
            final ImageView[] dots = new ImageView[dotscount];
            for (int i = 0; i < dotscount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                if (nearByModel.getOfferImageModels().size() > 1)
                    sliderDotspanel.addView(dots[i], params);
            }
            llOfferList.setVisibility(View.VISIBLE);
            viewPagerCompaign = (ViewPager) findViewById(R.id.marchang_offer_pager);
            viewPagerCompaign.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            setPager(0);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, nearByModel.getOfferImageModels(), this, "offer");
            viewPagerCompaign.setAdapter(viewPagerAdapter);
            viewPagerAdapter.notifyDataSetChanged();
        } else {
            llOfferList.setVisibility(View.GONE);
        }
        if (nearByModel.getGalleyImage().size() > 0) {
            LinearLayout sliderDotspanel1 = (LinearLayout) findViewById(R.id.slider_dots_gallery);
            final int dotscount1 = nearByModel.getGalleyImage().size();
            final ImageView[] dots1 = new ImageView[dotscount1];
            for (int i = 0; i < dotscount1; i++) {
                dots1[i] = new ImageView(this);
                dots1[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                if (nearByModel.getGalleyImage().size() > 1)
                    sliderDotspanel1.addView(dots1[i], params);
            }
            viewPagerGalleryView = (ViewPager) findViewById(R.id.marchang_gallery_view);
            viewPagerGalleryView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int i = 0; i < dotscount1; i++) {
                        dots1[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    }
                    dots1[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
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
        if (nearByModel.getOpening_days().contains(day) && iSOpen(nearByModel.getOpen_time())
                && !iSClose(nearByModel.getClose_time())) {
            tvWorkingDays.setText("Open");
            tvWorkingDays.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            tvWorkingDays.setText("Closed");
            tvWorkingDays.setBackgroundColor(getResources().getColor(R.color.button_selected));
        }
        shareString = "You should try " + nearByModel.getBusiness_name() + " in " + nearByModel.getAddress() + " via \n" + "https://grabbit.co.in/";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareString);
                startActivity(Intent.createChooser(sharingIntent, "Share with"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_merchant_details, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_share);
        return true;
    }

    private int getSeconds(String time) {
        String[] units = time.split(":"); //will break the string up into an array
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        int duration = 60 * minutes + seconds;
        return duration;
    }

    private boolean iSOpen(String openAndClosedTime) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
        int difference = 0;
        String nowTime = outFormat.format(date.getTime());
        try {
            int currentTimeinSeconds = getSeconds(nowTime);
            Date openDate = inFormat.parse(openAndClosedTime);
            String openTime = outFormat.format(openDate);
            int openTimeInSeconds = getSeconds(openTime);
            difference = currentTimeinSeconds - openTimeInSeconds;
            Log.d(TAG, ":::::::Current Time  " + currentTimeinSeconds + " Open Time " + openTimeInSeconds);
            Log.d(TAG, ":::::::Difference  " + difference);
            if (difference > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean iSClose(String openAndClosedTime) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
        int difference = 0;
        String nowTime = outFormat.format(date.getTime());
        try {
            int currentTimeinSeconds = getSeconds(nowTime);
            Date openDate = inFormat.parse(openAndClosedTime);
            String openTime = outFormat.format(openDate);
            int openTimeInSeconds = getSeconds(openTime);
            difference = currentTimeinSeconds - openTimeInSeconds;
            Log.d(TAG, ":::::::Current Time  " + currentTimeinSeconds + " Open Time " + openTimeInSeconds);
            Log.d(TAG, ":::::::Difference  " + difference);
            if (difference > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.location1:
                Other.viewMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getBusiness_name(),
                        MerchantDetailsActivity.this);
                break;
            case R.id.act_details_TV_mobile:
                Other.callNow(MerchantDetailsActivity.this, "" + nearByModel.getPhone());
                break;

            case R.id.viewpager_tv_description:
                if (isShowAllDescription) {
                    isShowAllDescription = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvDescription.setText(Html.fromHtml(short_description, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvDescription.setText(Html.fromHtml(short_description));
                    }
                } else {
                    isShowAllDescription = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvDescription.setText(Html.fromHtml(full_description, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvDescription.setText(Html.fromHtml(full_description));
                    }
                }
                break;
        }
    }

    private void openInBrowser(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(MerchantDetailsActivity.this, "Wrong Url used.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPager(int position) {
        ImageModel imageModel = nearByModel.getOfferImageModels().get(position);
        if (imageModel.getOffer_details().trim().length() > 1) {
            tvDescription.setVisibility(View.VISIBLE);
            full_description = imageModel.getOffer_details();
            try {
                short_description = full_description.substring(0, 200) + "...See More";
            } catch (Exception e) {
                short_description = imageModel.getOffer_details();
            }
            if (full_description.length() > 200) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDescription.setText(Html.fromHtml(short_description, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvDescription.setText(Html.fromHtml(short_description));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDescription.setText(Html.fromHtml(full_description, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvDescription.setText(Html.fromHtml(full_description));
                }
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
//        Log.d(TAG, "::::Wish List " + content);
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
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
