package com.mentobile.grabbit.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mentobile.grabbit.Activity.MerchantDetailsActivity;
import com.mentobile.grabbit.Adapter.AdapterViewPager;
import com.mentobile.grabbit.Adapter.RecyclerAdapter;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.CircleImageView;
import com.mentobile.grabbit.Utility.Other;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NearByFragment extends Fragment implements RecyclerAdapter.ReturnView {
    RecyclerView frag_nearby_rv;
    ImageView nearby_item_IMG_star;
    RecyclerAdapter recyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        frag_nearby_rv = (RecyclerView) view.findViewById(R.id.frag_nearby_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(GrabbitApplication.nearByModelList, getActivity(), R.layout.item_nearby_adapter, this, 0);
        frag_nearby_rv.setAdapter(recyclerAdapter);
        return view;
    }


    @Override
    public void getAdapterView(View view, List objects, final int position, int from) {
        ViewPager nearyby_item_IMG_place = (ViewPager) view.findViewById(R.id.nearby_item_IMG_place);
        TextView nearby_item_TXT_name = (TextView) view.findViewById(R.id.nearby_item_TXT_name);
        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.nearby_item_TXT_address);
        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.nearby_item_TXT_distace);
        final LinearLayout viewPagerCountDots = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        RelativeLayout nearby_item_RL = (RelativeLayout) view.findViewById(R.id.nearby_item_RL);
        CircleImageView nearby_item_IMG_logo = (CircleImageView) view.findViewById(R.id.nearby_item_IMG_logo);
        //Start imageview

        Log.w("NearByDataPostion", "" + position);
        nearby_item_IMG_star = (ImageView) view.findViewById(R.id.nearby_item_IMG_star);
//        if (nearByModel.getWishlist() == "1") {
//            nearby_item_IMG_star.setImageDrawable(getResources().getDrawable(R.drawable.like_press));
//        } else {
//            nearby_item_IMG_star.setImageDrawable(getResources().getDrawable(R.drawable.like_unpress));
//        }
        nearby_item_IMG_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,":::::Like Button "+position);
                NearByModel nearByModel = GrabbitApplication.nearByModelList.get(position);
                if (nearByModel.getWishlist().equals("1")) {
                    nearby_item_IMG_star.setImageDrawable(getResources().getDrawable(R.drawable.like_unpress));
                    nearByModel.setWishlist("0");
                } else {
                    nearby_item_IMG_star.setImageDrawable(getResources().getDrawable(R.drawable.like_press));
                    nearByModel.setWishlist("1");
                }
//                Intent intent = new Intent("Broadcast");
//                getActivity().sendBroadcast(intent);
                //recyclerAdapter.notifyDataSetChanged();
               // recyclerAdapter.notifyItemChanged(position);
            }
        });

        final List<String> profilePhotos = new ArrayList<String>();

        profilePhotos.add("camera");
//        profilePhotos.add("gallery");
//        profilePhotos.add("camera");
        //profilePhotos.add("gallery");


        nearby_item_IMG_logo.setImageResource(R.drawable.hotel);
        nearyby_item_IMG_place.setAdapter(new AdapterViewPager(getActivity(), profilePhotos, position, getActivity()));
//
//        nearyby_item_IMG_place.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                setUiPageViewController(profilePhotos.size(), viewPagerCountDots, position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


//        String name[] = nearByModel.getAddress().split(",");
//        nearby_item_TXT_name.setText(name[0]);
//        if (nearByModel.getDistance() != null) {
//            String distance[] = nearByModel.getDistance().trim().split(" ");
//            Double distance_in_km = Double.parseDouble(distance[0]) * 1.60934;
//            nearby_item_TXT_distace.setText(new DecimalFormat("##.##").format(distance_in_km) + "KM");
//        }
//        nearby_item_TXT_address.setText(nearByModel.getAddress());
//        nearby_item_TXT_distace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showMap(nearByModel.getLatitude(), nearByModel.getLongitude(), nearByModel.getBusiness_name());
//
//            }
//        });
//
//
        setUiPageViewController(profilePhotos.size(), viewPagerCountDots);
//        nearby_item_RL.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Other.sendToThisActivity(getActivity(), MerchantDetailsActivity.class, new String[]{"position;" + position});
//            }
//        });

    }

    public void showMap(String lat, String lng, String name) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + name + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }


    private void setUiPageViewController(int dotscount, LinearLayout viewPagerCountDots) {
        viewPagerCountDots.removeAllViews();
        ImageView[] dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);


            viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    private void setUiPageViewController(int dotscount, LinearLayout viewPagerCountDots, int position) {
        viewPagerCountDots.removeAllViews();
        ImageView[] dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            viewPagerCountDots.addView(dots[i], params);
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

}
