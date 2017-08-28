package com.quad.xpress.contacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.quad.xpress.R;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.utills.helpers.FieldsValidator;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 8/18/17.
 */

public class ContactsReaderObject {
    Context context;
    Activity activity;



    ContactsReaderObject(Activity activity){
        this.activity =activity;
    }

    public ArrayList<String> mtd_contacts_phone (){

        context = activity.getApplicationContext();


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

        ContentResolver contentResolver = context.getContentResolver();
       Cursor cursorPhone = contentResolver.query(CONTENT_URI, null,null, null, null);

        Boolean IsNewVer =false ;

        if (IsNewVer){


        }



        // Iterate every contact in the phone
        if (cursorPhone.getCount() > 0) {

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

                    if (name != null && phoneNumber != null && FieldsValidator.isPhoneNumberString(phoneNumber, true)) {

                        //phonecontactList.add(output.toString());

                        Contact.getInstance().email_list.add(phoneNumber.replace(" ",""));
                        Contact.getInstance().contact_namelist.add(name);
                        Contact.getInstance().contact_urilist.add(String.valueOf(R.drawable.ic_user_icon));
                        Contact.getInstance().ContactPairs_phone.put(phoneNumber, name);

                           /* Contact.getInstance().ixpressemail.add(phoneNumber);
                            Contact.getInstance().ixpressname.add(name);
                            Contact.getInstance().ixpress_user_pic.add(String.valueOf(R.drawable.ic_user_icon));*/

                        phoneNumber = null;


                    }
                    phoneCursor.close();
                }
            }

          }

        return Contact.getInstance().email_list;
    }
    private void callwebForContacts() {

        Contact.getInstance().email_list.addAll(mtd_contacts_phone());

        SharedPreferences sharedpreferences = activity.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);

        if (Contact.getInstance().email_list != null) {

            Contact.getInstance().ixpressemail.clear();
            Contact.getInstance().ixpressname.clear();
            Contact.getInstance().ixpress_user_pic.clear();

            RestClient.get(activity).PostPhoneContacts(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new ContactsReq(Contact.getInstance().email_list),
                    new Callback<ContactsResp>() {
                        @Override
                        public void success(ContactsResp contactsResp, Response response) {


                            if (contactsResp.getCode().equals("200")) {

                                for (int i = 0; i < contactsResp.getData().length; i++) {

                                    Contact.getInstance().ixpressemail.add(contactsResp.getData()[i].getEmail_id().trim());
                                    Contact.getInstance().ixpressname.add(contactsResp.getData()[i].getUser_name().trim());
                                    Contact.getInstance().ixpress_user_pic.add(contactsResp.getData()[i].getProfile_image().trim());

                                }


                                Map<String, String> map = new TreeMap<>(Contact.getInstance().email_name);
                                Contact.getInstance().ixpressemail.addAll(map.values());
                                Contact.getInstance().ixpressname.addAll(map.keySet());
                                Contact.getInstance().ixpress_user_pic.addAll(Contact.getInstance().contact_urilist);



                            }  else {
                                Contact.getInstance().ixpressemail.addAll(Contact.getInstance().email_name.values());
                                Contact.getInstance().ixpressname.addAll(Contact.getInstance().email_name.keySet());
                                Contact.getInstance().ixpress_user_pic.addAll(Contact.getInstance().contact_urilist);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(activity, "Contacts Sync failed miserably..  ", Toast.LENGTH_SHORT).show();

                            Contact.getInstance().ixpressemail.addAll(Contact.getInstance().email_name.values());
                            Contact.getInstance().ixpressname.addAll(Contact.getInstance().email_name.keySet());
                            Contact.getInstance().ixpress_user_pic.addAll(Contact.getInstance().contact_urilist);
                        }
                    });
        }

        else {
            Contact.getInstance().ixpressemail.addAll(Contact.getInstance().email_name.values());
            Contact.getInstance().ixpressname.addAll(Contact.getInstance().email_name.keySet());
            Contact.getInstance().ixpress_user_pic.addAll(Contact.getInstance().contact_urilist);
        }

    }

    public HashMap<String,String> mtd_email(){
    ContentResolver cer = context.getContentResolver();

    Cursor cur = cer.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    if (cur.getCount() > 0) {

        while (cur.moveToNext()) {
            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor cur1 = cer.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);
            while (cur1.moveToNext()) {

                String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String emails = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                if (emails != null && emails.length() > 5) {

                    if (name != null && !name.contains("@")) {

                        Contact.getInstance().email_name.put(StringUtils.abbreviate(name, 18), emails.trim().toLowerCase());

                    } else {
                        String val[] = name.split("@");

                        Contact.getInstance().email_name.put(StringUtils.abbreviate(val[0], 18), emails.trim().toLowerCase());
                    }
                }


            }
            cur1.close();
        }    }
         cur.close();

        return Contact.getInstance().email_name;
}


}
