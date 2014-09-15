package com.anyfetch.companion.wear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.activity.InsetActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;

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
        CardFragment card = null;
        String clearFix = context.isRound() ? "\n" : "";
        if(row == 0 && col == 0) { // Event presentation
            String text = "";
            if(event.getAttendees().size() > 0) {
                text = event.getAttendees().get(0).getName() + " ";
                if(event.getAttendees().size() > 1) {
                    text += "& " + (event.getAttendees().size() - 1) + " " + context.getString(R.string.other_attendees) + " ";
                }
            }
            text += event.getStart().getHours() + ":" + event.getStart().getMinutes();
            return CardFragment.create(event.getTitle(), text + clearFix, R.drawable.ic_event);
        } else if(row > 0 && col == 0) { // Attendee presentation
            Attendee attendee = event.getAttendees().get(row - 1);
            // TODO: icon for demo purposes only
            return AttendeeCardFragment.create(attendee.getName(), attendee.getJob() + clearFix, R.drawable.ic_sfdc);
        } else { // Document context
            Document document;
            if(row == 0) {
                document = ((Context) event).getAssociatedDocuments().get(col - 1);
            } else {
                Context attendeeContext = (Context) event.getAttendees().get(row - 1);
                document = attendeeContext.getAssociatedDocuments().get(col - 1);
            }
            return DocumentCardFragment.create(document.getTitle(), document.getSnippet() + clearFix, document.getIcon());
        }
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
