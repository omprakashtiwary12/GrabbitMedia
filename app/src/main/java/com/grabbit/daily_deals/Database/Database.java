package com.grabbit.daily_deals.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.TimeUtils;

import com.grabbit.daily_deals.Utility.Other;

import java.util.concurrent.TimeUnit;

/**
 * Created by Deepak Sharma on 2/21/2017.
 */

public class Database extends SQLiteOpenHelper {

    String TAG = "DBHandler";
    private final static String DATABASE_NAME = "grabbit.db";
    public static final String TBL_BEACON_OFFERS = "tbl_beacon_offers";
    public static final String TBL_MERCHANTS = "tbl_merchants";
    public static final String TBL_COMPAIGNS = "tbl_compaign";
    public static final String TBL_NOTIFICATION = "tbl_notification";

    private final String CREATE_TABLE_BCON_OFFERS = "create table tbl_beacon_offers" +
            "(id int not null primary key," +
            "message varchar(500) not null," +
            "out_id int not null," +
            "buz_name varchar(100) not null," +
            "m_id varchar(10) not null," +
            "bcon_name varchar(100) not null," +
            "bcon_mazor int not null," +
            "bcon_minor int not null," +
            "bcon_uuid varhcar(100) not null)";

    private final String CREATE_TABLE_MERCHANTS = "create table tbl_merchants" + "(id INTEGER PRIMARY KEY AUTOINCREMENT,m_id varchar(6)," +
            "business_name varchar(100)," + "logo varchar(50)," + "business_banner varchar(50)," +
            "email varchar(50)," + "cat_id int," + "about varchar(500)," +
            "open_time varchar(20)," + "close_time varchar(20)," + "opening_days varchar(100)," +
            "website varchar(50)," + "facebook varchar(50)," + "twitter varchar(50)," +
            "Instagram varchar(50)," + "gallery_img1 varchar(20)," + "gallery_img2 varchar(20)," +
            "gallery_img3 varchar(20)," + "gallery_img4 varchar(20)," + "gallery_img5 varchar(20)," +
            "gallery_img6 varchar(20)," + "status varchar(10)," + "wishlist varchar(10)," +
            "out_id varchar(6)," + "name varchar(50)," + "address varchar(50)," +
            "city_name varchar(20)," + "state_name varchar(20)," + "pincode varchar(6)," + "distance double," +
            "phone varchar(10)," + "latitute varchar(20)," + "longtitute varchar(20)," + "outlet_status varchar(10)," +
            "bcon_id varchar(10)," + "bcon_uuid varchar(100))";

    private final String CREATE_TABLE_COMPAIGNS = "create table tbl_compaign" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "m_id varchar(6)," +
            "out_id varchar(6)," +
            "banner_name varchar(100)," +
            "start_dt varchar(6)," +
            "end_dt varchar(6)," +
            "offer_description varchar(100)," +
            "offer_details varchar(300))";

    private final String CREATE_TABLE_NOTIFICATION = "create table if not exists tbl_notification(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "message varchar(200) not null,title varchar(40) not null " +
            ",flag INTEGER DEFAULT 0 not null, msg_id INTEGER,out_id INTEGER,current_time LONG, notification_dt DATETIME DEFAULT CURRENT_TIMESTAMP, read_status INTEGER DEFAULT 0 NOT NULL)";

    public Database(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        onUpgrade(sqLiteDatabase, 1, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BCON_OFFERS);
        db.execSQL(CREATE_TABLE_MERCHANTS);
        db.execSQL(CREATE_TABLE_COMPAIGNS);
        db.execSQL(CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(TBL_BEACON_OFFERS);
        dropTable(TBL_MERCHANTS);
        dropTable(TBL_COMPAIGNS);
//        dropTable(TBL_NOTIFICATION);
        onCreate(db);
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void truncateTable(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE from " + tableName);
    }

    public void insertData(ContentValues values, String tablename) {
        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow(tablename, null, values);
    }

    public Cursor getTableData(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        return cursor;
    }

    public Cursor getAllNotification(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " order by notification_dt desc", null);
        return cursor;
    }

    public Cursor getMerchantDetailsFromLocalDB(String query) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getMerchantCompaign(String out_id, String current_dt) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM tbl_compaign where out_id=" + "'" + out_id + "'";
        Log.d(TAG,"::::::Query "+query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllOffers(int cat_id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM tbl_compaign where out_id=" + "'" + cat_id + "'";
        Log.d(TAG,"::::::Query "+query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean iSCompaignExits(int msg_id, int out_id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM tbl_notification where msg_id=" + "'" + msg_id + "' AND out_id=" + "'" + out_id + "' AND flag='0'";
        Log.d(TAG, ":::::Query " + query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            long currentDate = cursor.getLong(6);
            int id = cursor.getInt(0);
            long hours = TimeUnit.MILLISECONDS.toHours(Other.getCurrentTime() - currentDate);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(Other.getCurrentTime() - currentDate);
            Log.d(TAG, "::::::seconds " + seconds);
            if (hours < 1) {
                return true;
            } else {
                SQLiteDatabase dbWrite = getWritableDatabase();
//                String query_update = "UPDATE tbl_notification SET flag=1 where id=" + id;
//                dbWrite.rawQuery(query_update, null);
                ContentValues values = new ContentValues();
                values.put("flag", 1);
                dbWrite.update(TBL_NOTIFICATION, values, "id=" + id, null);
                return false;
            }
        }
        return false;
    }

    public void changeReadStatus() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("read_status", 1);
        db.update(TBL_NOTIFICATION, contentValues, null, null);
    }

    public int getNotificationCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_notification where read_status=0", null);
        int count = cursor.getCount();
        return count;
    }


}
