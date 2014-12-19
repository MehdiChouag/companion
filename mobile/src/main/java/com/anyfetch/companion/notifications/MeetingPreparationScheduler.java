package com.anyfetch.companion.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;

/**
 * Schedules the automatic event notifications (alarms
 */
public class MeetingPreparationScheduler {
    public static final String ARG_EVENT = "event";
    private static final String PREF_REMINDER_MINUTES = "pref_minutes_before_reminder";
    private final Context mContext;
    private final AlarmManager mAlarmManager;

    /**
     * Constructs the scheduler with a context
     *
     * @param context The Android Context
     */
    public MeetingPreparationScheduler(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }


    /**
     * Schedules the next notification.
     * <strong>It relies on android services so it shouldn't be executed on the main thread</strong>
     */
    public void schedule() {
        clear();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int minutes = preferences.getInt(PREF_REMINDER_MINUTES, 60);

        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(mContext);
        try {
            EventsList events = request.loadDataFromNetwork();
            long now = System.currentTimeMillis();
            for (Event e : events) {
                long wakeUpTime = e.getStartDate().getTime() - 1000 * 60 * minutes;
                if (wakeUpTime > now) {
                    Intent intent = new Intent(mContext, MeetingPreparationReceiver.class);
                    intent.putExtra(ARG_EVENT, e);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
                    mAlarmManager.set(AlarmManager.RTC, wakeUpTime, pendingIntent);
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        Intent intent = new Intent(mContext, MeetingPreparationReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        mAlarmManager.cancel(sender);
    }
}
