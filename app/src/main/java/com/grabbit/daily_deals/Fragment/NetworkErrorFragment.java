package com.grabbit.daily_deals.Fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.Other;

public class NetworkErrorFragment extends Fragment implements View.OnClickListener {

    private LinearLayout noInternet;
    private TextView tvRetry;
    private TextView tvTurnOn;

    public NetworkErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_error, container, false);

        noInternet = (LinearLayout) view.findViewById(R.id.nointernet);

        tvRetry = (TextView) view.findViewById(R.id.splash_tv_retry);
        tvRetry.setOnClickListener(this);

        tvTurnOn = (TextView) view.findViewById(R.id.splash_tv_turn_on);
        tvTurnOn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_tv_retry:
                if (Other.isNetworkAvailable(getActivity())) {
                    noInternet.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), getActivity().getClass());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                   // getActivity().finish();
                } else {
                    noInternet.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.splash_tv_turn_on:
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(callGPSSettingIntent, 0);
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                break;
        }
    }
}
