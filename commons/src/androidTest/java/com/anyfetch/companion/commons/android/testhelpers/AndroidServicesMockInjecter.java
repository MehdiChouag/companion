package com.anyfetch.companion.commons.android.testhelpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import com.anyfetch.companion.commons.android.pojo.Event;

import java.util.Calendar;
import java.util.Date;

public class AndroidServicesMockInjecter {
    public static long injectContact(Context context) throws InterruptedException {
        ContentResolver cr = context.getContentResolver();

        ContentValues contact = new ContentValues();
        contact.put(ContactsContract.RawContacts.ACCOUNT_TYPE, (String) null);
        contact.put(ContactsContract.RawContacts.ACCOUNT_NAME, (String) null);
        Uri uri = cr.insert(ContactsContract.RawContacts.CONTENT_URI, contact);

        long contactId = Long.parseLong(uri.getLastPathSegment());
        Thread.sleep(500);

        ContentValues name = new ContentValues();
        name.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        name.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        name.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Sterling Archer");
        cr.insert(ContactsContract.Data.CONTENT_URI, name);

        ContentValues phone = new ContentValues();
        phone.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        phone.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        phone.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "000");
        cr.insert(ContactsContract.Data.CONTENT_URI, phone);

        ContentValues emailWork = new ContentValues();
        emailWork.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        emailWork.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        emailWork.put(ContactsContract.CommonDataKinds.Email.DATA, "sterling@example.com");
        emailWork.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        cr.insert(ContactsContract.Data.CONTENT_URI, emailWork);

        ContentValues emailHome = new ContentValues();
        emailHome.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        emailHome.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        emailHome.put(ContactsContract.CommonDataKinds.Email.DATA, "sarcher@example.com");
        emailHome.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        cr.insert(ContactsContract.Data.CONTENT_URI, emailHome);

        ContentValues org = new ContentValues();
        org.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        org.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
        org.put(ContactsContract.CommonDataKinds.Organization.COMPANY, "Example");
        org.put(ContactsContract.CommonDataKinds.Organization.TITLE, "Secret Agent");
        org.put(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK);
        cr.insert(ContactsContract.Data.CONTENT_URI, org);

        Thread.sleep(3000);
        return contactId;
    }

    public static long injectEvent(Context context) throws InterruptedException {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        ContentResolver cr = context.getContentResolver();

        ContentValues calendar = new ContentValues();
        calendar.put(CalendarContract.Calendars.NAME, "BobCal");
        Uri calUri = cr.insert(CalendarContract.Calendars.CONTENT_URI, calendar);

        long calId = Long.parseLong(calUri.getLastPathSegment());
        Thread.sleep(500);

        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
        event.put(CalendarContract.Events.TITLE, "Secret Briefing");
        event.put(CalendarContract.Events.DESCRIPTION, "Not Disclosed");
        event.put(CalendarContract.Events.DTSTART, now.getTimeInMillis() + Event.MINUTE);
        event.put(CalendarContract.Events.DTEND, now.getTimeInMillis() + Event.HOUR);
        event.put(CalendarContract.Events.EVENT_LOCATION, "A Secret Place");
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "America/New_York");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, event);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        Thread.sleep(500);

        ContentValues att0 = new ContentValues();
        att0.put(CalendarContract.Attendees.EVENT_ID, eventID);
        att0.put(CalendarContract.Attendees.ATTENDEE_NAME, "Sterling Archer");
        att0.put(CalendarContract.Attendees.ATTENDEE_EMAIL, "sterling@example.com");
        cr.insert(CalendarContract.Attendees.CONTENT_URI, att0);

        ContentValues att1 = new ContentValues();
        att1.put(CalendarContract.Attendees.EVENT_ID, eventID);
        att1.put(CalendarContract.Attendees.ATTENDEE_NAME, "Malory Archer");
        att1.put(CalendarContract.Attendees.ATTENDEE_EMAIL, "malory@example.com");
        cr.insert(CalendarContract.Attendees.CONTENT_URI, att1);

        Thread.sleep(1500);
        return eventID;
    }
}
