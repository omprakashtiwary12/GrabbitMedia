package com.mentobile.grabbit.Utility;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Deepak Sharma on 8/4/2015.
 */
public class CProgressDialog extends ProgressDialog {

    public CProgressDialog(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.setIndeterminate(true);
    }
}
