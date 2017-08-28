package com.quad.xpress.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kural on 8/19/17.
 */

public class FullContactsDBhandler  extends SQLiteOpenHelper {

        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "contactsManagerFull";

        // Contacts table name
        private static final String TABLE_CONTACTS = "contacts";

        // Contacts Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_PH_NO = "phone_number";
        private static final String KEY_PROFILE_PIC = "profile_pic";
        private static final String KEY_ixprezuser = "_ixprezuser";

        public FullContactsDBhandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_PH_NO + " TEXT," + KEY_PROFILE_PIC + " TEXT,"+ KEY_ixprezuser + " TEXT" + ")";
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

        /**
         * All CRUD(Create, Read, Update, Delete) Operations
         */
    
        // Adding new contact
       public void addContact(FullContactDBOBJ contact) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName()); // Contact Name
            values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
            values.put(KEY_PROFILE_PIC,contact.get_profile_pic());
            values.put(KEY_ixprezuser,contact.get_ixprezuser());

           Log.d("dbInsert",values.toString());
            // Inserting Row
            db.insert(TABLE_CONTACTS, null, values);
            db.close(); // Closing database connection
        }

    public void addContactWithConflict(FullContactDBOBJ contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        int row = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        values.put(KEY_PROFILE_PIC,contact.get_profile_pic());
        values.put(KEY_ixprezuser,contact.get_ixprezuser());

        Log.d("dbInsert with con",values.toString());
        // Inserting Row
        db.insertWithOnConflict(TABLE_CONTACTS, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        // db.insert(TABLE_CONTACTS, null, values,SQLiteDatabase.CONFLICT_REPLACE);
     //   db.close(); // Closing database connection



    }

        // Getting single contact
        FullContactDBOBJ getContact(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                            KEY_NAME, KEY_PH_NO , KEY_PROFILE_PIC, KEY_ixprezuser }, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            FullContactDBOBJ contact = new FullContactDBOBJ(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));
            // return contact
            return contact;
        }

        // Getting All Contacts
        public List<FullContactDBOBJ> getAllContacts() {
            List<FullContactDBOBJ> contactList = new ArrayList<FullContactDBOBJ>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    FullContactDBOBJ contact = new FullContactDBOBJ();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNumber(cursor.getString(2));
                    contact.set_profile_pic(cursor.getString(3));
                    contact.set_ixprezuser(cursor.getString(4));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

            // return contact list
            return contactList;
        }

        // Updating single contact
        public int updateContact(FullContactDBOBJ contact) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName());
            values.put(KEY_PH_NO, contact.getPhoneNumber());
            values.put(KEY_PROFILE_PIC, contact.get_profile_pic());
            values.put(KEY_ixprezuser, contact.get_ixprezuser());

            Log.d("db update",values.toString());
            return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                    new String[] { String.valueOf(contact.getID()) });
        }

        // Deleting single contact
        public void deleteContact(FullContactDBOBJ contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                    new String[] { String.valueOf(contact.getID()) });
            db.close();
        }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(DATABASE_NAME, null, null);

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
