package com.mentobile.grabbit.Activity;

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
 * Created by Gokul on 11/23/2016.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {
    EditText act_signup_ET_lastName;
    EditText act_signup_ET_mobile;
    EditText act_signup_ET_email;
    EditText act_signup_ET_password;

    Button act_signup_BTN_next;

    private static final int SIMPLE_LOGIN_TYPE = 1;
    private static final int FACEBOOK_LOGIN_TYPE = 2;


    private String gcm_id = "";

    @Override
    public int getActivityLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initialize() {
        setTitle("Register");
        act_signup_ET_lastName = (EditText) findViewById(R.id.act_signup_ET_lastName);
        act_signup_ET_mobile = (EditText) findViewById(R.id.act_signup_ET_mobile);
        act_signup_ET_email = (EditText) findViewById(R.id.act_signup_ET_email);
        act_signup_ET_password = (EditText) findViewById(R.id.act_signup_ET_password);

        act_signup_BTN_next = (Button) findViewById(R.id.act_signup_BTN_next);
        act_signup_BTN_next.setOnClickListener(this);


    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_signup_BTN_next:
                save();
                break;
        }
    }

    private void save() {
        String name = act_signup_ET_lastName.getText().toString();
        String mobile = act_signup_ET_mobile.getText().toString();
        String email = act_signup_ET_email.getText().toString();
        String password = act_signup_ET_password.getText().toString();

        if (name.length() < 5) {
            toastMessage("Please Provide name");
            return;
        } else if (mobile.length() != 10) {
            toastMessage("Please Provide Mobile No");
            return;
        } else if (!Other.isValidEmail(email)) {
            toastMessage("Please Provide a Valid E-Mail Id");
            return;
        } else if (password.length() < 5) {
            toastMessage("Please Provide Password");
            return;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("api_key=").append(AppUrl.API_KEY);
            stringBuilder.append("&name=").append(name);
            stringBuilder.append("&phone=").append(mobile);
            stringBuilder.append("&email=").append(email);
            stringBuilder.append("&password=").append(password);
            stringBuilder.append("&login_type=").append(SIMPLE_LOGIN_TYPE);
            stringBuilder.append("&gcmid=").append(gcm_id);

            String content = stringBuilder.toString();

            GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.REGISTER_URL, 0, content, true, "Registering ...", this);
            getDataUsingWService.execute();
        }
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {

        try {
            Log.w("response_data", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
            String cus_id = jsonObject.getString("cus_id");
            if (status.equalsIgnoreCase("1")) {
                toastMessage(msg);
                Other.saveDataInSharedPreferences(cus_id, act_signup_ET_lastName.getText().toString(), act_signup_ET_email.getText().toString(), act_signup_ET_mobile.getText().toString());
                sendToThisActivity(OtpActivity.class, new String[]{"from;register", "phone;" + act_signup_ET_mobile.getText().toString()});
            }

        } catch (Exception e) {

        }
    }


}
