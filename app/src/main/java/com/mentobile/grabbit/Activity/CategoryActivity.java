package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mentobile.grabbit.Adapter.ImageAdapter;
import com.mentobile.grabbit.Adapter.OneAdapter;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 12/6/2016.
 */
public class CategoryActivity extends BaseActivity implements OneAdapter.ReturnView {

    public int[] mThumbIds = {
            R.drawable.hospitality, R.drawable.retail,
            R.drawable.event1, R.drawable.travel,
    };

    @Override
    public int getActivityLayout() {
        return R.layout.activity_category;
    }

    @Override
    public void initialize() {
        setTitle("Category");

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        // Instance of ImageAdapter Class
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //friendImageId = position;
                toastMessage("myposition" + position);
            }
        });
        gridView.setAdapter(new ImageAdapter(this, mThumbIds));
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void getAdapterView(View view, int position) {

    }
}
