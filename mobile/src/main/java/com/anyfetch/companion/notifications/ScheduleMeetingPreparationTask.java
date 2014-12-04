package com.anyfetch.companion.notifications;

import android.content.Context;
import android.os.AsyncTask;

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
        new MeetingPreparationScheduler(mContext).sched();
        return null;
    }
}
