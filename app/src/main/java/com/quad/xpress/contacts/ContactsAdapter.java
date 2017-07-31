package com.quad.xpress.contacts;

import android.content.ContentUris;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quad.xpress.R;

/**
 *
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private Cursor mCursor;
    private final int mNameColIdx, mIdColIdx;
    private String emailID;
    private Contact mContact;

    public ContactsAdapter(Cursor cursor, Contact c) {
        mCursor = cursor;
        mNameColIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        mIdColIdx = cursor.getColumnIndex(ContactsContract.Contacts._ID);

        mContact = c;
        //emailID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int pos) {

        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);

        return new ContactViewHolder(listItemView, mCursor);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int pos) {
        // Extract info from cursor

            mCursor.moveToPosition(pos);
            String contactName = mCursor.getString(mNameColIdx);
            long contactId = mCursor.getLong(mIdColIdx);
            String email = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            Log.d("email", email);

            String emailIdOfContact = mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            Log.d("email", emailIdOfContact);
            // Create contact model and bind to viewholder
            Contact c = new Contact();
            c.name = contactName;
           // c.emailID = email;
            c.profilePic = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            contactViewHolder.bind(c, pos);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}