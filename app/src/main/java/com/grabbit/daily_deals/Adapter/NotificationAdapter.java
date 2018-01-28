package com.grabbit.daily_deals.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grabbit.daily_deals.Model.Notification;
import com.grabbit.daily_deals.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecordHolder> {

    final static String TAG = "RecycleViewAdapter";

    private Context context;
    private ArrayList<Notification> arrayList = new ArrayList();
    private URI uri;
    private final TypedValue mTypedValue = new TypedValue();
    int mBackground;
    private int resourceID;
    public onItemClickListener itemClickListener;

    public interface onItemClickListener {
        void getItemPosition(int position);
    }

    public NotificationAdapter(Context context, int resourceID, ArrayList<Notification> arrayList, onItemClickListener itemClickListener) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.context = context;
        this.resourceID = resourceID;
        this.arrayList = arrayList;
        mBackground = mTypedValue.resourceId;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(resourceID, parent, false);
        itemView.setBackgroundResource(mBackground);
        return new RecordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, final int position) {
        Notification notification = arrayList.get(position);

        holder.tvTitle.setText(notification.getTitle());

        holder.tvDateTime.setText(notification.getDate());


        Picasso.with(context).load("http://karrots.in/admin/" + notification.getDescription()).into(holder.tvDescription);
        if (notification.getDescription().equals("")) {
            holder.tvDescription.setVisibility(View.GONE);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.getItemPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecordHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView tvDescription;
        TextView tvDateTime;

        public final View view;

        public RecordHolder(View gridView) {
            super(gridView);
            view = gridView;
//            tvTitle = (TextView) gridView.findViewById(R.id.list_notification_tv_title);
//            tvDescription = (ImageView) gridView.findViewById(R.id.list_notification_iv_description);
//            tvDateTime = (TextView) gridView.findViewById(R.id.list_notification_tv_date);

        }
    }
}


