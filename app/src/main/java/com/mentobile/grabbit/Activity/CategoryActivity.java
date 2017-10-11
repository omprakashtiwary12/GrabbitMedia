package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;
import com.mentobile.grabbit.Adapter.ImageAdapter;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.Other;

/**
 * Created by Gokul on 12/6/2016.
 */

public class CategoryActivity extends BaseActivity implements ImageAdapter.iButtonCallBack {

    public int[] mThumbIds = {
            R.drawable.hospitality, R.drawable.retail,
            R.drawable.event, R.drawable.travel, R.drawable.saloon_spa, R.drawable.electronics
    };

    private boolean isCloseApp;
    private int flag;

    @Override
    public void onBackPressed() {
        if (flag == 100) {
            finish();
        } else {
            if (isCloseApp) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press again to close Application.", Toast.LENGTH_SHORT).show();
                isCloseApp = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCloseApp = false;
    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_category;
    }

    @Override
    public void initialize() {
        setTitle("Category");
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(this, mThumbIds, this));
        flag = getIntent().getFlags();
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void getClick(int position) {
        if (flag == 100) {
            Intent intent1 = new Intent();
            intent1.putExtra("pos", (position + 1));
            setResult(100, intent1);
            onBackPressed();
        } else {
            DrawerActivity.cat_id = (position + 1);
            Other.sendToThisActivity(CategoryActivity.this, DrawerActivity.class);
        }
        Log.d("Category Activity ", ":::::::Clear Activity " + flag);
    }
}
