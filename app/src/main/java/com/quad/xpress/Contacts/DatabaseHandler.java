package com.quad.xpress.Contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ctl on 13/3/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "contactsManager";

    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL_ID = "emailID";
    private static final String KEY_PIC_IMG = "picImg";
    private int condition = 0, condition1=0, condition2=0;
    private Context con;
   // public static ArrayList<String> newIxpressElist = new ArrayList<>();

    public static List<String> dbEmailList = new ArrayList<String>();
    public static List<String> dbNameList = new ArrayList<String>();
    public static List<String> dbPicList = new ArrayList<String>();

    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.con = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL_ID + " TEXT," + KEY_PIC_IMG + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);

    }

    public void addContact(String email, String name, String pic, int position) {

       List<String> contacts = getAllDetailsFormDatabase();
        SQLiteDatabase db = this.getWritableDatabase();

        if(contacts.size() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_EMAIL_ID, email);
            values.put(KEY_NAME, name);
            values.put(KEY_PIC_IMG, pic);
            db.insert(TABLE_CONTACTS, null, values);
            condition2 = 1;
        }else{
            for (String n : contacts) {
                if (email.equalsIgnoreCase(n)) {
                    condition++;
                    /*ContentValues values = new ContentValues();
                    values.put(KEY_EMAIL_ID, s);
                    db.insert(TABLE_CONTACTS, null, values);
                    db.close();*/
                    //return;
                } else {
                    condition1 = 0;
                    condition2 = 0;
                }

            }
        }
        if(condition <= 0 && condition1 == 0 && condition2 == 0){
            //Toast.makeText(con, "add success in database", Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            values.put(KEY_EMAIL_ID, email);
            values.put(KEY_NAME, name);
            values.put(KEY_PIC_IMG, pic);
            db.insert(TABLE_CONTACTS, null, values);
            //newIxpressElist.add(email);
        }else{
           // Toast.makeText(con, "already stored in database", Toast.LENGTH_SHORT).show();
        }
        db.close();
        //db.close();
       /* values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone*/
        // Inserting Row

        // Closing database connection

        if(position == -1){

        }else{
            if(Contact.getInstance().ixpressemail.get(position) != null) {
                Contact.getInstance().ixpressemail.remove(position);
                Contact.getInstance().ixpressname.remove(position);
                Contact.getInstance().ixpress_user_pic.remove(position);
            }else{
                Log.d("ixpressemail", "contacts data empty");
            }
        }

    }

    public List<String> getAllDetailsFormDatabase() {
        dbEmailList.clear();
        dbNameList.clear();
        dbPicList.clear();
        // Select All Query
        try{

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    dbEmailList.add(cursor.getString(2));
                    dbNameList.add(cursor.getString(1));
                   dbPicList.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }

            Collections.reverse(dbEmailList);
            Collections.reverse(dbNameList);
            Collections.reverse(dbPicList);

        }else{

        }

        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        // return contact list
        return dbEmailList;

    }





}
