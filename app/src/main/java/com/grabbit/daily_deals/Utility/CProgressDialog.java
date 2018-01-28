package com.grabbit.daily_deals.Utility;

import android.app.ProgressDialog;
import android.content.Context;

import com.grabbit.daily_deals.R;

/**
 * Created by Deepak Sharma on 8/4/2015.
 */
public class CProgressDialog extends ProgressDialog {

    public CProgressDialog(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.setIndeterminate(false);
    }
}
