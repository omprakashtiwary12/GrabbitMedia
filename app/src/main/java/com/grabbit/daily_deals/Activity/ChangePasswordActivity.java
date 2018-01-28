package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;

import org.json.JSONObject;

/**
 * Created by Gokul on 7/7/2016.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {
    EditText new_password, re_enter_password;
    Button save;

    private static final String TAG = "ChangePasswordActivity";
    private String phone = "";
    private String from = "";

    @Override
    public int getActivityLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initialize() {
        setTitle("Change Password");
        re_enter_password = (EditText) findViewById(R.id.re_enter_password);
        new_password = (EditText) findViewById(R.id.new_password);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void init(Bundle save) {
        save = getIntent().getExtras();
        if (save != null) {
            from = save.getString("from");
            phone = save.getString("phone");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                save();
                break;
        }
    }

    private void save() {
        String s1 = new_password.getText().toString();
        String s2 = re_enter_password.getText().toString();
        if (s1.equals(s2)) {
            sendToServer(s1);
        } else
            toastMessage("New and confirm password should be same.");
    }

    private void sendToServer(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("phone=").append(phone);
        stringBuilder.append("&password=").append(password);
        stringBuilder.append("&api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(ChangePasswordActivity.this, AppUrl.CHANGE_PASSWORD, 0, content, true, "Processing", this);
        getDataUsingWService.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        try {
            Log.w("LoginActivity", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
            if (from.equalsIgnoreCase("forget")) {
                if (status.equalsIgnoreCase("1")) {
                    jsonObject = jsonObject.getJSONObject("details");
                    String cus_id = jsonObject.getString("cus_id");
                    String phone = jsonObject.getString("phone");
                    String email = jsonObject.getString("email");
                    String name = jsonObject.getString("name");
                    String photo = jsonObject.getString("photo");
                    Other.saveDataInSharedPreferences(cus_id, name, email, phone, photo);
                    String emg_phone1 = jsonObject.getString("emg_phone1");
                    AppPref.getInstance().setEPhone1(emg_phone1);
                    String emg_phone2 = jsonObject.getString("emg_phone2");
                    AppPref.getInstance().setEPhone2(emg_phone2);
                    String emg_phone3 = jsonObject.getString("emg_phone3");
                    AppPref.getInstance().setEPhone3(emg_phone3);
                    sendToThisActivity(DashboardActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
            } else {
                finish();
            }
            toastMessage(msg);
        } catch (Exception e) {

        }
    }

}
