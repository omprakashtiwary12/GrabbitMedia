package com.mentobile.grabbit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mentobile.grabbit.Activity.DrawerActivity;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.Other;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final int[] mThumbIds;
    ImageView imageView;
    int temp;

    public ImageAdapter(Context context, int[] mThumbIds) {
        this.context = context;
        this.mThumbIds = mThumbIds;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.gridview_item, null);
            imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);
            imageView.setImageResource(mThumbIds[position]);
            temp = position;
        } else {
            gridView = (View) convertView;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "myPostioton" + temp, Toast.LENGTH_SHORT).show();
                Other.sendToThisActivity(context, DrawerActivity.class);
            }
        });

        return gridView;

    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}