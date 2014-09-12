package com.anyfetch.companion.wear;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.anyfetch.companion.R;

public class DisplayEventNotificationActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_notification);
        mTextView = (TextView) findViewById(R.id.text);
    }
}