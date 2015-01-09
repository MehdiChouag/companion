package com.anyfetch.companion;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class SettingActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "8dbbc1e04d6535b7c52e47c9582eaeaf");
        // Set up
        super.onCreate(savedInstanceState);

        // UI
        addPreferencesFromResource(R.xml.settings);
    }
    protected void onDestroy() {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "8dbbc1e04d6535b7c52e47c9582eaeaf");
        mixpanel.flush();
        super.onDestroy();
    }
}
