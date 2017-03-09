package com.mentobile.grabbit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotificationDatabase {
    public static String table = "create table notification(id bigint not null primary key,msg varchar(200) not null,title varchar(40) not null,flag varchar(10) not null);";
    public static String table_offers = "create table offers" +
            "(id bigint not null primary key," +
            "msg varchar(200) not null," +
            "out_id varchar(40) not null," +
            "m_id varchar(10) not null," +
            "bcon_name varchar(10) not null," +
            "bcon_uuid varhcar(40) not null" +
            ");";

    SQLiteDatabase db;
    Context context;
    DatabaseHelper databaseHelper;

    public NotificationDatabase(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context ctx) {
            super(ctx, "grabbit", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(table);
                db.execSQL(table_offers);
                Log.w("both", "created SuccessFully");
            } catch (Exception e) {
                Log.w("Exception create table", e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("Drop table if already exists notification");
            db.execSQL("Drop table if already exists offers");
            onCreate(db);
        }
    }

    public NotificationDatabase open() {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }


    public void insert_offers(String id, String msg, String out_id, String m_id, String uuid, String bcon_name) {
        String query = "insert into offers values('" + id + "','" + msg + "','" + out_id + "','" + m_id + "','" + uuid + "','" + bcon_name + "')";
        db.execSQL(query);
    }

    public void delete_offers() {
        String query = "delete from offers";
        db.execSQL(query);
    }

    public Cursor getOffer(String uuid) {
        return db.rawQuery("select * from offers", null);
    }

    public void insert(String msg, String title, String flag) {
        long lo = System.currentTimeMillis();
        String query = "insert into notification values('" + lo + "','" + msg + "','" + title + "','" + flag + "')";
        db.execSQL(query);
    }

    public Cursor getData() {
        return db.rawQuery("select * from notification order by id desc limit 20", null);
    }

    public Cursor getUnSeenData() {
        return db.rawQuery("select * from notification where flag='unseen'", null);
    }

    public void delete() {
        String query = "delete from notification";
        db.execSQL(query);
    }

    public void update(String flagValue2) {
        String query1 = "update notification set flag='" + flagValue2 + "'";
        db.execSQL(query1);
    }

}
