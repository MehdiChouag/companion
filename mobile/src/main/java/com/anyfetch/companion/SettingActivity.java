package com.anyfetch.companion;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

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
        if (savedInstanceState == null) {
            mixpanel.track("SettingActivity", new JSONObject());
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingFragment())
                    .commit();
        }
    }

    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
