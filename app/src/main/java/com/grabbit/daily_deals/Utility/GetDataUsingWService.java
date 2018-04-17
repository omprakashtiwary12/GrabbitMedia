package com.grabbit.daily_deals.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.grabbit.daily_deals.Fragment.NetworkErrorFragment;
import com.grabbit.daily_deals.R;


/**
 * Created by user on 11/11/2015.
 */

public class GetDataUsingWService extends AsyncTask<String, String, String> {

    private Activity activity;
    private String content;
    private CProgressDialog progressBar;
    private String url;
    private GetWebServiceData getWebServiceData;
    private int serviceCounter;
    private String message = "Processing...";
    private boolean isShowProgress = true;
    private String responseData;

    public GetDataUsingWService(Activity activity, String url, int serviceCounter, String content, boolean isShowProgress, String message, GetWebServiceData getWebServiceData) {
        this.activity = activity;
        this.content = content;
        this.url = url;
        this.serviceCounter = serviceCounter;
        this.getWebServiceData = getWebServiceData;
        this.isShowProgress = isShowProgress;
        this.message = message;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowProgress) {
            progressBar = new CProgressDialog(activity);
            progressBar.setIndeterminate(false);
            progressBar.setMessage(message);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setCancelable(false);
            progressBar.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if (!Other.isNetworkAvailable(activity)) {
            NetworkErrorFragment networkErrorFragment = new NetworkErrorFragment();
            activity.getFragmentManager().beginTransaction().
                    add(android.R.id.content, networkErrorFragment).commit();
        } else {
            responseData = WebService1.Web_FetchData(url, content);
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (isShowProgress && progressBar != null)
            progressBar.dismiss();
        getWebServiceData.getWebServiceResponse(result, serviceCounter);
    }
}
