package com.anyfetch.companion.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.anyfetch.companion.commons.android.pojo.Event;

/**
 * Receive a meeting preparation scheduled notification
 */
public class MeetingPreparationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Event event = intent.getParcelableExtra(MeetingPreparationScheduler.ARG_EVENT);
        new BuildNotificationStackTask(context).execute(event, null, null);
        new ScheduleMeetingPreparationTask(context).execute(null, null, null);
    }
}