package com.anyfetch.companion.stats;

import android.content.Context;
import android.preference.PreferenceManager;

import com.anyfetch.companion.commons.api.builders.BaseRequestBuilder;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by neamar on 1/11/15.
 */
public class MixPanel {
    public static final String MIXPANEL_TOKEN = "8dbbc1e04d6535b7c52e47c9582eaeaf";

    public static MixpanelAPI getInstance(Context context) {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, MIXPANEL_TOKEN);
        String token = PreferenceManager.getDefaultSharedPreferences(context).getString(BaseRequestBuilder.PREF_API_TOKEN, "");
        mixpanel.identify(Integer.toString(token.hashCode()));
        return mixpanel;
    }

    public static JSONObject buildProp(String name, String value) {
        JSONObject props = new JSONObject();
        addProp(props, name, value);

        return props;
    }

    public static JSONObject addProp(JSONObject props, String name, String value) {
        try {
            props.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return props;
    }
}
