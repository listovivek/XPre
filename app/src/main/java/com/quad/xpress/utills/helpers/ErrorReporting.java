package com.quad.xpress.utills.helpers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Venkatesh on 06-06-16.
 */
public class ErrorReporting {
    Activity _context;

    public ErrorReporting(Activity _context) {
        this._context = _context;
    }

    public void SendMail(String MailSubject, String Error) {
        final Intent fintent = new Intent(Intent.ACTION_SEND);
        fintent.setType("plain/text");
        try {
            Log.v("Exception", "try ");
            fintent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        } catch (Exception e) {
            Log.v("Exception", "Exception " + e);
        } finally {
            Log.v("Exception", "final ");
            fintent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Kural.ms@claritaz.com"});
            fintent.putExtra(Intent.EXTRA_SUBJECT, MailSubject);
            fintent.putExtra(Intent.EXTRA_TEXT, Error);
            fintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(Intent.createChooser(fintent, "Send this Error"));
        }
    }

}
