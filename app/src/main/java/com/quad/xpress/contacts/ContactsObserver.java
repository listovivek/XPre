package com.quad.xpress.contacts;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by kural on 8/17/17.
 */

public class ContactsObserver extends ContentObserver {

    public ContactsObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

       // Log.e("change", "contact");



    }

}
