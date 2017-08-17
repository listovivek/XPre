package com.quad.xpress.contacts;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Entity that represents a contact
 */
public class Contact {

    private static Contact instance = null;

    public enum Color { RED, AMBER, GREEN };


    public static Contact getInstance(){
        if(instance == null) {
            instance = new Contact();
        }
        return instance;
    }

    public Uri profilePic;
    public String name;
    public String xpressUser;
    public ArrayList<Boolean> is_ixpress_user = new ArrayList<>();
    public HashMap<String,String> email_name = new HashMap<>();
    public ArrayList<String> ixpressname = new ArrayList<>();
    public ArrayList<String> ixpress_user_pic = new ArrayList<>();
    public ArrayList<String> ixpressemail = new ArrayList<>();
    public ArrayList<String> ixpressemail_DB = new ArrayList<>();
    public HashMap<String, String> ContactPairs=new HashMap<String, String>();
    public ArrayList<String> phone_list = new ArrayList<>();
    public ArrayList<String> email_list = new ArrayList<>();
    public ArrayList<String> contact_namelist = new ArrayList<>();
    public ArrayList<String> contact_urilist = new ArrayList<>();



}
