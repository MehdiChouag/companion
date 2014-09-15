package com.anyfetch.companion.wear;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.View;

import com.anyfetch.companion.wear.ui.WearMenuAction;
import com.anyfetch.companion.wear.ui.WearMenuActivity;

public class OpenAttendeeMenuActivity extends WearMenuActivity implements View.OnClickListener {
    @Override
    protected WearMenuAction[] getMenuActions() {
        Context context = getApplicationContext();
        WearMenuAction[] actions =  new WearMenuAction[2];
        actions[0] = new WearMenuAction(context.getString(R.string.action_show_linkedin), R.drawable.ic_linked_in, this);
        actions[1] = new WearMenuAction(context.getString(R.string.action_open_in_companion), R.drawable.ic_phone_companion, this);
        return actions;
    }

    @Override
    public void onClick(View v) {
        switch (getSelectedIndex()) {
            case 0:
                Intent linkedInIntent = new Intent(this, LinkedInActivity.class);
                // TODO: Add some extra when not in demo
                startActivity(linkedInIntent);
                break;
            case 1:
                Intent confirmationIntent = new Intent(this, ConfirmationActivity.class);
                confirmationIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                startActivity(confirmationIntent);
                break;
        }
        finish();
    }
}
