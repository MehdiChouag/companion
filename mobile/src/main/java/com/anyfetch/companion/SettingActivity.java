package com.anyfetch.companion;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.anyfetch.companion.stats.MixPanel;

public class SettingActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up
        super.onCreate(savedInstanceState);

        MixPanel.getInstance(this);

        // UI
        addPreferencesFromResource(R.xml.settings);
    }
    protected void onDestroy() {
        MixPanel.getInstance(this).flush();
        super.onDestroy();
    }
}
