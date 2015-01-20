package com.anyfetch.companion.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class TelephonyReceiver extends BroadcastReceiver {
    private static PhoneStateListener stateListener;
    private MixpanelAPI mixpanel = null;

    public void onReceive(final Context context, Intent intent) {
        if (mixpanel == null) {
            mixpanel = MixPanel.getInstance(context);
        }

        // Only register listener when the user asked to be notified on calls
        if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefNotifyForCalls", true)) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        String state = bundle.getString(TelephonyManager.EXTRA_STATE, "");
        String incomingNumber = bundle.getString("incoming_number", "");

        if (incomingNumber.isEmpty()) {
            Log.d("TelephonyState", "Got state " + state + " with empty number, discarded.");
            return;
        }

        Log.i("TelephonyState", "Got state " + state + ", incoming number: " + incomingNumber);

        final Person contact = Person.getPersonByPhone(context, incomingNumber);

        if (contact == null) {
            Log.i("TelephonyState", "Unknown contact, discarded.");
            return;
        }

        if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
            // Someone is calling us
            Log.i("TelephonyState", "Incoming call caught: " + contact.getName());

            mixpanel.getPeople().increment("IncomingCallCount", 1);
            mixpanel.getPeople().increment("CallCount", 1);
            mixpanel.flush();

            new BuildNotificationStackTask(context).execute(contact, null, null);
        } else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // We are calling someone
            Log.i("TelephonyState", "Outgoing call caught: " + contact.getName());

            mixpanel.getPeople().increment("OutgoingCallCount", 1);
            mixpanel.getPeople().increment("CallCount", 1);
            mixpanel.flush();

            new BuildNotificationStackTask(context).execute(contact, null, null);
        } else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            int timeBeforeDismiss = Integer.parseInt(prefs.getString("prefDismissNotification", "4000"));

            if (timeBeforeDismiss == -1) {
                // Don't discard notification
                return;
            }

            Log.i("TelephonyState", "Call ended, notification for " + contact.getName() + " will be dismissed in " + timeBeforeDismiss + "ms.");
            // Clean up previous notification after a small delay
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NotificationManagerCompat.from(context).cancel(contact.getHashCode() - 1);
                    Log.i("TelephonyState", "Dismissed notification for " + contact.getName());
                }
            }, timeBeforeDismiss);

        }
    }
}
