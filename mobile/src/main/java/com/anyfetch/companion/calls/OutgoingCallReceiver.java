package com.anyfetch.companion.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;

public class OutgoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d(OutgoingCallReceiver.class.getSimpleName(), intent.toString() + ", call to: " + phoneNumber);

        Person contact = Person.getPersonByPhone(context, phoneNumber);

        if (contact == null) {
            Log.i("Outgoing", "Outgoing call caught, but unable to generate context for " + phoneNumber);
        }
        else {
            Log.i("Outgoing", "Outgoing call caught: " + contact.getName());
            new BuildNotificationStackTask(context).execute(contact, null, null);
        }
    }
}
