package com.mentobile.grabbit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Deepak Sharma on 2/21/2017.
 */

public class Database extends SQLiteOpenHelper {

    String TAG = "DBHandler";

    private final static String DATABASE_NAME = "grabbit.db";
    public static final String TBL_BEACON_NOTIFICATION = "tbl_beacon_offers";

    private final String CREATE_TABLE_OFFERS = "create table tbl_beacon_offers" +
            "(id int not null primary key," +
            "message varchar(500) not null," +
            "out_id int not null," +
            "m_id varchar(10) not null," +
            "bcon_name varchar(100) not null," +
            "bcon_mazor int not null," +
            "bcon_minor int not null," +
            "bcon_uuid varhcar(100) not null)";

    public Database(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
        Log.d(TAG, "::::::Database");
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        onUpgrade(sqLiteDatabase, 1, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "::::::OnCreate");
        db.execSQL(CREATE_TABLE_OFFERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "::::::OnUpgrade");
        db.execSQL("Drop table if exists " + TBL_BEACON_NOTIFICATION);
        onCreate(db);
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BEACON_NOTIFICATION);
    }

    public void insertData(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TBL_BEACON_NOTIFICATION, null, values);
    }

    public Cursor getTableData(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        return cursor;
    }
}
