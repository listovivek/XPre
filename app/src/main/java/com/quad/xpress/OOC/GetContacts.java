package com.quad.xpress.OOC;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.quad.xpress.Contacts.Contact;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.webservice.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kural on 3/5/17.
 */

public class GetContacts {
    Activity activity;
    Context context;
    ArrayList<String> ixem =new ArrayList<>();
    ArrayList<String> ixusername =  new ArrayList<>();
    ArrayList<String> ixcontact = new ArrayList<>();

    List<GetContacts> getcont= new ArrayList<>();

    public GetContacts(ArrayList<String> ixem, ArrayList<String> ixusername, ArrayList<String> ixcontact){
        this.ixem = ixem;
        this.ixusername=ixusername;
        this.ixcontact=ixcontact;

    }


    public ArrayList<String> getIxcontact() {
        return ixcontact;
    }

    public void setIxcontact(ArrayList<String> ixcontact) {
        this.ixcontact = ixcontact;
    }

    public ArrayList<String> getIxem() {
        return ixem;
    }

    public void setIxem(ArrayList<String> ixem) {
        this.ixem = ixem;
    }

    public ArrayList<String> getIxusername() {
        return ixusername;
    }

    public void setIxusername(ArrayList<String> ixusername) {
        this.ixusername = ixusername;
    }

    private List<GetContacts> callwebForContacts(Activity activity, final ArrayList<String> ixprez_email, final ArrayList<String>ixprez_username, final ArrayList<String> ixprez_profilepic) {

        ixprez_email.clear();
        ixprez_username.clear();
        ixprez_profilepic.clear();



        Log.d("email contactttss", Contact.getInstance().email_list.toString());

        if (Contact.getInstance().email_list != null) {

            RestClient.get(activity).PostContacts(new ContactsReq(Contact.getInstance().email_list),
                    new Callback<ContactsResp>() {
                        @Override
                        public void success(ContactsResp contactsResp, Response response) {

                            if (contactsResp.getCode().equals("200")) {

                                for (int i = 0; i < contactsResp.getData().length; i++) {

                                    ixprez_email.add(contactsResp.getData()[i].getEmail_id());
                                    ixprez_username.add(contactsResp.getData()[i].getUser_name());
                                    ixprez_profilepic.add(contactsResp.getData()[i].getProfile_image());

                                    //count=i++;
                                }
                        ixem = ixprez_email;
                        ixusername = ixprez_username;
                        ixcontact = ixprez_profilepic;

                              /*  DatabaseHandler handler = new DatabaseHandler(activity);
                                List<String> contacts = handler.getAllDetailsFormDatabase();
                                ixemailcount = 0;
                                *//*if(DatabaseHandler.dbEmailList!=null &&
                                        DatabaseHandler.dbEmailList.size()>0){
                                    appendEmail.addAll(DatabaseHandler.dbEmailList);
                                    appendName.addAll(DatabaseHandler.dbNameList);
                                    appendProfilePic.addAll(DatabaseHandler.dbPicList);
                                }*//*

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
*/

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

    return getcont;
    }

}
