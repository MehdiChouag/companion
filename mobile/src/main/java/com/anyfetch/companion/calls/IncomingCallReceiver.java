package com.anyfetch.companion.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;

import java.util.HashSet;
import java.util.Set;

public class IncomingCallReceiver extends BroadcastReceiver {
    public static String CALL_JOURNAL = "call_journal";

    private static PhoneStateListener stateListener;

    public void onReceive(final Context context, Intent intent) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            if (stateListener == null) {
                // For some weird reasons, the Receiver is registered multiple time on Android.
                // We need to ensure we only register once using a static-hack
                // See http://stackoverflow.com/questions/16016603/phone-state-listener-called-multiple-times
                stateListener = new android.telephony.PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        Log.d("Incoming", state + "   incoming no:" + incomingNumber);
                        Person contact;

                        if (state == TelephonyManager.CALL_STATE_RINGING) {
                            // Someone is calling us
                            contact = Person.getPersonByPhone(context, incomingNumber);

                            if (contact == null) {
                                Log.i("Outgoing", "Ingoing call caught, but unable to generate context for " + incomingNumber);
                            } else {
                                Log.i("Outgoing", "Ingoing call caught: " + contact.getName());
                                new BuildNotificationStackTask(context).execute(contact, null, null);
                            }
                        }

                        if (state == TelephonyManager.CALL_STATE_IDLE) {
                            contact = Person.getPersonByPhone(context, incomingNumber);

                            if (contact != null) {
                                // Clean up previous notification
                                SharedPreferences callSettings = context.getSharedPreferences("calls", Context.MODE_PRIVATE);
                                Set<String> currentCalls = callSettings.getStringSet(IncomingCallReceiver.CALL_JOURNAL, new HashSet<String>());
                                SharedPreferences.Editor editor = callSettings.edit();
                                currentCalls.remove(Long.toString(contact.getId()));
                                editor.putStringSet(IncomingCallReceiver.CALL_JOURNAL, currentCalls);
                                editor.commit();

                                NotificationManagerCompat.from(context).cancel(contact.getHashCode());
                            }
                        }
                    }
                };

                telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
