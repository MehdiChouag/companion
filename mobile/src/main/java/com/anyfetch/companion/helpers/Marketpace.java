package com.anyfetch.companion.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.anyfetch.companion.R;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

public class Marketpace {
    private final Context mContext;
    private final MixpanelAPI mMixpanel;

    public Marketpace(final Context context, final MixpanelAPI mixpanel) {
        mContext = context;
        mMixpanel = mixpanel;
    }
    public void openMarketplace(final String origin) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(mContext.getString(R.string.opening_manager));
        alertDialog.setMessage(mContext.getString(R.string.opening_manager_description));
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JSONObject props = MixPanel.buildProp("origin", origin);
                mMixpanel.track("Open marketplace", props);
                String url = "https://manager.anyfetch.com/marketplace";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
