package com.quad.xpress.contacts;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.quad.xpress.R;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.utills.helpers.FieldsValidator;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 8/11/17.
 */

public class contactSyncService extends Service {

        private Looper mServiceLooper;
        private ServiceHandler mServiceHandler;
        Cursor cursorPhone;
        Integer counter;
        FullContactsDBhandler fDB;
        List<FullContactDBOBJ> Dbcontacts;
        SharedPreferences sharedpreferences;


    // Handler that receives messages from the thread
        private final class ServiceHandler extends Handler {
            public ServiceHandler(Looper looper) {
                super(looper);
            }
            @Override
            public void handleMessage(Message msg) {
                sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
                fDB = new FullContactsDBhandler(contactSyncService.this);
                Dbcontacts = fDB.getAllContacts();
                int Dbver = Integer.parseInt(sharedpreferences.getString(SharedPrefUtils.SpDbver, "0"));
                fDB.onUpgrade(fDB.getReadableDatabase(),Dbver,Dbver++);
                Contact.getInstance().email_list.clear();
                Contact.getInstance().contact_namelist.clear();
                Contact.getInstance().contact_urilist.clear();
                Contact.getInstance().ContactPairs_phone.clear();
                Contact.getInstance().email_name.clear();

                String phoneNumber = null;
                String email = null;
                Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
                String _ID = ContactsContract.Contacts._ID;
                String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
                String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
                String CONTACT_LAST_UPDATED_TIMESTAMP = ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP;
                Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
                Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
                String DATA = ContactsContract.CommonDataKinds.Email.DATA;

                ContentResolver contentResolver = getContentResolver();
                cursorPhone = contentResolver.query(CONTENT_URI, null,null, null, null);


                // Iterate every contact in the phone
                if (cursorPhone.getCount() > 0) {
                    counter = 0;
                    while (cursorPhone.moveToNext()) {

                        String contact_id = cursorPhone.getString(cursorPhone.getColumnIndex(_ID));
                        String name = cursorPhone.getString(cursorPhone.getColumnIndex(DISPLAY_NAME));
                        int hasPhoneNumber = Integer.parseInt(cursorPhone.getString(cursorPhone.getColumnIndex(HAS_PHONE_NUMBER)));
                        if (hasPhoneNumber > 0) {

                            //This is to read multiple phone numbers associated with the same contact
                            Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                            while (phoneCursor.moveToNext()) {
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                                // Log.d("pno",output.toString());

                            }
                            phoneCursor.close();


                            if (name != null && phoneNumber != null && FieldsValidator.isPhoneNumberString(phoneNumber, true)) {

                                Contact.getInstance().email_list.add(phoneNumber.replace(" ", ""));
                                Contact.getInstance().contact_namelist.add(name);
                                Contact.getInstance().contact_urilist.add(String.valueOf(R.drawable.ic_user_icon));
                                Contact.getInstance().ContactPairs_phone.put(phoneNumber, name);
                                fDB.addContact(new FullContactDBOBJ(name.trim(), phoneNumber.replace(" ", "").trim(),String.valueOf(R.drawable.ic_user_icon), "false","yes"));

                                phoneNumber = null;


                            }

                        }
                    }

                }






                sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
                RestClient.get(contactSyncService.this).PostPhoneContacts(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new ContactsReq(Contact.getInstance().email_list),
                        new retrofit.Callback<ContactsResp>() {
                            @Override
                            public void success(ContactsResp contactsResp, Response response) {

                                if (contactsResp.getCode().equals("200")) {


                                    for (int i = 0; i < contactsResp.getData().length; i++) {

                                        fDB.addContactWithConflict(new FullContactDBOBJ(contactsResp.getData()[i].getUser_name().trim(),
                                                contactsResp.getData()[i].getEmail_id().trim().toLowerCase(), contactsResp.getData()[i].getProfile_image().trim(), "true","yep"));
                                    }

                                }



                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(contactSyncService.this, "Contacts Sync failed miserably...  ", Toast.LENGTH_SHORT).show();


                            }
                        });



                Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


                if (cur.getCount() > 0) {

                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor cur1 = contentResolver.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (cur1.moveToNext()) {

                            String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String emails = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                            if (emails != null && emails.length() > 5) {
                                // Log.d("EMAI", emails);
                                if (name != null && !name.contains("@")) {

                                    fDB.addContactWithConflict(new FullContactDBOBJ(StringUtils.abbreviate(name, 18), emails.trim().toLowerCase(), String.valueOf(R.drawable.ic_user_icon), "false","nope"));


                                } else {
                                    String val[] = name.split("@");

                                    fDB.addContactWithConflict(new FullContactDBOBJ(StringUtils.abbreviate(val[0], 18), emails.trim().toLowerCase(), String.valueOf(R.drawable.ic_user_icon), "false","nope"));


                                }
                            }


                        }
                        cur1.close();
                    }


                }
                cur.close();



            /*    for (FullContactDBOBJ cn : Dbcontacts) {

                    if(cn.get_ixprezuser().equalsIgnoreCase("true")){

                        Contact.getInstance().ixpressemail.add(cn.getPhoneNumber());
                        Contact.getInstance().ixpressname.add(cn.getName());
                        Contact.getInstance().ixpress_user_pic.add(cn.get_profile_pic());
                        Contact.getInstance().is_ixpress_user.add(true);
                    }

                }


                TreeMap<String,String> map = new TreeMap<>();

                for (FullContactDBOBJ cn : Dbcontacts) {
                    if((cn.get_diplayed().equalsIgnoreCase("true")) ){

                         map.put(cn.getName(),cn.getPhoneNumber());
                        *//*if((cn.get_diplayed().equalsIgnoreCase("true"))){

                        }*//*
                    }


                }

                Contact.getInstance().ixpressemail.addAll(map.values());
                Contact.getInstance().ixpressname.addAll(map.keySet());

                for (int i = 0; i < map.size(); i++) {
                    Contact.getInstance().ixpress_user_pic.add(String.valueOf(R.drawable.ic_user_icon));
                    Contact.getInstance().is_ixpress_user.add(false);
                }
*/
                stopSelf(msg.arg1);
            }
        }

        @Override
        public void onCreate() {
            // Start up the thread running the service.  Note that we create a
            // separate thread because the service normally runs in the process's
            // main thread, which we don't want to block.  We also make it
            // background priority so CPU-intensive work will not disrupt our UI.


            HandlerThread thread = new HandlerThread("ServiceStartArguments",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();

          //  Toast.makeText(this, "Staretd", Toast.LENGTH_SHORT).show();

            // Get the HandlerThread's Looper and use it for our Handler
            mServiceLooper = thread.getLooper();
            mServiceHandler = new ServiceHandler(mServiceLooper);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
             // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);

            // If we get killed, after returning from here, restart
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // We don't provide binding, so return null
            return null;
        }

        @Override
        public void onDestroy() {
          //  Toast.makeText(contactSyncService.this, "size in Service "+Dbcontacts.size(), Toast.LENGTH_SHORT).show();
            ContactsObservers contactsContentObserver = new ContactsObservers(null);
            getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false, contactsContentObserver);

        }
    public class ContactsObservers extends ContentObserver {

        public ContactsObservers(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);



        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {

            onCreate();
            Log.d("ccc","ccc");
            //Toast.makeText(contactSyncService.this, "contacts changed", Toast.LENGTH_SHORT).show();
        }

    }


}
