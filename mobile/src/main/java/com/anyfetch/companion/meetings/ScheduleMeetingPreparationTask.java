package com.anyfetch.companion.meetings;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.anyfetch.companion.meetings.MeetingPreparationScheduler;

/**
 * Asynchronously schedule a next meeting notification
 */
public class ScheduleMeetingPreparationTask extends AsyncTask<Object, Object, Object> {
    private final Context mContext;

    public ScheduleMeetingPreparationTask(Context context) {
        mContext = context;
    }

    @Override
    protected Object doInBackground(Object... params) {
        if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("prefNotifyForEvents", true)) {
            new MeetingPreparationScheduler(mContext).schedule();
        }

        return null;
    }
}
