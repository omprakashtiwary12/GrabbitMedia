package com.grabbit.daily_deals.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grabbit.daily_deals.Activity.MerchantDetailsActivity;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.Other;
import com.grabbit.daily_deals.Utility.PageviewerImageclick;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/1/2015.
 */

public class AdapterViewPager extends PagerAdapter {

    private final Activity activity;
    private List<String> arrayList = new ArrayList<>();
    private int position1;
    Context context;

    public AdapterViewPager(Activity activity, List<String> arrayList, int position, Context context) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.position1 = position;
        this.context = context;
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
        Picasso.with(context).load(arrayList.get(position)).fit().into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.w("messageo full", "my postion is :-" + position1);
                Other.sendToThisActivity(context, MerchantDetailsActivity.class, new String[]{"position;" + position1});
            }
        });

        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
