package com.grabbit.daily_deals.Activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.Model.Notification;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends BaseActivity implements RecyclerAdapter.ReturnView, RecyclerAdapter.onItemClickListener, GetWebServiceData {

    private RecyclerView notificatio_recyle_list;
    private List<Notification> notificationlist = new ArrayList<Notification>();
    private RecyclerAdapter recyclerAdapter;
    private LinearLayout linearLayout;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_offers;
    }

    @Override
    public void initialize() {

        notificatio_recyle_list = (RecyclerView) findViewById(R.id.notificatio_recyle_list);
        linearLayout = (LinearLayout) findViewById(R.id.liner_layout_notification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificatio_recyle_list.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(notificationlist, this, R.layout.item_offers, this, 0, this);
        notificatio_recyle_list.setAdapter(recyclerAdapter);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.ALL_GRABBIT_OFFERS, 0, content, true, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {
        Notification notification = notificationlist.get(position);
        TextView message = (TextView) view.findViewById(R.id.notification_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message.setText(Html.fromHtml(notification.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            message.setText(Html.fromHtml(notification.getDescription()));
        }
    }

    @Override
    public void getItemPosition(int position) {

    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        if (responseData != null) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    JSONArray jsonArrayOffers = jsonObject.getJSONObject("details").getJSONArray("all_offers");
                    for (int i = 0; i < jsonArrayOffers.length(); i++) {
                        JSONObject objectOffers = jsonArrayOffers.getJSONObject(i);
                        String id = objectOffers.getString("id");
                        String title = objectOffers.getString("title");
                        String description = objectOffers.getString("details");
                        String status1 = objectOffers.getString("status");
                        String date_cr = objectOffers.getString("date_cr");
                        Notification notification = new Notification(id, title, description, status1, date_cr);
                        notificationlist.add(notification);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    notificatio_recyle_list.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        }
    }
}
