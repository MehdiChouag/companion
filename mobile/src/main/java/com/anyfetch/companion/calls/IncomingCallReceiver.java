package com.anyfetch.companion.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;

public class IncomingCallReceiver extends BroadcastReceiver {
    private static PhoneStateListener stateListener;

    public void onReceive(final Context context, Intent intent) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            if(stateListener == null) {
                // For some weird reasons, the Receiver is registered multiple time on Android.
                // We need to ensure we only register once using a static-hack
                // See http://stackoverflow.com/questions/16016603/phone-state-listener-called-multiple-times
                stateListener = new android.telephony.PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        Log.d("Incoming", state + "   incoming no:" + incomingNumber);

                        if (state == TelephonyManager.CALL_STATE_RINGING) {
                            Person contact = Person.getPersonByPhone(context, incomingNumber);

                            if (contact == null) {
                                Log.i("Outgoing", "Ingoing call caught, but unable to generate context for " + incomingNumber);
                            }
                            else {
                                Log.i("Outgoing", "Ingoing call caught: " + contact.getName());
                                new BuildNotificationStackTask(context).execute(contact, null, null);
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
