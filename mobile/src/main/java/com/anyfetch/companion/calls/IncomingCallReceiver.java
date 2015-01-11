package com.anyfetch.companion.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;

public class IncomingCallReceiver extends BroadcastReceiver {
    private static PhoneStateListener stateListener;

    public void onReceive(final Context context, Intent intent) {
        // Only register listener when the user asked to be notified on calls
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefNotifyForCalls", true)) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                if (stateListener == null) {
                    // For some weird reasons, the Receiver is registered multiple time on Android.
                    // We need to ensure we only register once using a static-hack
                    // See http://stackoverflow.com/questions/16016603/phone-state-listener-called-multiple-times
                    stateListener = new android.telephony.PhoneStateListener() {
                        private String latestIncomingNumber = "";

                        @Override
                        public void onCallStateChanged(int state, String incomingNumber) {
                            // Sometimes, Android does not send the incomingNumber (garbage collect, race condition, whatever...)
                            // In such case, we have to restore the latest known number (possibly incorrect) to ensure notification is dismissed.
                            if(incomingNumber.isEmpty() && state == TelephonyManager.CALL_STATE_IDLE) {
                                incomingNumber = latestIncomingNumber;
                            }

                            if(incomingNumber.isEmpty()) {
                                Log.i("CallIncoming", state + "   with empty incoming number");
                                return;
                            }

                            latestIncomingNumber = incomingNumber;

                            Log.i("CallIncoming", state + "   incoming no:" + incomingNumber);
                            final Person contact = Person.getPersonByPhone(context, incomingNumber);

                            if (state == TelephonyManager.CALL_STATE_RINGING) {
                                // Someone is calling us
                                if (contact == null) {
                                    Log.i("CallIncoming", "Incoming call caught, but unable to generate context for " + incomingNumber);
                                } else {
                                    Log.i("CallIncoming", "Incoming call caught: " + contact.getName());
                                    new BuildNotificationStackTask(context).execute(contact, null, null);
                                }
                            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                                if (contact != null) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                                    int timeBeforeDismiss = Integer.parseInt(prefs.getString("prefDismissNotification", "4000"));

                                    if(timeBeforeDismiss == -1) {
                                        // Don't discard notification
                                        return;
                                    }

                                    Log.i("CallHangUp", "Call ended, notification for " + contact.getName() + " will be dismissed in " + timeBeforeDismiss + "ms.");
                                    // Clean up previous notification after a small delay
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManagerCompat.from(context).cancel(contact.getHashCode() - 1);
                                            Log.i("CallHangUp", "Dismissed notification for " + contact.getName());
                                        }
                                    }, timeBeforeDismiss);

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
}
