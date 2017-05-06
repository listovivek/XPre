package com.quad.xpress.models.send;

import android.net.Uri;

import java.io.File;

/**
 * Created by Venkatesh on 05-05-16.
 */
public class AVDetails {

    public static long getFileSize(Uri FileUri) {
        long DfileSize = new File(FileUri.getPath()).length() / 1024 / 1024;
        return DfileSize;
    }
}
