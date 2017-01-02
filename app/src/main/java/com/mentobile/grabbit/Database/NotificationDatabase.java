package com.mentobile.grabbit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotificationDatabase
{
    public static String table="create table notification(id bigint not null primary key,msg varchar(40) not null,image varchar(40) not null,date varchar(40) not null,flag varchar(10) not null);";

    SQLiteDatabase db;
    Context context;
    DatabaseHelper databaseHelper;
    public NotificationDatabase(Context context)
    {
        this.context=context;
        databaseHelper=new DatabaseHelper(context);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context ctx)
        {
            super(ctx,"karrots",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try
            {
                db.execSQL(table);
            }
            catch (Exception e)
            {
                Log.w("Exception create table", e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("Drop table if already exists notification");
            onCreate(db);
        }
    }

    public NotificationDatabase open()
    {
        db=databaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public void insert(String msg,String img,String date,String flag)
    {
        long lo=System.currentTimeMillis();
        String query="insert into notification values('"+lo+"','"+msg+"','"+img+"','"+date+"','"+flag+"')";
        db.execSQL(query);
    }

    public Cursor getData()
    {
        return db.rawQuery("select * from notification order by id desc limit 20", null);
    }

    public Cursor getUnSeenData()
    {
        return db.rawQuery("select * from notification where flag='unseen'",null);
    }

    public void delete()
    {
        String query="delete from notification";
        db.execSQL(query);
    }

    public void update(String flagValue2)
    {
        String query1="update notification set flag='"+flagValue2+"'";
        db.execSQL(query1);
    }

}
