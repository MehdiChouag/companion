package com.anyfetch.companion.stats.MixPanel;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

/**
 * Created by neamar on 1/11/15.
 */
public class MixPanel {
    public static final String MIXPANEL_TOKEN = "8dbbc1e04d6535b7c52e47c9582eaeaf";

    public static MixpanelAPI getInstance(Context context) {
        return MixpanelAPI.getInstance(context, MIXPANEL_TOKEN);
    }
}
