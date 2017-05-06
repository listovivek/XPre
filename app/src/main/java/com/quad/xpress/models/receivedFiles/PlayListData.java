package com.quad.xpress.models.receivedFiles;

/**
 * Created by Venkatesh on 15-06-16.
 */
public class PlayListData {
    private PlayListRecords[] Records;

    private String last;

    public PlayListRecords[] getRecords() {
        return Records;
    }

    public void setRecords(PlayListRecords[] Records) {
        this.Records = Records;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "ClassPojo [Records = " + Records + ", last = " + last + "]";
    }
}