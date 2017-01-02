package com.mentobile.grabbit.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


/**
 * Created by user on 11/11/2015.
 */

public class GetDataUsingWService extends AsyncTask<String, String, String> {

    private Context activity;
    private String content;
    ProgressDialog cProgressDialog;
    private String url;
    GetWebServiceData getWebServiceData;
    private int serviceCounter;
    private String message = "Processing...";
    private boolean isShowProgress = true;
    private String responseData;




    public GetDataUsingWService(Context activity, String url, int serviceCounter, String content, boolean isShowProgress, String message, GetWebServiceData getWebServiceData) {
        this.activity = activity;
        this.content = content;
        this.url = url;
        this.serviceCounter = serviceCounter;
        this.getWebServiceData = getWebServiceData;
        this.isShowProgress = isShowProgress;
        this.message = message;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowProgress) {
            cProgressDialog = new CProgressDialog(activity);
            cProgressDialog.setMessage(message);
            cProgressDialog.setCanceledOnTouchOutside(false);
            cProgressDialog.setCancelable(false);
            cProgressDialog.show();
        }
    }


    @Override
    protected String doInBackground(String... params) {
        if (!Other.isNetworkAvailable(activity)) {
            Intent intent = new Intent(activity, NetworkErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } else {
            responseData = WebService1.Web_FetchData(url, content);
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (isShowProgress)
            cProgressDialog.hide();
        getWebServiceData.getWebServiceResponse(result, serviceCounter);
    }
}
