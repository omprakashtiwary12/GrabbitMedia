package com.grabbit.daily_deals.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Gokul on 8/4/2016.
 */
public class OneAdapter extends ArrayAdapter {
    int resource;
    ReturnView returnView;

    public interface ReturnView {
        void getAdapterView(View view, int position);
    }


    public OneAdapter(Context context, int resource, List objects, ReturnView returnView) {
        super(context, resource, objects);
        this.resource = resource;
        this.returnView = returnView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object user = getItem(position);
        convertView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        returnView.getAdapterView(convertView, position);
        return convertView;
    }

}
