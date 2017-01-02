package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mentobile.grabbit.Database.DatabaseHandler;
import com.mentobile.grabbit.Model.Contact;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Service.DownloadDataService;

import java.util.List;

/**
 * Created by Administrator on 12/30/2016.
 */

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        DatabaseHandler db = new DatabaseHandler(this);
        Log.d("Insert: ", "Downloading  ..");
        startService(new Intent(this, DownloadDataService.class));

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
//        db.addContact(new Contact("Ravi", "9100000000"));
//        db.addContact(new Contact("Srinivas", "9199999999"));
//        db.addContact(new Contact("Tommy", "9522222222"));
//        db.addContact(new Contact("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
      //  List<Contact> contacts = db.getAllContacts();

//        for (Contact cn : contacts) {
//            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
//            // Writing Contacts to log
//            Log.d("Name: ", log);
//        }
    }
}
