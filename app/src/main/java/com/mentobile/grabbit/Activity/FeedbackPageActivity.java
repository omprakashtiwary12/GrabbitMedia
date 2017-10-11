package com.mentobile.grabbit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedbackPageActivity extends BaseActivity {

    private EditText edComments;
    private Button btnSubmit;
    private RatingBar ratingBar;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_feedback_page;
    }

    @Override
    public void initialize() {

        edComments = (EditText) findViewById(R.id.feedback_comments);
        ratingBar = (RatingBar) findViewById(R.id.feedback_rating);
        btnSubmit = (Button) findViewById(R.id.feedback_btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comments = edComments.getText().toString();
                String ratingValue = "" + ratingBar.getRating();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("api_key=").append(AppUrl.API_KEY);
                stringBuilder.append("&comment=").append(comments);
                stringBuilder.append("&rating=").append(ratingValue);
                String content = stringBuilder.toString();
                GetDataUsingWService getDataUsingWService = new GetDataUsingWService(FeedbackPageActivity.this, AppUrl.FEEDBACK, 0, content, true, "Loading...", new GetWebServiceData() {
                    @Override
                    public void getWebServiceResponse(String responseData, int serviceCounter) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(responseData);
                            String result = jsonObject1.getString("status");
                            if (result.equals("1")) {
                                String msg = jsonObject1.getString("msg");
                                Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                getDataUsingWService.execute();
            }
        });
    }

    @Override
    public void init(Bundle save) {

    }
}
