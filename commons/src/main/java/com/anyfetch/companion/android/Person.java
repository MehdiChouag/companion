package com.anyfetch.companion.android;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Contact/Attendee
 */
public class Person {
    private static final String[] CONTACT_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
    };
    private static final int PRJ_CON_ID = 0;
    private static final int PRJ_CON_NAME = 1;
    private static final int PRJ_CON_PHOTO = 2;
    private static final int PRJ_CON_THUMB = 3;

    private static final String[] PHONE_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.ADDRESS
    };
    private static final int PRJ_EMAIL_ADDRESS = 1;

    private static final String[] EMAIL_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    private static final int PRJ_PHONE_NUMBER = 1;

    private static final String[] JOB_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Organization.CONTACT_ID,
            ContactsContract.CommonDataKinds.Organization.COMPANY,
            ContactsContract.CommonDataKinds.Organization.TITLE,
    };
    private static final int PRJ_JOB_COMPANY = 1;
    private static final int PRJ_JOB_TITLE = 2;

    private final String mName;
    private final String mJob;
    private final String mCompany;
    private final List<String> mEmails;
    private final List<String> mNumbers;
    private final String mPhotoUri;

    private static Person personFromCursor(Context context, Cursor cur, boolean thumb) {
        if(cur.getCount() < 1) {
            return null;
        }
        ContentResolver cr = context.getContentResolver();
        int contactId = cur.getInt(PRJ_CON_ID);
        String photoUri;
        if(thumb) {
            photoUri = cur.getString(PRJ_CON_THUMB);
        } else {
            photoUri = cur.getString(PRJ_CON_PHOTO);
        }
        Cursor emCur = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                EMAIL_PROJECTION,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                null,
                null);
        emCur.moveToFirst();
        List<String> emails = new ArrayList<String>();
        for (int i = 0; i < emCur.getCount(); i++) {
            emails.add(emCur.getString(PRJ_EMAIL_ADDRESS));
            emCur.moveToNext();
        }
        Cursor phCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONE_PROJECTION,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                null,
                null);
        phCur.moveToFirst();
        List<String> numbers = new ArrayList<String>();
        for (int i = 0; i < phCur.getCount(); i++) {
            numbers.add(emCur.getString(PRJ_PHONE_NUMBER));
            phCur.moveToNext();
        }
        String title = "";
        String company = "";
        Cursor jobCur = cr.query(
                ContactsContract.Data.CONTENT_URI,
                JOB_PROJECTION,
                ContactsContract.CommonDataKinds.Organization.CONTACT_ID + "=" + contactId,
                null,
                null);
        if(jobCur.getCount() > 0) {
            title = jobCur.getString(PRJ_JOB_TITLE);
            company = jobCur.getString(PRJ_JOB_COMPANY);
        }
        jobCur.moveToFirst();
        return new Person(
                cur.getString(PRJ_CON_NAME),
                title,
                company,
                emails,
                numbers,
                photoUri
        );
    }

    /**
     * Retrieve a person from their id
     * @param context The context to fetch from
     * @param id Their id
     * @param thumb Fetch the thumb instead of the full image
     * @return The person
     */
    public static Person getPerson(Context context, int id, boolean thumb) {
        ContentResolver cr = context.getContentResolver();
        Cursor perCur = cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                CONTACT_PROJECTION,
                ContactsContract.Contacts._ID + "=" + id,
                null,
                null);
        perCur.moveToFirst();
        return personFromCursor(context, perCur, thumb);
    }

    /**
     * Retrieve a person from one of their email address
     * @param context The context to fetch from
     * @param email Their email
     * @param thumb Fetch the thumb instead of the full image
     * @return The person
     */
    public static Person getPerson(Context context, String email, boolean thumb) {
        ContentResolver cr = context.getContentResolver();
        Cursor emCur = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                EMAIL_PROJECTION,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "='" + email + "'",
                null,
                null);
        emCur.moveToFirst();
        if(emCur.getCount() < 1) {
            return null;
        }
        return getPerson(context, emCur.getInt(PRJ_CON_ID), thumb);
    }

    /**
     * Creates a new Person
     * @param name Their name
     * @param job Their job name
     * @param company Their company
     * @param emails Their emails
     * @param numbers Their phone numbers
     * @param photoUri Their photoUri
     */
    public Person(String name, String company, String job, List<String> emails, List<String> numbers, String photoUri) {
        mName = name;
        mCompany = company;
        mJob = job;
        mEmails = emails;
        mNumbers = numbers;
        mPhotoUri = photoUri;
    }

    /**
     * Gets the name
     * @return A name
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the job
     * @return A job title
     */
    public String getJob() {
        return mJob;
    }

    /**
     * Gets the company
     * @return A company name
     */
    public String getCompany() {
        return mCompany;
    }

    /**
     * Gets the emails
     * @return A list of emails
     */
    public List<String> getEmails() {
        return mEmails;
    }

    /**
     * Gets the phone numbers
     * @return A list of numbers
     */
    public List<String> getNumbers() {
        return mNumbers;
    }

    /**
     * Gets the image uri
     * @return An URI
     */
    public String getPhotoUri() {
        return mPhotoUri;
    }
}
