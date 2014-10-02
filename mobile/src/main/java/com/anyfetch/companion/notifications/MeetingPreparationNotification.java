package com.anyfetch.companion.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.anyfetch.companion.ContextActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.android.Event;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.anyfetch.companion.fragments.ContextFragment;

import java.util.Date;

/**
 * Receives a notification order and build it
 */
public class MeetingPreparationNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Event event = intent.getParcelableExtra(MeetingPreparationAlarm.ARG_EVENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = (int) event.getId();
        long minutesRemaining = (event.getStartDate().getTime() - new Date().getTime()) / (1000 * 60);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_prepare_your_meeting))
                .setContentText(String.format(context.getString(R.string.notification_remaining), minutesRemaining, event.getTitle()))
                .setSmallIcon(R.drawable.ic_launcher);

        Intent resultIntent = new Intent(context, ContextActivity.class);
        resultIntent.putExtra(ContextFragment.ARG_TYPE, ContextFragment.TYPE_EVENT);
        resultIntent.putExtra(ContextFragment.ARG_PARCELABLE, event);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ContextActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        manager.notify(id, builder.build());

    }
}
