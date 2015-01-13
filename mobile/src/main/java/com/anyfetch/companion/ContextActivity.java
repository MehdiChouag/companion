package com.anyfetch.companion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.fragments.ContextFragment;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

/**
 * Launches ContextFragment
 */
public class ContextActivity extends Activity {
    // Notification origin
    public static final String ORIGIN = "origin";

    private MixpanelAPI mixpanel;

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mixpanel = MixPanel.getInstance(this);
        mixpanel.getPeople().increment("ContextCount", 1);

        setContentView(R.layout.activity_context);

        Intent originIntent = getIntent();

        ContextualObject contextualObject = originIntent.getParcelableExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT);

        if (savedInstanceState == null) {
            ContextFragment fragment = ContextFragment.newInstance((Parcelable) contextualObject);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();

            // We need to postpone all transitions, as the Fragment won't be loaded until later
            // See https://plus.google.com/u/1/+AlexLockwood/posts/FJsp1N9XNLS
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                postponeEnterTransition();
            }

            String contextType = contextualObject.getClass().getName();
            // Remove "com.anyfetch.companion.commons...."
            contextType = contextType.substring(contextType.lastIndexOf(".") + 1);
            JSONObject props = MixPanel.buildProp("ContextType", contextType);
            MixPanel.addProp(props, "Origin", originIntent.getStringExtra(ORIGIN));
            mixpanel.track("ViewContext", props);
        }
    }
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
