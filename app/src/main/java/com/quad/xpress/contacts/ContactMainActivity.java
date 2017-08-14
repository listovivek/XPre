package com.quad.xpress.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.quad.xpress.DashBoard;
import com.quad.xpress.R;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.utills.helpers.FieldsValidator;
import com.quad.xpress.utills.helpers.SharedPrefUtils;
import com.quad.xpress.webservice.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ctl on 13/3/17.
 */

public class ContactMainActivity extends AppCompatActivity   {


    private ImageButton btn_back_tb;

    TabLayout tabLayout;

    ViewPager mViewPager;
    ArrayList<String> ixprez_email = new ArrayList<>();
    ArrayList<String> ixprez_username = new ArrayList<>();
    ArrayList<String> ixprez_profilepic = new ArrayList<>();
    ArrayList<String> appendEmail = new ArrayList<String>();
    ArrayList<String> appendName = new ArrayList<String>();
    ArrayList<String> appendProfilePic = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static ContactMainActivity mContactMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
        sharedpreferences = getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mContactMainActivity = ContactMainActivity.this;


        btn_back_tb = (ImageButton) findViewById(R.id.btn_back_contacts);
        btn_back_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mViewPager = (ViewPager) findViewById(R.id.contact_viewpager);
        setUpViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_private);
        tabLayout.setupWithViewPager(mViewPager);


    }

    private void setUpViewPager(ViewPager mViewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentXpressActivity(), "iXprez");
        adapter.addFragment(new FragmentRecentActivity(), "Recent");
        mViewPager.setAdapter(adapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
    private class ContactsReaderClass extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursorPhone;
            Integer counter;
            String phoneNumber = null;
            String email = null;
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
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
                        // Read every email id associated with the contact
                       /* Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));



                        }
                        emailCursor.close();*/
                    }
                    // Add the contact to the ArrayList
                   /* if(name !=null && email != null){
                        Contact.getInstance().email_primary.add(email);
                    }*/




                    if (name != null && phoneNumber != null && FieldsValidator.isPhoneNumberString(phoneNumber, true)) {

                        //phonecontactList.add(output.toString());

                        Contact.getInstance().email_list.add(phoneNumber);
                        Contact.getInstance().contact_namelist.add(name);
                        Contact.getInstance().contact_urilist.add(String.valueOf(R.drawable.ic_user_icon));
                        Contact.getInstance().ContactPairs.put(phoneNumber, name);


                        phoneNumber = null;



                    }

                }
            }

            ContentResolver cer = getContentResolver();

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
                        //   Log.e("Name :", name);
                        String emails = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        // names.add(name);
                        if(emails != null && emails.length() >5 ){

                               /* Contact.getInstance().email_primary.add(emails.trim());
                                Contact.getInstance().phone_list.add("kuralms@gmail.com".trim());*/
                            if(name!=null && !name.contains("@")){

                                Contact.getInstance().email_primary.add(name.trim()+" - "+emails.trim());}

                            else {
                                String val [] =  name.split("@");
                                Contact.getInstance().email_primary.add(val[0]+" - "+emails.trim());
                            }
                        }


                    }
                    cur1.close();
                }


            }

            cur.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(context, ""+ Contact.getInstance().email_primary, Toast.LENGTH_LONG).show();
            callwebForContacts();
        }
    }

    private void callwebForContacts() {


        //  Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();

        Contact.getInstance().ixpressemail.clear();
        Contact.getInstance().ixpressname.clear();
        Contact.getInstance().ixpress_user_pic.clear();



        ixprez_email.clear();
        ixprez_username.clear();
        ixprez_profilepic.clear();

        appendEmail.clear();
        appendName.clear();
        appendProfilePic.clear();


        //  Log.d("emailcons", Contact.getInstance().email_list.toString());

        if (Contact.getInstance().email_list != null) {

            RestClient.get(this).PostPhoneContacts(sharedpreferences.getString(SharedPrefUtils.SpToken, ""),new ContactsReq(Contact.getInstance().email_list),
                    new Callback<ContactsResp>() {
                        @Override
                        public void success(ContactsResp contactsResp, Response response) {

                            if (contactsResp.getCode().equals("200")) {

                                for (int i = 0; i < contactsResp.getData().length; i++) {

                                    ixprez_email.add(contactsResp.getData()[i].getEmail_id());
                                    ixprez_username.add(contactsResp.getData()[i].getUser_name());
                                    ixprez_profilepic.add(contactsResp.getData()[i].getProfile_image());

                                    Contact.getInstance().ixpressemail_DB.add(contactsResp.getData()[i].getEmail_id());

                                    //count=i++;
                                }

                                DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
                                List<String> contacts = handler.getAllDetailsFormDatabase();
                                DashBoard.ixemailcount = 0;
                                /*if(DatabaseHandler.dbEmailList!=null &&
                                        DatabaseHandler.dbEmailList.size()>0){
                                    appendEmail.addAll(DatabaseHandler.dbEmailList);
                                    appendName.addAll(DatabaseHandler.dbNameList);
                                    appendProfilePic.addAll(DatabaseHandler.dbPicList);
                                }*/

                                // ixemailcount = ixprez_email.size();

                                // ixprez user from service

                                appendEmail.addAll(ixprez_email);
                                appendName.addAll(ixprez_username);
                                appendProfilePic.addAll(ixprez_profilepic);

                                // internal contacts list

                                appendEmail.addAll(Contact.getInstance().email_list);
                                appendName.addAll(Contact.getInstance().contact_namelist);
                                appendProfilePic.addAll(Contact.getInstance().contact_urilist);

                                Contact.getInstance().ixpressname = appendName;
                                Contact.getInstance().ixpressemail = appendEmail;
                                Contact.getInstance().ixpress_user_pic = appendProfilePic;
                                Contact.getInstance().xpressUser = "iXpressUser";


                            } else if (contactsResp.getCode().equals("202")) {
                              /*  Toast.makeText(DashBoard.this, "ixpress users...found " +
                                        "in your contacts...", Toast.LENGTH_SHORT).show();*/
                                //  recyclerView.setAdapter(new ContactsAdapter(cur, null));

                            } else {
                              /*  Toast.makeText(DashBoard.this, "Hmm,.. " +
                                        "Something went wrong...", Toast.LENGTH_SHORT).show();*/
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //recyclerView.setAdapter(new ContactsAdapter(cur, null));
                        }
                    });
        } else {
           /* Toast.makeText(DashBoard.this, "Hmm,.. " +
                    "No records found...", Toast.LENGTH_SHORT).show();*/
        }
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}