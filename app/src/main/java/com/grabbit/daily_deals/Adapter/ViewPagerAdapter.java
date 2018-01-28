package com.grabbit.daily_deals.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grabbit.daily_deals.Model.ImageModel;
import com.grabbit.daily_deals.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Sharma on 2/19/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private final Activity activity;
    private List<ImageModel> arrayList = new ArrayList<>();
    private pageonClick pageonClick;
    private String pageType;

    public interface pageonClick {
        void viewPageImageClick(String page, int position);
    }

    public ViewPagerAdapter(Activity activity, List<ImageModel> arrayList, pageonClick pageonClick, String pageType) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.pageType = pageType;
        this.pageonClick = pageonClick;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private View view = null;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_view_pager, container, false);

        ImageModel imageModel = arrayList.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_image);
        final String name = imageModel.getName();
        Picasso.with(activity.getApplicationContext()).load(name).fit()
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE).
                .placeholder(R.drawable.placeholder_banner).
                into(imageView);

        TextView tvTitle = (TextView) view.findViewById(R.id.viewpager_tv_title_text);

        if (pageType.equalsIgnoreCase("offer")) {
            tvTitle.setVisibility(View.VISIBLE);
            Log.d("ViewPagerActivity ", imageModel.getOffer_details());
            tvTitle.setText("" + imageModel.getOffer_description());
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        ((ViewPager) container).addView(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageonClick.viewPageImageClick(pageType, position);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
