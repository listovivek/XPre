package com.quad.xpress.models.receivedFiles.search;

/**
 * Created by Venkatesh on 28-04-16.
 */
public class SearchFilesReq {
    String limit, index, tags,sort,user_email;

    public SearchFilesReq(String tags, String index, String limit, String sort, String user_email) {
        this.index = index;
        this.limit = limit;
        this.tags = tags;
        this.sort = sort;
        this.user_email = user_email;
    }

    //{"tags":"sugu","index":"","limit":""}
}
