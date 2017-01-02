package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mentobile.grabbit.Adapter.RecyclerAdapter;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;
import static android.support.v7.recyclerview.R.styleable.RecyclerView;

/**
 * Created by Administrator on 12/22/2016.
 */

public class NotificationActivity extends BaseActivity implements RecyclerAdapter.ReturnView {
    RecyclerView notificatio_recyle_list;
    public static List<String> notificationlist = new ArrayList<String>();


    @Override
    public int getActivityLayout() {
        return R.layout.activity_notification;
    }

    @Override
    public void initialize() {
        notificatio_recyle_list = (RecyclerView) findViewById(R.id.notificatio_recyle_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificatio_recyle_list.setLayoutManager(linearLayoutManager);
        notificationlist.clear();
        notificationlist.add("1");
        notificationlist.add("2");
        notificatio_recyle_list.setAdapter(new RecyclerAdapter(notificationlist, this, R.layout.item_notification, this, 0));
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {
       ImageView  list_notification_iv_description = (ImageView) view.findViewById(R.id.list_notification_iv_description);
        list_notification_iv_description.setImageDrawable(getResources().getDrawable(R.drawable.events));
    }
}
