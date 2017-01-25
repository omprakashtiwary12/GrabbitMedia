package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mentobile.grabbit.Adapter.AdapterViewPager;
import com.mentobile.grabbit.Adapter.RecyclerAdapter;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.CircleImageView1;
import com.mentobile.grabbit.Utility.Other;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/21/2016.
 */

public class SearchActivity extends BaseActivity implements RecyclerAdapter.ReturnView {

    EditText search_activity_et_search;
    RecyclerView search_activity_rv;
    public static List<NearByModel> nearByModelList11 = new ArrayList<NearByModel>();

    @Override
    public int getActivityLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initialize() {
        search_activity_et_search = (EditText) findViewById(R.id.search_activity_et_search);
        search_activity_rv = (RecyclerView) findViewById(R.id.search_activity_rv);
        nearByModelList11.clear();
        search_activity_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchList(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void init(Bundle save) {

    }

    private void searchList(CharSequence charSequence) {
        String search_data = search_activity_et_search.getText().toString();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (search_data.length() >= 3) {
            for (int i = 0; i < GrabbitApplication.nearByModelList.size(); i++) {
                if (GrabbitApplication.nearByModelList.get(i).getAddress().contains(search_data)) {
                    nearByModelList11.add(GrabbitApplication.nearByModelList.get(i));
                }
            }
            search_activity_rv.setLayoutManager(linearLayoutManager);
            search_activity_rv.setAdapter(new RecyclerAdapter(nearByModelList11, this, R.layout.item_nearby_adapter, this, 0));
        }
    }

    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        ViewPager nearyby_item_IMG_place = (ViewPager) view.findViewById(R.id.nearby_item_IMG_place);
        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);
        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        final LinearLayout viewPagerCountDots = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        RelativeLayout nearby_item_RL = (RelativeLayout) view.findViewById(R.id.nearby_item_RL);
        CircleImageView1 nearby_item_IMG_logo = (CircleImageView1) view.findViewById(R.id.nearby_item_IMG_logo);
        ImageView nearby_item_IMG_star = (ImageView) view.findViewById(R.id.nearby_item_IMG_star);
        nearby_item_IMG_star.setVisibility(View.INVISIBLE);
        final NearByModel nearByModel = GrabbitApplication.nearByModelList.get(position);
        int wishlist = Integer.parseInt(nearByModel.getWishlist());
        final List<String> profilePhotos = new ArrayList<String>();
        profilePhotos.add("camera");
        profilePhotos.add("gallery");
        profilePhotos.add("camera");
        profilePhotos.add("gallery");
        nearby_item_IMG_logo.setImageResource(R.drawable.hotel);
        nearyby_item_IMG_place.setAdapter(new AdapterViewPager(this, profilePhotos, position, this));

        nearyby_item_IMG_place.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setUiPageViewController(profilePhotos.size(), viewPagerCountDots, position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String name[] = nearByModel.getAddress().split(",");
        nearby_item_TXT_name.setText(name[0]);
        if (nearByModel.getDistance() != null) {
            String distance[] = nearByModel.getDistance().trim().split(" ");
            Double distance_in_km = Double.parseDouble(distance[0]) * 1.60934;
            nearby_item_TXT_distace.setText(new DecimalFormat("##.##").format(distance_in_km) + "KM");
        }
        nearby_item_TXT_address.setText(nearByModel.getAddress());
        nearby_item_TXT_distace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getBusiness_name());
            }
        });

        setUiPageViewController(profilePhotos.size(), viewPagerCountDots);


        nearby_item_RL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Other.sendToThisActivity(SearchActivity.this, MerchantDetailsActivity.class, new String[]{"position;" + position});
            }
        });

    }

    public void showMap(String lat, String lng, String name) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + name + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        if (mapIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }


    private void setUiPageViewController(int dotscount, LinearLayout viewPagerCountDots) {
        viewPagerCountDots.removeAllViews();
        ImageView[] dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);


            viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    private void setUiPageViewController(int dotscount, LinearLayout viewPagerCountDots, int position) {
        viewPagerCountDots.removeAllViews();
        ImageView[] dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            viewPagerCountDots.addView(dots[i], params);
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }
}
