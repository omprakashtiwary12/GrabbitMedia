package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mentobile.grabbit.Adapter.CustomExpandableListAdapter;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/22/2016.
 */
public class HelpActivity extends BaseActivity implements GetWebServiceData {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    static List<String> expandableListTitle = new ArrayList<String>();
    static List<String> expandableListDetail = new ArrayList<String>();

    @Override
    public int getActivityLayout() {
        return R.layout.activity_help;
    }

    @Override
    public void initialize() {
        setTitle("Help");
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.QUESTION_URL, 0, "", true, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void init(Bundle save) {

    }


    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        try {
            expandableListTitle.clear();
            expandableListDetail.clear();
            Log.w("responsedata", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    String q_id = jsonObject.getString("q_id");
                    String q_name = jsonObject.getString("q_name");
                    String q_ans = jsonObject.getString("q_ans");
                    expandableListTitle.add(q_name);
                    expandableListDetail.add(q_ans);
                }
            }

            expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);
        } catch (Exception e) {

        }
    }
}
