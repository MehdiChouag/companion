package com.anyfetch.companion.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class OutgoingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Skip notification if user unchecked the settings
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefNotifyForCalls", true)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(OutgoingCallReceiver.class.getSimpleName(), intent.toString() + ", call to: " + phoneNumber);

            Person contact = Person.getPersonByPhone(context, phoneNumber);

            if (contact == null) {
                Log.i("CallOutgoing", "Outgoing call caught, but unable to generate context for " + phoneNumber);
            } else {
                Log.i("CallOutgoing", "Outgoing call caught: " + contact.getName());
                new BuildNotificationStackTask(context).execute(contact, null, null);

                MixpanelAPI mixpanel = MixPanel.getInstance(context);
                mixpanel.getPeople().increment("OutgoingCallCount", 1);
                mixpanel.getPeople().increment("CallCount", 1);
                mixpanel.flush();
            }
        }
    }
}
