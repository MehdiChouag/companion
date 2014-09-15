package com.anyfetch.companion.wear;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.View;

import com.anyfetch.companion.wear.ui.WearMenuAction;
import com.anyfetch.companion.wear.ui.WearMenuActivity;

/**
 * Created by rricard on 15/09/14.
 */
public class OpenDocumentMenuActivity extends WearMenuActivity implements View.OnClickListener {
    @Override
    protected WearMenuAction[] getMenuActions() {
        Context context = getApplicationContext();
        WearMenuAction[] actions =  new WearMenuAction[3];
        actions[0] = new WearMenuAction(context.getString(R.string.action_open_in_app), android.R.drawable.ic_menu_share, this);
        actions[1] = new WearMenuAction(context.getString(R.string.action_open_in_companion), R.drawable.ic_launcher, this);
        actions[2] = new WearMenuAction(context.getString(R.string.action_mark_important), android.R.drawable.ic_menu_add, this);
        return actions;
    }

    @Override
    public void onClick(View v) {
        Intent confirmationIntent = new Intent(this, ConfirmationActivity.class);
        switch (getSelectedIndex()) {
            case 2:
                break;
            default:
                confirmationIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                break;
        }
        startActivity(confirmationIntent);
        finish();
    }
}
