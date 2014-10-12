package com.anyfetch.companion.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;

import com.anyfetch.companion.ContextActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.GetDocumentsListRequest;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.ui.ImageHelper;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Because a broadcast receiver is on the main thread ...
 */
public class BuildNotificationTask extends AsyncTask<Event, Object, Object> {
    private static final int WEAR_CONTEXT_SIZE = 5;
    private static final String GROUP_KEY_EVENT = "group_key_event_";

    private final Context mContext;
    private final OkHttpClient mClient = new OkHttpClient();
    private final NotificationManagerCompat mManager;

    public BuildNotificationTask(Context context) {
        mContext = context;
        mManager = NotificationManagerCompat.from(mContext);
    }

    @Override
    protected Object doInBackground(Event[] params) {
        for (Event param : params) {
            try {
                if (param != null) {
                    buildEventNotification(param);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void buildEventNotification(Event event) throws Exception {
        // Fetch context
        GetDocumentsListRequest request = new GetDocumentsListRequest(mContext, event.getTitle());
        request.setOkHttpClient(mClient);
        DocumentsList documents = request.loadDataFromNetwork();

        int notificationId = (int) event.getId();
        // Main Notification Intent
        Intent viewIntent = new Intent(mContext, ContextActivity.class);
        viewIntent.putExtra(ContextFragment.ARG_TYPE, ContextFragment.TYPE_EVENT);
        viewIntent.putExtra(ContextFragment.ARG_PARCELABLE, event);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(mContext, 0, viewIntent, 0);

        // Big View
        NotificationCompat.BigTextStyle bigView = new NotificationCompat.BigTextStyle();
        int attNumber = event.getAttendees().size();
        String location = event.getLocation();
        String bigViewText = "<b>" + event.getTitle() + "</b><br/>";
        if (location != null && !location.equals("")) {
            bigViewText += location + ", ";
        }
        if (attNumber == 1) {
            bigViewText += mContext.getString(R.string.single_attendee);
        } else {
            bigViewText += String.format(mContext.getString(R.string.multiple_attendees), attNumber);
        }
        bigView.bigText(Html.fromHtml(bigViewText));

        // Build the notification
        long minutesBefore = (event.getStartDate().getTime() - new Date().getTime()) / (1000 * 60);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(com.anyfetch.companion.commons.R.drawable.ic_launcher)
                        .setContentTitle(String.format(mContext.getString(R.string.minutes_before), minutesBefore))
                        .setContentIntent(viewPendingIntent)
                        .setStyle(bigView)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                mContext.getResources(), R.drawable.notif_background
                        ))
                        .setGroup(GROUP_KEY_EVENT + notificationId);

        // Wearable extender
        Notification notification =
                new NotificationCompat.WearableExtender()
                        .addPages(getPagesFromDocuments(documents))
                        .extend(builder)
                        .build();

        // Stack attendees
        for (Person attendee : event.getAttendees()) {
            mManager.notify((int) attendee.getId(), buildAttendeeNotification(notificationId, attendee));
        }

        // ... and fire it !
        mManager.notify(notificationId, notification);
    }

    private Notification buildAttendeeNotification(int notificationId, Person attendee) throws Exception {
        // Fetch context
        String query = "(" + attendee.getName() + ")";
        for (String email : attendee.getEmails()) {
            query += " OR (" + email + ")";
        }
        GetDocumentsListRequest request = new GetDocumentsListRequest(mContext, query);
        request.setOkHttpClient(mClient);
        DocumentsList documents = request.loadDataFromNetwork();


        // Big View
        NotificationCompat.BigTextStyle bigView = new NotificationCompat.BigTextStyle();
        String job = "";
        String jobTitle = attendee.getJob();
        String company = attendee.getCompany();
        if (jobTitle != null && !jobTitle.equals("")) {
            job += jobTitle + ", ";
        }
        if (company != null && !company.equals("")) {
            job += company;
        }
        bigView.bigText(Html.fromHtml("<b>" + attendee.getName() + "</b><br/>" + job));

        // Build the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(com.anyfetch.companion.commons.R.drawable.ic_launcher)
                        .setContentTitle(mContext.getString(R.string.event_s_attendee))
                        .setStyle(bigView)
                        .setLargeIcon(attendee.getThumb())
                        .setGroup(GROUP_KEY_EVENT + notificationId);

        // Wearable extender
        NotificationCompat.Builder extendedBuilder =
                new NotificationCompat.WearableExtender()
                        .addPages(getPagesFromDocuments(documents))
                        .extend(builder);

        return extendedBuilder.build();
    }

    private List<Notification> getPagesFromDocuments(DocumentsList documents) {
        List<Notification> pages = new ArrayList<Notification>();
        for (int i = 0; i < documents.size() && i < WEAR_CONTEXT_SIZE; i++) {
            Document document = documents.get(i);

            // Big View
            NotificationCompat.BigTextStyle bigView = new NotificationCompat.BigTextStyle();
            bigView.bigText(Html.fromHtml(
                    "<b>" + HtmlUtils.convertHlt(document.getTitle()) + "</b><br/>" +
                            HtmlUtils.stripHtml(HtmlUtils.selectTag(document.getSnippet(), "main"))
            ));

            // Standard View
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(mContext)
                            .setContentTitle(Html.fromHtml(HtmlUtils.stripHtml(HtmlUtils.selectTag(document.getSnippet(), "ul"))))
                            .setStyle(bigView)
                            .setSmallIcon(ImageHelper.matchResourceForProvider(document.getProvider()));
            pages.add(builder.build());
        }
        return pages;
    }
}
