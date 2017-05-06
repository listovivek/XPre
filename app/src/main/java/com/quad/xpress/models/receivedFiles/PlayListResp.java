package com.quad.xpress.models.receivedFiles;

/**
 * Created by Venkatesh on 15-06-16.
 */
public class PlayListResp {
    private String status;

    private PlayListData data;

    private String code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PlayListData getData() {
        return data;
    }

    public void setData(PlayListData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", data = " + data + ", code = " + code + "]";
    }
}
