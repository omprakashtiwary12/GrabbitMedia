package com.grabbit.daily_deals.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Gokul on 11/23/2016.
 */
public class OtpActivity extends BaseActivity implements View.OnClickListener, GetWebServiceData {
    private EditText edit_otp;
    private Button verify_otp;
    private TextView resend_otp;

    private String otp = "";
    private String phone = "";
    private String from = "";
    private TextView textView;


    @Override
    public int getActivityLayout() {
        return R.layout.activity_otp;
    }

    @Override
    public void initialize() {
        setTitle("Account Verification");
        edit_otp = (EditText) findViewById(R.id.edit_otp);
        verify_otp = (Button) findViewById(R.id.verify_otp);
        resend_otp = (TextView) findViewById(R.id.resend_otp);

        textView = (TextView) findViewById(R.id.text_message);

        resend_otp.setOnClickListener(this);
        verify_otp.setOnClickListener(this);
    }

    @Override
    public void init(Bundle save) {
        save = getIntent().getExtras();
        if (save != null) {
            from = save.getString("from");
            phone = save.getString("phone");
            otp = save.getString("otp");
            textView.setText("+91 " + phone);
            if (from.equalsIgnoreCase("login")) {
                send_otp();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify_otp:
                String ed_otp = edit_otp.getText().toString().trim();
                if (otp.equalsIgnoreCase(ed_otp)) {
                    verify_otp();
                } else {
                    toastMessage("Please check your OTP or Resend OTP.");
                }
                break;
            case R.id.resend_otp:
                send_otp();
                break;
        }
    }

    private void send_otp() {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        otp = randomPIN + "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("otp=").append(otp);
        stringBuilder.append("&phone=").append(phone);
        stringBuilder.append("&api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService wService = new GetDataUsingWService(OtpActivity.this, AppUrl.OTP_URL, 0, content, true, "Sending OTP ...", this);
        wService.execute();
    }

    private void verify_otp() {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        otp = randomPIN + "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&user_id=").append(AppPref.getInstance().getUserID());
        stringBuilder.append("&api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService wService = new GetDataUsingWService(OtpActivity.this, AppUrl.OTP_VERIFY, 1, content, true, "Verify OTP...", this);
        wService.execute();
    }


    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        try {
            Log.w("responseData", responseData);
            JSONObject jsonObject = new JSONObject(responseData);
            if (serviceCounter == 0) {
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    toastMessage("Resend otp successfully.");
                }
            } else {
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    toastMessage("OTP verify successfully.");
                    sendToThisActivity(DashboardActivity.class);
                    finish();
                }
            }
        } catch (Exception e) {
        }
    }
}
