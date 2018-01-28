package com.grabbit.daily_deals.Fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grabbit.daily_deals.Activity.DashboardActivity;
import com.grabbit.daily_deals.Activity.FirstPageActivity;
import com.grabbit.daily_deals.Activity.HelpActivity;
import com.grabbit.daily_deals.Activity.MerchantDetailsActivity;
import com.grabbit.daily_deals.Activity.SplashActivity;
import com.grabbit.daily_deals.Adapter.RecyclerAdapter;
import com.grabbit.daily_deals.Model.Helpline;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpLineFragment extends Fragment implements RecyclerAdapter.onItemClickListener, RecyclerAdapter.ReturnView, GetWebServiceData {

    private RecyclerView frag_nearby_rv;
    private RecyclerAdapter recyclerAdapter;
    private List<Helpline> nearHelplineLine = new ArrayList<Helpline>();
    String[] arrayTitle = {"Police", "Fire", "Ambulance", "Child Helpline", "Women in Distress"};
    String[] arrayNumber = {"100", "101", "102", "1098", "1091"};

    private Button btnSOSShortcut;
    private String TAG = "HelpLineFragment";

    public HelpLineFragment() {
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
        return inflater.inflate(R.layout.fragment_help_line, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nearHelplineLine.clear();
        for (int i = 0; i < arrayTitle.length; i++) {
            Helpline helpline = new Helpline(i, arrayTitle[i], arrayNumber[i]);
            nearHelplineLine.add(helpline);
        }

        frag_nearby_rv = (RecyclerView) view.findViewById(R.id.recycle_view_helpline_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        frag_nearby_rv.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerAdapter(nearHelplineLine, getActivity(), R.layout.list_item_helpline, this, 0, this);
        frag_nearby_rv.setAdapter(recyclerAdapter);

        boolean isAppInstalled = appInstalledOrNot("com.check.application");

        btnSOSShortcut = (Button) view.findViewById(R.id.helpline_btn_shortcut);
        btnSOSShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 25) {
                    addShortcut();
                } else {
                    addShortcut1();
                }
            }
        });

        FloatingActionButton fbSOS = (FloatingActionButton) view.findViewById(R.id.activity_dashboard_fb_sos);
        fbSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencyMessage();
            }
        });
    }

    private void sendEmergencyMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&cus_id=").append("" + AppPref.getInstance().getUserID());
        stringBuilder.append("&current_location=").append(Other.getCurrentAddress(getActivity()));
        String content = stringBuilder.toString();
        GetDataUsingWService serviceProfileUpdate =
                new GetDataUsingWService(getActivity(), AppUrl.EMERGENCY_MESSAGE, 0, content, true, "Sending...", this);
        serviceProfileUpdate.execute();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @Override
    public void getItemPosition(int position) {

    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {

        final Helpline helpline = nearHelplineLine.get(position);
        TextView nearby_item_TXT_address = (TextView) view.findViewById(R.id.list_item_helpline_title);
        nearby_item_TXT_address.setText("" + helpline.getTitle());

        TextView nearby_item_TXT_distace = (TextView) view.findViewById(R.id.list_item_helpline_number);
        nearby_item_TXT_distace.setText("" + helpline.getPhone());

        ImageView imageView = (ImageView) view.findViewById(R.id.helpline_img_callback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Other.callNow(getActivity(), "" + helpline.getPhone());
            }
        });
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        try {
            Intent shortCutInt = new Intent(getActivity(), FirstPageActivity.class);
            shortCutInt.setAction(Intent.ACTION_MAIN);
            shortCutInt.putExtra("isProd", true);
            Intent addIntent = new Intent();
            addIntent
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutInt);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Grabbit1");
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(getActivity(),
                            R.drawable.sos));
            addIntent.putExtra("duplicate", false);
            addIntent
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getActivity().getApplicationContext().sendBroadcast(addIntent);
            //Toast.makeText(getActivity(), "Shortcut created successfully.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "::::Exception " + e.getMessage());
        }
    }

    @TargetApi(25)
    private void addShortcut1() {
        try {
            // after Android 7.0
            ShortcutManager manager = getActivity().getSystemService(ShortcutManager.class);
            Intent shortCutInt = new Intent(getActivity(), FirstPageActivity.class);
            shortCutInt.setAction(Intent.ACTION_MAIN);
            shortCutInt.putExtra("isProd", true);
            shortCutInt.putExtra("duplicate", false);
            ShortcutInfo info = new ShortcutInfo.Builder(getActivity(), "shortcutID")
                    .setShortLabel("Grabbit1")
                    .setIcon(Icon.createWithResource(getActivity(), R.drawable.sos))
                    .setIntent(shortCutInt)
                    .build();
            manager.setDynamicShortcuts(Arrays.asList(info));
        } catch (Exception e) {
            Log.d(TAG, "::::::Exception " + e.getMessage());
        }
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        if (responseData != null) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
