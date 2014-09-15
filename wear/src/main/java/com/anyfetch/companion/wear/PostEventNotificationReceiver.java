package com.anyfetch.companion.wear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anyfetch.companion.commons.models.Event;
import com.anyfetch.companion.commons.models.demo.DemoEvent;

public class PostEventNotificationReceiver extends BroadcastReceiver {
    public static final String CONTENT_KEY = "eventId";

    public PostEventNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String eventId = intent.getStringExtra(CONTENT_KEY);
        Event event;
        if(eventId.equals("demo")) {
            event = new DemoEvent(context);
        } else {
            // TODO: Handle real data
            event = null;
        }

        Intent prepareIntent = new Intent(context, EventGridActivity.class);
        prepareIntent.putExtra(CONTENT_KEY, eventId);
        PendingIntent preparePendingIntent = PendingIntent.getActivity(context, 0, prepareIntent, 0);

        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_generic);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .extend(new Notification.WearableExtender().setBackground(background))
                .setContentText(event.getTitle())
                // TODO: Better date formatting
                .setContentTitle(context.getString(R.string.title_notification_upcoming_event) + ", " + event.getStart().getHours() + ":" + event.getStart().getMinutes())
                .addAction(R.drawable.ic_prepare, context.getString(R.string.prepare_action), preparePendingIntent)
                .build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
    }
}