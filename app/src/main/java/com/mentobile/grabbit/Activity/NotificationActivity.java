package com.mentobile.grabbit.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mentobile.grabbit.Adapter.RecyclerAdapter;
import com.mentobile.grabbit.Database.Database;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.Notification;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/22/2016.
 */

public class NotificationActivity extends BaseActivity implements RecyclerAdapter.ReturnView, RecyclerAdapter.onItemClickListener {

    private RecyclerView notificatio_recyle_list;
    private List<Notification> notificationlist = new ArrayList<Notification>();
    private RecyclerAdapter recyclerAdapter;
    private LinearLayout linearLayout;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_notification;
    }

    @Override
    public void initialize() {
        notificatio_recyle_list = (RecyclerView) findViewById(R.id.notificatio_recyle_list);
        linearLayout = (LinearLayout) findViewById(R.id.liner_layout_notification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int i = 0;
        Cursor cursor = GrabbitApplication.database.getTableData(Database.TBL_NOTIFICATION);
        if (cursor.moveToFirst()) {
            do {
                i++;
                String text = cursor.getString(1);
                String title = cursor.getString(2);
                String view_status = cursor.getString(3);
                String notify_dt = cursor.getString(4);
                Notification notification = new Notification("" + i, title, text, view_status, notify_dt);
                notificationlist.add(notification);
            }
            while (cursor.moveToNext());
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
        cursor.close();
        notificatio_recyle_list.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(notificationlist, this, R.layout.item_notification, this, 0, this);
        notificatio_recyle_list.setAdapter(recyclerAdapter);
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {
        Notification notification = notificationlist.get(position);
        TextView title = (TextView) view.findViewById(R.id.notification_title);
        title.setText(notification.getTitle());
        TextView message = (TextView) view.findViewById(R.id.notification_message);
        message.setText(notification.getDescription());
        TextView date_time = (TextView) view.findViewById(R.id.notification_dt);
        date_time.setText(notification.getDate());
    }

    @Override
    public void getItemPosition(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_all:
                GrabbitApplication.database.truncateTable(Database.TBL_NOTIFICATION);
                notificationlist.clear();
                recyclerAdapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
