package com.grabbit.daily_deals.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.grabbit.daily_deals.R;


/**
 * Created by Gokul on 5/13/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(getActivityLayout());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
        init(save);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void Alert(String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    public String readString(int string) {
        return getResources().getString(string);
    }

    public String getDeviceModel() {
        return Build.MANUFACTURER;
    }

    public abstract int getActivityLayout();

    public abstract void initialize();

    public abstract void init(Bundle save);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        hideKeyBoard();
    }


    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void sendToThisActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void sendToThisActivity(Class activity, int flag) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(flag);
        startActivity(intent);
    }

    public void sendToThisActivity(Class activity, String s[]) {
        Intent intent = new Intent(this, activity);
        for (int i = 0; i < s.length; i++) {
            String p[] = s[i].split(";");
            intent.putExtra(p[0], p[1]);
        }
        startActivity(intent);
    }
}
