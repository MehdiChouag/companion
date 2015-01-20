package com.anyfetch.companion.notifications;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.anyfetch.companion.commons.api.builders.ContextualObject;

/**
 * Build a notification with a list of associated documents and sub-contexts
 * First, instantly display a "stub" notification ; then display documents once loaded
 */
public class BuildNotificationStackTask extends AsyncTask<ContextualObject, Object, Object> {
    private final Context mContext;
    private final NotificationManagerCompat mManager;

    public BuildNotificationStackTask(Context context) {
        mContext = context;
        mManager = NotificationManagerCompat.from(mContext);
    }

    @Override
    protected Object doInBackground(ContextualObject[] params) {
        for (ContextualObject param : params) {
            try {
                if (param != null) {
                    buildNotificationStack(param);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void buildNotificationStack(ContextualObject contextualObject) throws Exception {
        int id = contextualObject.getHashCode();

        ContextNotificationBuilder builder = new ContextNotificationBuilder(mContext)
                .setContextualObject(contextualObject);

        // Instantly display the handheld device's notifications
        mManager.notify(id - 1, builder.buildSummary());
        // And for the wear, display a "stub" with basic functionality -- we'll load the document context after
        mManager.notify(id, builder.buildWearPlaceholder());

        if (builder.getContextualObjectDocuments().size() == 0) {
            // No results; no need for a notification
            Log.i("Notification", "Not displaying empty notification for " + contextualObject.getTitle());
            mManager.cancel(id - 1);
            mManager.cancel(id);

            return;
        }

        Log.i("Notification", "Displaying notification for " + contextualObject.getTitle());

        // Also build sub context pages
        int count = 1;
        for (Notification subNotif : builder.buildSubcontextWearNotifications()) {
            mManager.notify(id + count, subNotif);
            count++;
        }

        // And main context
        mManager.notify(id, builder.buildWearNotification());
    }

}
