package com.anyfetch.companion.wear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.wearable.activity.InsetActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.view.View;

import com.anyfetch.companion.commons.models.Attendee;
import com.anyfetch.companion.commons.models.Context;
import com.anyfetch.companion.commons.models.Document;
import com.anyfetch.companion.commons.models.Event;

/**
 * Created by rricard on 12/09/14.
 */
public class EventGridViewPagerAdapter extends FragmentGridPagerAdapter {
    private final Event event;
    private final InsetActivity context;

    public EventGridViewPagerAdapter(InsetActivity context, FragmentManager fm, Event event) {
        super(fm);
        this.event = event;
        this.context = context;
    }

    @Override
    public int getRowCount() {
        return event.getAttendees().size() + 1;
    }

    @Override
    public int getColumnCount(int row) {
        if(row == 0) {
            Context eventContext = (Context) event;
            return eventContext.getAssociatedDocuments().size() + 1;
        } else {
            Context attendeeContext = (Context) event.getAttendees().get(row - 1);
            return attendeeContext.getAssociatedDocuments().size() + 1;
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        String title;
        String text = "";
        int icon;
        CardFragment card = null;
        if(row == 0 && col == 0) { // Event presentation
            if(event.getAttendees().size() > 0) {
                text = event.getAttendees().get(0).getName() + " ";
                if(event.getAttendees().size() > 1) {
                    text += "& " + (event.getAttendees().size() - 1) + " " + context.getString(R.string.other_attendees) + " ";
                }
            }
            text += event.getStart().getHours() + ":" + event.getStart().getMinutes();
            title = event.getTitle();
            icon = R.drawable.ic_event;
        } else if(row > 0 && col == 0) { // Attendee presentation
            Attendee attendee = event.getAttendees().get(row - 1);
            title = attendee.getName();
            text = attendee.getJob();
            icon = R.drawable.ic_sfdc; // TODO: for demo purposes only
        } else { // Document context
            Document document;
            if(row == 0) {
                document = ((Context) event).getAssociatedDocuments().get(col - 1);
            } else {
                Context attendeeContext = (Context) event.getAttendees().get(row - 1);
                document = attendeeContext.getAssociatedDocuments().get(col - 1);
            }
            title = document.getTitle();
            text = document.getSnippet();
            icon = document.getIcon();
            card = DocumentCardFragment.create(title, text, icon);
        }
        if(card == null) {
            if(context.isRound()) {
                text += "\n";
            }
            card = CardFragment.create(title, text, icon);
        }
        card.setExpansionEnabled(true);
        return card;
    }

    @Override
    public ImageReference getBackground(int row, int col) {
        if(row > 0) { // Attendee presentation
            Attendee attendee = event.getAttendees().get(row - 1);
            return ImageReference.forBitmap(attendee.getFace());
        }
        return ImageReference.forDrawable(R.drawable.bg_generic);
    }
}
