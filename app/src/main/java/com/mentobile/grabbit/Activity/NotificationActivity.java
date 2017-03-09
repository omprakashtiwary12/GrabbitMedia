package com.mentobile.grabbit.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.mentobile.grabbit.Adapter.RecyclerAdapter;
import com.mentobile.grabbit.Database.NotificationDatabase;
import com.mentobile.grabbit.Model.Notification;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 12/22/2016.
 */

public class NotificationActivity extends BaseActivity implements RecyclerAdapter.ReturnView {

    RecyclerView notificatio_recyle_list;
    public static List<Notification> notificationlist = new ArrayList<Notification>();


    @Override
    public int getActivityLayout() {
        return R.layout.activity_notification;
    }

    @Override
    public void initialize() {
        notificatio_recyle_list = (RecyclerView) findViewById(R.id.notificatio_recyle_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationlist.clear();
        int i = 0;
        NotificationDatabase notificationDatabase = new NotificationDatabase(this);
        notificationDatabase.open();
        Cursor cursor = notificationDatabase.getData();
        if (cursor.moveToFirst()) {
            do {
                i++;
                String text = cursor.getString(1);
                String title = cursor.getString(2);
                Notification notification = new Notification(i + "", title, text, "");
                notificationlist.add(notification);
                Log.w("mydata", notification.getDescription() + "--" + notification.getTitle());
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        notificationDatabase.close();
        notificatio_recyle_list.setLayoutManager(linearLayoutManager);
        //notificatio_recyle_list.setAdapter(new RecyclerAdapter(notificationlist, this, R.layout.item_notification, this, 0));
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {
        //  ImageView  list_notification_iv_description = (ImageView) view.findViewById(R.id.list_notification_iv_description);
        // list_notification_iv_description.setImageDrawable(getResources().getDrawable(R.drawable.events));
        TextView messgae = (TextView) view.findViewById(R.id.notification_message);
        messgae.setText(notificationlist.get(position).getDescription() + "\n" + notificationlist.get(position).getTitle());
    }
}
