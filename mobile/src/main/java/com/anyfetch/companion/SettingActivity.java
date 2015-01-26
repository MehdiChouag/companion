package com.anyfetch.companion;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

public class SettingActivity extends PreferenceActivity {
    private MixpanelAPI mixpanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up
        super.onCreate(savedInstanceState);

        mixpanel = MixPanel.getInstance(this);
        mixpanel.track("SettingActivity", new JSONObject());


        // UI
        addPreferencesFromResource(R.xml.settings);
    }

    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
