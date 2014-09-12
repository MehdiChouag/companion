package com.anyfetch.companion.wear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.models.Event;
import com.anyfetch.companion.commons.models.demo.DemoEvent;

public class PostEventNotificationReceiver extends BroadcastReceiver {
    public static final String CONTENT_KEY = "contentText";

    public PostEventNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent displayIntent = new Intent(context, DisplayEventNotificationActivity.class);
        String eventId = intent.getStringExtra(CONTENT_KEY);
        Event event;
        if(eventId.equals("demo")) {
            event = new DemoEvent();
        } else {
            // TODO: Handle real data
            event = null;
        }
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(event.getTitle())
                .extend(new Notification.WearableExtender()
                        .setDisplayIntent(PendingIntent.getActivity(context, 0, displayIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT)))
                .build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
    }
}