package com.mentobile.grabbit.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProfileHolder> {

    private List rideHistoryModelList;
    private Context context;
    private int layout;
    ReturnView returnView;
    int from;

    public interface ReturnView {
        void getAdapterView(View view, List objects, int position, int from);
    }


    public RecyclerAdapter(List rideHistoryModelList, Context context, int layout, ReturnView returnView, int from) {
        this.rideHistoryModelList = rideHistoryModelList;
        this.context = context;
        this.layout = layout;
        this.returnView = returnView;
        this.from = from;
    }

    @Override
    public int getItemCount() {
        return rideHistoryModelList.size();
    }

    @Override
    public void onBindViewHolder(ProfileHolder rideHistoryViewHolder, final int i) {
        returnView.getAdapterView(rideHistoryViewHolder.v, rideHistoryModelList, i, from);
    }

    @Override
    public ProfileHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(layout, viewGroup, false);

        return new ProfileHolder(itemView);
    }

    public static class ProfileHolder extends RecyclerView.ViewHolder {
        View v;
        public ProfileHolder(View v) {
            super(v);
            this.v = v;

        }
    }
}