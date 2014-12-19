package com.anyfetch.companion.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.anyfetch.companion.ContextActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.requests.GetDocumentsListRequest;
import com.anyfetch.companion.commons.ui.ImageHelper;
import com.anyfetch.companion.fragments.ContextFragment;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builder for creating contexts
 */
public class ContextNotificationBuilder {
    private static final int WEAR_CONTEXT_SIZE = 5;
    private final Context mContext;
    private final OkHttpClient mClient = new OkHttpClient();
    private ContextualObject mContextualObject;
    private Set<String> mTailedEmails;
    private String mGroupKey;

    /**
     * Setups the notification builder
     *
     * @param context The android app context
     */
    public ContextNotificationBuilder(Context context) {
        mContext = context;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mTailedEmails = prefs.getStringSet(BaseRequestBuilder.TAILED_EMAILS, new HashSet<String>());
    }

    /**
     * Sets the involved context object
     *
     * @param contextualObject A contextual object
     * @return A chainable builder
     */
    public ContextNotificationBuilder setContextualObject(ContextualObject contextualObject) {
        mContextualObject = contextualObject;

        mGroupKey = Integer.toString(contextualObject.getSearchQuery(mTailedEmails).hashCode());

        return this;
    }

    /**
     * Overrides the group key <strong>To be used after setting the contextual object</strong>
     *
     * @param groupKey The notification group key
     * @return A chainable builder
     */
    public ContextNotificationBuilder setGroupKey(String groupKey) {
        mGroupKey = groupKey;
        return this;
    }

    /**
     * Builds the notification. <strong>This  method shouldn't be called from the main thread</strong>
     *
     * @return A Context Notification
     */
    public Notification build() {
        Intent viewIntent = new Intent(mContext, ContextActivity.class);
        viewIntent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, mContextualObject);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(mContext, mContextualObject.getTitle().hashCode(), viewIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setLargeIcon(ImageHelper.toBitmap(mContextualObject.getIcon(mContext)))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(mContextualObject.getTitle())
                .setContentText(mContextualObject.getInfo())
                .setContentIntent(viewPendingIntent)
                .setColor(mContext.getResources().getColor(R.color.primary))
                .setGroup(mGroupKey);

        try {
            NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                    .addPages(buildPages());
            return extender.extend(builder).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the sub-stacked notifications. <strong>This  method shouldn't be called from the main thread</strong>
     *
     * @return A List of Context Notification
     */
    public List<Notification> buildSubs() {
        List<Notification> notifs = new ArrayList<Notification>();
        List<ContextualObject> subContexts = mContextualObject.getSubContexts(mTailedEmails);
        if (subContexts != null) {
            for (ContextualObject subContext : subContexts) {
                notifs.add(new ContextNotificationBuilder(mContext)
                        .setContextualObject(subContext)
                        .setGroupKey(mGroupKey)
                        .build());
            }
        }
        return notifs;
    }

    private List<Notification> buildPages() throws Exception {
        GetDocumentsListRequest request = (GetDocumentsListRequest) new DocumentsListRequestBuilder(mContext)
                .setContextualObject(mContextualObject)
                .build();
        request.setOkHttpClient(mClient);
        List<Notification> pages = new ArrayList<Notification>();
        DocumentsList documents = request.loadDataFromNetwork();
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
            NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                    .setContentIcon(ImageHelper.matchResourceForProvider(document.getProvider()));

            pages.add(extender.extend(builder).build());
        }
        return pages;
    }
}
