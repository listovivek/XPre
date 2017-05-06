package com.quad.xpress.models.abuse_resp;

/**
 * Created by kural on 8/2/2016.
 */
//{"file_id":"57651e99a8ba6eef7b74ce0c","description":"testing the path","user_email":"sugandhi.n@quadrupleindia.com","file_type":"video"}

public class Abuse_Req {
    String user_email;
    String description;
    String file_type;
    String file_id;
    public Abuse_Req(String user_email, String description, String file_type, String file_id){
        this.user_email = user_email;
        this.description = description;
        this.file_type = file_type;
        this.file_id = file_id;
    }

}
