package com.quad.xpress.Contacts;

import android.net.Uri;

import java.util.ArrayList;

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

    public ArrayList<String> ixpressname = new ArrayList<>();
    public ArrayList<String> ixpress_user_pic = new ArrayList<>();
    public ArrayList<String> ixpressemail = new ArrayList<>();


    public ArrayList<String> email_list = new ArrayList<>();
    public ArrayList<String> contact_namelist = new ArrayList<>();
    public ArrayList<String> contact_urilist = new ArrayList<>();



}
