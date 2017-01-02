package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;

import org.json.JSONObject;

/**
 * Created by Gokul on 7/7/2016.
 */
public class ForgetNewPasswordActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {
    EditText new_password, re_enter_password;
    Button save;

    private static final String TAG = "ForgetNewPasswordActivity";

    private String phone = "";

    @Override
    public int getActivityLayout() {
        return R.layout.activity_forget_new_password;
    }

    @Override
    public void initialize() {
        re_enter_password = (EditText) findViewById(R.id.re_enter_password);
        new_password = (EditText) findViewById(R.id.new_password);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(this);
    }

    @Override
    public void init(Bundle save) {
        save = getIntent().getExtras();
        if (save != null) {
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
            toastMessage("re_enter password not matched");
    }

    private void sendToServer(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("phone=").append(phone);
        stringBuilder.append("&password=").append(password);
        stringBuilder.append("&api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();

        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(ForgetNewPasswordActivity.this, AppUrl.SET_PASSWORD_URL, 0, content, true, "Processing", this);
        getDataUsingWService.execute();

    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        try {
            Log.w("LoginActivity", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                String msg = jsonObject.getString("msg");
                toastMessage(msg);
                jsonObject = jsonObject.getJSONObject("details");
                String cus_id = jsonObject.getString("cus_id");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String name = jsonObject.getString("name");
                Other.saveDataInSharedPreferences(cus_id, name, email, phone);
                sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        } catch (Exception e) {

        }
    }

}
