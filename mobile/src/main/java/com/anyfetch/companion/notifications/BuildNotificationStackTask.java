package com.anyfetch.companion.notifications;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
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


        int count = 1;
        for (Notification subNotif : builder.buildSubs()) {
            mManager.notify(id + count, subNotif);
            count++;
        }

        mManager.notify(id, builder.build());
    }

}
