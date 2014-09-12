package com.anyfetch.companion.wear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Example shell activity which simply broadcasts to our receiver and exits.
 */
public class FakeEventNotification extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setAction("com.anyfetch.companion.wear.SHOW_NOTIFICATION");
        i.putExtra(PostEventNotificationReceiver.CONTENT_KEY, "demo");
        sendBroadcast(i);
        finish();
    }
}
