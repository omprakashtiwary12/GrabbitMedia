package com.grabbit.daily_deals.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grabbit.daily_deals.Activity.MerchantDetailsActivity;
import com.grabbit.daily_deals.Adapter.ViewPagerAdapter_Tap;
import com.grabbit.daily_deals.Model.ImageModel;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;

import java.util.List;

public class ImageViewFragment extends Fragment implements ViewPagerAdapter_Tap.iPageonClick {

    private ViewPager viewPagerGalleryView;
    private List<ImageModel> imageModels;

    public ImageViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MerchantDetailsActivity) getActivity()).getSupportActionBar().setTitle("");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPagerGalleryView = (ViewPager) view.findViewById(R.id.imageview_taping);
        // Set title bar
        viewPagerGalleryView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("ImageModel", ":::::Position " + position);
                ((MerchantDetailsActivity) getActivity()).getSupportActionBar().setTitle((position + 1) + " of " + imageModels.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        NearByModel nearByModel = ((MerchantDetailsActivity) getActivity()).nearByModel;

        String strPageType = ((MerchantDetailsActivity) getActivity()).strPagerType;
        if (strPageType.equalsIgnoreCase("gallery")) {
            imageModels = nearByModel.getGalleyImage();
        } else {
            imageModels = nearByModel.getOfferImageModels();
        }
        ViewPagerAdapter_Tap
                viewPagerAdapter1 = new ViewPagerAdapter_Tap(getActivity(), imageModels, this);
        viewPagerGalleryView.setAdapter(viewPagerAdapter1);
        viewPagerGalleryView.setCurrentItem(((MerchantDetailsActivity) getActivity()).pagerPosition);
        viewPagerAdapter1.notifyDataSetChanged();
        ((MerchantDetailsActivity) getActivity()).getSupportActionBar().setTitle("1" + " of " + imageModels.size());
    }

    @Override
    public void viewPagerItemClick(int poition) {
        Log.d("ImageModel", ":::::Position " + poition);
    }
}