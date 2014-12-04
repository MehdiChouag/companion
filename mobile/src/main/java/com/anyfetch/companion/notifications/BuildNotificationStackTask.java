package com.anyfetch.companion.notifications;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import com.anyfetch.companion.commons.api.builders.ContextualObject;

/**
 * Because a broadcast receiver is on the main thread ...
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
        ContextNotificationBuilder builder = new ContextNotificationBuilder(mContext)
                .setContextualObject(contextualObject);

        int id = contextualObject.getTitle().hashCode();
        for (Notification subNotif : builder.buildSubs()) {
            mManager.notify(id + subNotif.hashCode(), subNotif);
        }
        mManager.notify(id, builder.build());
    }

}
