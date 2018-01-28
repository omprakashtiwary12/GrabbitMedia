package com.grabbit.daily_deals.Fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

    public ImageViewFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser) {
//            Activity a = getActivity();
//            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//    }

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
        NearByModel nearByModel = ((MerchantDetailsActivity) getActivity()).nearByModel;

        String strPageType = ((MerchantDetailsActivity) getActivity()).strPagerType;
        List<ImageModel> imageModels;
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
    }

    @Override
    public void viewPagerItemClick(int poition) {

    }
}