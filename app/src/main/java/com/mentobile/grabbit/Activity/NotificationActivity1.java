package com.mentobile.grabbit.Activity;

/**
 * Created by Administrator on 12/27/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.mentobile.grabbit.Adapter.NotificationAdapter;
import com.mentobile.grabbit.Database.NotificationDatabase;
import com.mentobile.grabbit.Model.Notification;
import com.mentobile.grabbit.R;

import java.util.ArrayList;


public class NotificationActivity1 extends ActionBarActivity implements NotificationAdapter.onItemClickListener {
    private RecyclerView listQuery;
    ArrayList<Notification> arrListQuery = new ArrayList<>();
    NotificationAdapter notificationAdapter;

    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Notification");

        listQuery = (RecyclerView) findViewById(R.id.notificatio_recyle_list);

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
                arrListQuery.add(notification);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        notificationDatabase.update("seen");
        notificationDatabase.close();


        notificationAdapter = new NotificationAdapter(this, R.layout.item_notification, arrListQuery, this);
        listQuery.setLayoutManager(new LinearLayoutManager(this));
        listQuery.setHasFixedSize(true);
        listQuery.setAdapter(notificationAdapter);

        Intent notificationIntent = new Intent(getApplicationContext(), DrawerActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setContentIntent(intent);

        mBuilder.setWhen(100);

        notificationManager.notify(0, mBuilder.build()

        );


    }

    @Override
    public void getItemPosition(int position) {

    }
}

