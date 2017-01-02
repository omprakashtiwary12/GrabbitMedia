package com.mentobile.grabbit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/26/2016.
 */

public class DBHepler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GrabItDB.db";
    public static final String CONTACTS_TABLE_NAME = "listdata";

    public DBHepler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table listdata " +
                        //"(id integer primary key, name text,phone text,email text, street text,place text)"
                        "(text m_id ,text business_name,text email ,text phone ,text latitude ,text longitude,text address ,text about ,text website ,text facebook ,text twitter ,text Instagram ,text open_time ,text close_time ,text city_name ,text state_name ,text business_logo ,text business_banner,text gallery_img1,text gallery_img2,text gallery_img3,text gallery_img4,text gallery_img5,text opening_days ,text cat_name ,text status ,text wishlist )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS listdata");
        onCreate(sqLiteDatabase);
    }

    public boolean insertListData(String m_id, String business_name, String email, String phone, String latitude, String longitude, String address, String about, String website, String facebook, String twitter, String Instagram, String open_time, String close_time, String city_name, String state_name, String business_logo, String business_banner, String gallery_img1, String gallery_img2, String gallery_img3, String gallery_img4, String gallery_img5, String opening_days, String cat_name, String status, String wishlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("m_id", m_id);
        contentValues.put("business_name", business_name);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("address", address);
        contentValues.put("about", about);
        contentValues.put("website", website);
        contentValues.put("facebook", facebook);
        contentValues.put("twitter", twitter);
        contentValues.put("Instagram", Instagram);
        contentValues.put("open_time", open_time);
        contentValues.put("close_time", close_time);
        contentValues.put("city_name", city_name);
        contentValues.put("state_name", state_name);
        contentValues.put("business_logo", business_logo);
        contentValues.put("business_banner", business_banner);
        contentValues.put("business_logo", business_logo);
        contentValues.put("cat_name", cat_name);
        contentValues.put("status", status);
        contentValues.put("wishlist", wishlist);
        db.insert("listdata", null, contentValues);
        return true;
    }

    public boolean updateContact(Integer id, String m_id, String business_name, String email, String phone, String latitude, String longitude, String address, String about, String website, String facebook, String twitter, String Instagram, String open_time, String close_time, String city_name, String state_name, String business_logo, String business_banner, String gallery_img1, String gallery_img2, String gallery_img3, String gallery_img4, String gallery_img5, String opening_days, String cat_name, String status, String wishlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("m_id", m_id);
        contentValues.put("business_name", business_name);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("address", address);
        contentValues.put("about", about);
        contentValues.put("website", website);
        contentValues.put("facebook", facebook);
        contentValues.put("twitter", twitter);
        contentValues.put("Instagram", Instagram);
        contentValues.put("open_time", open_time);
        contentValues.put("close_time", close_time);
        contentValues.put("city_name", city_name);
        contentValues.put("state_name", state_name);
        contentValues.put("business_logo", business_logo);
        contentValues.put("business_banner", business_banner);
        contentValues.put("business_logo", business_logo);
        contentValues.put("cat_name", cat_name);
        contentValues.put("status", status);
        contentValues.put("wishlist", wishlist);
        db.update("listdata", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    public ArrayList<String> getAlllistdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from listdata", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_TABLE_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
