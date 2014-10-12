package com.anyfetch.companion.commons.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;

/**
 * Sets alarms for the upcoming meetings
 */
public class MeetingPreparationAlarm extends BroadcastReceiver {
    public static final String ARG_EVENT = "event";

    /**
     * Sets an alarm for an event
     *
     * @param event The event to remind
     */
    public static void setForEvent(Context context, Event event) {
        clearAlarm(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int minutes = preferences.getInt("minutesBeforeReminder", 60);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MeetingPreparationAlarm.class);
        intent.putExtra(ARG_EVENT, event);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, event.getStartDate().getTime() + 1000 * 60 * minutes, pendingIntent);
    }

    private static void clearAlarm(Context context) {
        Intent intent = new Intent(context, MeetingPreparationAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Broadcast a notification
        Event event = intent.getParcelableExtra(ARG_EVENT);
        Intent i = new Intent();
        i.setAction("com.anyfetch.companion.SHOW_NOTIFICATION");
        i.putExtra(ARG_EVENT, event);
        context.sendBroadcast(i);

        // Schedule the next alarm
        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(context);
        try {
            EventsList events = request.loadDataFromNetwork();
            if (events.size() > 1) {
                setForEvent(context, events.get(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
