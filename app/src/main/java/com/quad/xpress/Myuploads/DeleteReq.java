package com.quad.xpress.Myuploads;

/**
 * Created by kural on 9/27/2016.
 */

public class DeleteReq {

    String file_type;
    String id;

    public DeleteReq(String id , String file_type){
        this.id = id;
        this.file_type = file_type;

    }
}
