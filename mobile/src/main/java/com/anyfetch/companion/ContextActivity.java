package com.anyfetch.companion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.fragments.ContextFragment;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Launches ContextFragment
 */
public class ContextActivity extends Activity {

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "8dbbc1e04d6535b7c52e47c9582eaeaf");
        JSONObject props = new JSONObject();
        try {
            props.put("Gender", "Female");
            props.put("Plan", "Premium");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Plan Selected", props);
        super.onCreate(savedInstanceState);
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
        }
    }
    protected void onDestroy() {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "8dbbc1e04d6535b7c52e47c9582eaeaf");
        mixpanel.flush();
        super.onDestroy();
    }
}
