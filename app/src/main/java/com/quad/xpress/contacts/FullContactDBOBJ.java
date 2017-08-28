package com.quad.xpress.contacts;

/**
 * Created by kural on 8/19/17.
 */

public class FullContactDBOBJ {

        //private variables
        int _id;
        String _name;
        String _phone_number;
        String _profile_pic;
        String _ixprezuser;

        // Empty constructor
        public FullContactDBOBJ(){

        }
        // constructor
        public FullContactDBOBJ(int id, String name, String _phone_number, String _profile_pic,String _ixprezuser){
            this._id = id;
            this._name = name;
            this._phone_number = _phone_number;
            this._profile_pic =_profile_pic;
            this._ixprezuser = _ixprezuser;
        }

        // constructor
        public FullContactDBOBJ(String name, String _phone_number,String _profile_pic,String _ixprezuser){
            this._name = name;
            this._phone_number = _phone_number;
            this._profile_pic= _profile_pic;
            this._ixprezuser = _ixprezuser;
        }



    // getting ID
        public int getID(){
            return this._id;
        }

        // setting id
        public void setID(int id){
            this._id = id;
        }

        // getting name
        public String getName(){
            return this._name;
        }

        // setting name
        public void setName(String name){
            this._name = name;
        }

        // getting phone number
        public String getPhoneNumber(){
            return this._phone_number;
        }

        // setting phone number
        public void setPhoneNumber(String phone_number){
            this._phone_number = phone_number;
        }

    public String get_profile_pic() {
        return _profile_pic;
    }

    public void set_profile_pic(String _profile_pic) {
        this._profile_pic = _profile_pic;
    }
    public void set_ixprezuser(String _ixprezuser) {
        this._ixprezuser = _ixprezuser;
    }

    public String get_ixprezuser() {
        return _ixprezuser;
    }
}
