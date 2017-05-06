package com.quad.xpress.models.registration;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class RegRequest {
    String user_name, email_id, phone_number, country, device_id, gcm_id, mobile_os, mobile_version, mobile_modelname,is_edit;
    String language,notification,remainder;

    public RegRequest(String user_name, String email_id, String phone_number, String country, String language, String device_id,
                      String gcm_id, String mobile_os, String mobile_version, String mobile_modelname,String is_edit,
                        String notification, String remainder) {

        this.user_name = user_name;
        this.email_id = email_id;
        this.phone_number = phone_number;
        this.country = country;
        this.language = language;
        this.device_id = device_id;
        this.gcm_id = gcm_id;
        this.mobile_os = mobile_os;
        this.mobile_version = mobile_version;
        this.mobile_modelname = mobile_modelname;
        this.is_edit =is_edit;
        this.notification=notification;
        this.remainder=remainder;
    }
}