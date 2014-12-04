package com.anyfetch.companion.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;

/**
 * Receives a notification order and build it
 */
public class MeetingPreparationNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new BuildNotificationStackTask(context).execute((Event) intent.getParcelableExtra(MeetingPreparationAlarm.ARG_EVENT), null, null);
    }
}
