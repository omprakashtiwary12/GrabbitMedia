package com.grabbit.daily_deals.Adapter;

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
    public onItemClickListener itemClickListener;

    public interface ReturnView {
        void getAdapterView(View view, List objects, int position, int from);
    }
    public interface onItemClickListener {
        void getItemPosition(int position);
    }

    public RecyclerAdapter(List rideHistoryModelList, Context context, int layout, ReturnView returnView, int from,onItemClickListener itemClickListener) {
        this.rideHistoryModelList = rideHistoryModelList;
        this.context = context;
        this.layout = layout;
        this.returnView = returnView;
        this.from = from;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return rideHistoryModelList.size();
    }

    @Override
    public void onBindViewHolder(ProfileHolder rideHistoryViewHolder, final int position) {
        returnView.getAdapterView(rideHistoryViewHolder.v, rideHistoryModelList, position, from);
        rideHistoryViewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.getItemPosition(position);
            }
        });
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