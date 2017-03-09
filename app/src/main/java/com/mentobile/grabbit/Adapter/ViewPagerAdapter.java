package com.mentobile.grabbit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mentobile.grabbit.Model.ImageModel;
import com.mentobile.grabbit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Sharma on 2/19/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private final Activity activity;
    private List<ImageModel> arrayList = new ArrayList<>();

    public ViewPagerAdapter(Activity activity, List<ImageModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
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
        ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_image);
        String name = arrayList.get(position).getName();
        Picasso.with(activity.getApplicationContext()).load(name).fit().placeholder(R.drawable.placeholder_banner).into(imageView);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
