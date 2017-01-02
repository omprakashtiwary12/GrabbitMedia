package com.mentobile.grabbit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mentobile.grabbit.Model.Contact;
import com.mentobile.grabbit.Model.NearByModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_M_id = "m_id";
    private static final String KEY_BUSINESS_NAME = "business_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_ABOUT = "about";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_FACEBOOK = "facebook";
    private static final String KEY_TWITTER = "twitter";
    private static final String KEY_INSTAGRAM = "Instagram";
    private static final String KEY_OPENTIME = "open_time";
    private static final String KEY_CLOSETIME = "close_time";
    private static final String KEY_CITY_NAME = "city_name";
    private static final String KEY_STATENAME = "state_name";
    private static final String KEY_BUSINESSLOGO = "business_logo";
    private static final String KEY_BUSINESS = "business_banner";
    private static final String KEY_GALLERY_IMG1 = "gallery_img1";
    private static final String KEY_GALLARY_IMG2 = "gallery_img2";
    private static final String KEY_GALLARY_IMG3 = "gallery_img3";
    private static final String KEY_GALLARY_IMG4 = "gallery_img4";
    private static final String KEY_GALLARY_IMG5 = "gallery_img5";
    private static final String KEY_OPNING_DAYS = "opening_days";
    private static final String KEY_CAT_NAME = "cat_name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_WISHLIST = "wishlist";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_M_id + "TEXT," + KEY_BUSINESS_NAME + "TEXT,"
                + KEY_EMAIL + "TEXT," + KEY_PHONE + "TEXT,"
                + KEY_LATITUDE + "TEXT," + KEY_LONGITUDE + "TEXT,"
                + KEY_ADDRESS + "TEXT," + KEY_DISTANCE + "TEXT,"
                + KEY_ABOUT + "TEXT," + KEY_WEBSITE + "TEXT,"
                + KEY_FACEBOOK + "TEXT," + KEY_TWITTER + "TEXT,"
                + KEY_INSTAGRAM + "TEXT," + KEY_OPENTIME + "TEXT,"
                + KEY_CLOSETIME + "TEXT," + KEY_CITY_NAME + "TEXT,"
                + KEY_STATENAME + "TEXT," + KEY_BUSINESSLOGO + "TEXT,"
                + KEY_BUSINESS + "TEXT," + KEY_GALLERY_IMG1 + "TEXT,"
                + KEY_GALLARY_IMG2 + "TEXT," + KEY_GALLARY_IMG3 + "TEXT,"
                + KEY_GALLARY_IMG4 + "TEXT," + KEY_GALLARY_IMG5 + "TEXT,"
                + KEY_OPNING_DAYS + "TEXT," + KEY_CAT_NAME + "TEXT,"
                + KEY_STATUS + "TEXT," + KEY_WISHLIST + "TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addContact(NearByModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName()); // Contact Name
//        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        values.put(KEY_M_id ,contact.getM_id());
        values.put(KEY_BUSINESS_NAME , contact.getBusiness_name());
        values.put(KEY_EMAIL , contact.getEmail());
        values.put(KEY_PHONE , contact.getPhone());
        values.put(KEY_LATITUDE,contact.getLatitude());
        values.put(KEY_LONGITUDE,contact.getLongitude());
        values.put(KEY_ADDRESS ,contact.getAddress());
        values.put(KEY_DISTANCE,contact.getDistance());
        values.put(KEY_ABOUT,contact.getAbout());
        values.put(KEY_WEBSITE, contact.getWebsite());
        values.put(KEY_FACEBOOK,contact.getFacebook());
        values.put(KEY_TWITTER,contact.getTwitter());
        values.put(KEY_INSTAGRAM,contact.getInstagram());
        values.put(KEY_OPENTIME,contact.getOpen_time());
        values.put(KEY_CLOSETIME,contact.getClose_time());
        values.put(KEY_CITY_NAME,contact.getCity_name());
        values.put(KEY_STATENAME,contact.getState_name());
        values.put(KEY_BUSINESSLOGO,contact.getBusiness_logo());
        values.put(KEY_BUSINESS,contact.getBusiness_banner());
        values.put(KEY_GALLERY_IMG1,contact.getGallery_img1());
        values.put(KEY_GALLARY_IMG2,contact.getGallery_img2());
        values.put(KEY_GALLARY_IMG3,contact.getGallery_img3());
        values.put(KEY_GALLARY_IMG4,contact.getGallery_img4());
        values.put(KEY_GALLARY_IMG5,contact.getGallery_img5());
        values.put(KEY_OPNING_DAYS,contact.getGallery_img5());
        values.put(KEY_CAT_NAME,contact.getGallery_img5());
        values.put(KEY_STATUS,contact.getGallery_img5());
        values.put(KEY_WISHLIST,contact.getGallery_img5());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PH_NO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<NearByModel> getAllContacts() {
        List<NearByModel> contactList = new ArrayList<NearByModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NearByModel contact = new NearByModel();
                contact.setM_id(cursor.getString(1));
                contact.setBusiness_name(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setPhone(cursor.getString(4));
                contact.setLatitude(cursor.getString(5));
                contact.setLongitude(cursor.getString(6));
                contact.setAddress(cursor.getString(7));
                contact.setDistance(cursor.getString(8));
                contact.setAbout(cursor.getString(9));
                contact.setWebsite(cursor.getString(10));
                contact.setFacebook(cursor.getString(11));
                contact.setTwitter(cursor.getString(12));
                contact.setInstagram(cursor.getString(13));
                contact.setOpen_time(cursor.getString(14));
                contact.setClose_time(cursor.getString(15));
                contact.setCity_name(cursor.getString(16));
                contact.setState_name(cursor.getString(17));
                contact.setBusiness_logo(cursor.getString(18));
                contact.setBusiness_banner(cursor.getString(19));
                contact.setGallery_img1(cursor.getString(20));
                contact.setGallery_img2(cursor.getString(21));
                contact.setGallery_img3(cursor.getString(22));
                contact.setGallery_img4(cursor.getString(23));
                contact.setGallery_img5(cursor.getString(24));
                contact.setOpening_days(cursor.getString(25));
                contact.setCat_name(cursor.getString(26));
                contact.setStatus(cursor.getString(27));
                contact.setWishlist(cursor.getString(28));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}