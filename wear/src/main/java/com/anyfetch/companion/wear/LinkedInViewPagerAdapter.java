package com.anyfetch.companion.wear;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.wearable.activity.InsetActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyfetch.companion.commons.models.LinkedInProfile;
import com.anyfetch.companion.commons.models.demo.DemoLinkedBenioff;
import com.anyfetch.companion.wear.ui.WearMenuAction;

/**
 * Created by rricard on 15/09/14.
 */
public class LinkedInViewPagerAdapter extends FragmentGridPagerAdapter {


    private final Activity mContext;
    private final LinkedInProfile mProfile;

    public LinkedInViewPagerAdapter(Activity context,FragmentManager fm, LinkedInProfile linkedInProfile) {
        super(fm);
        mContext = context;
        mProfile = linkedInProfile;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        String clearFix = "\n";
        if(col == 0) { // Connections
            String text = "0 Connections";
            if(mProfile.getConnections().size() > 0) {
                text = "";
                for(int i = 0; i < mProfile.getConnections().size() && i < 3; i ++) {
                    text += mProfile.getConnections().get(i).getName() + " ";
                }
                if(mProfile.getConnections().size() > 3) {
                    text += " & " + (mProfile.getConnections().size() - 3) + mContext.getString(R.string.other_attendees);
                }
            }
            return CardFragment.create(mContext.getString(R.string.linked_in_connections), text + clearFix); // TODO: add a linked in icon
        } else { // Icebreakers
            String text = "";
            for(int i = 0; i < mProfile.getLikes().size() && i < 5; i ++) {
                text += mProfile.getLikes().get(i) + " ";
            }
            return CardFragment.create(mContext.getString(R.string.linked_in_likes), text + clearFix);
        }
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }

    @Override
    public ImageReference getBackground(int row, int col) {
        return ImageReference.forBitmap(mProfile.getFace());
    }
}
