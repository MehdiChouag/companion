package com.anyfetch.companion.mobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyfetch.companion.android.Event;
import com.anyfetch.companion.android.EventsList;
import com.anyfetch.companion.android.Person;
import com.anyfetch.companion.mobile.R;
import com.anyfetch.companion.mobile.ui.ImageHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Adapt events to a list
 */
public class EventsListAdapter extends GroupedListAdapter<Event> {

    public EventsListAdapter(Context context, EventsList events) {
        super(context, R.layout.row_event, events);
    }

    @Override
    protected String getSection(Event element) {
        Date now = new Date();
        Date then = element.getStartDate();
        if(now.getYear() == then.getYear() &&
           now.getDate() == then.getDate() &&
           now.getMonth() == then.getMonth()) {
            return getContext().getString(R.string.date_today);
        } else if(now.getYear() == then.getYear() &&
                  now.getDate() + 1 == then.getDate() &&
                  now.getMonth() == then.getMonth()) {
            return getContext().getString(R.string.date_tomorrow);
        } else {
            return then.getDate() + "/" + (then.getMonth() + 1);
        }
    }

    @Override
    protected View getView(Event event, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_event, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView titleView = (TextView) rowView.findViewById(R.id.titleView);
        TextView locationView = (TextView) rowView.findViewById(R.id.locationView);
        TextView timeView = (TextView) rowView.findViewById(R.id.timeView);
        TextView attendeeView = (TextView) rowView.findViewById(R.id.attendeeView);

        int attendees = event.getAttendees().size();
        attendeeView.setText(String.format("%d %s", attendees, event.getAttendees().size() == 1 ? getContext().getString(R.string.one_attendee) : getContext().getString(R.string.multiple_attendees)));

        Date start = event.getStartDate();
        Date end = event.getEndDate();

        String location = event.getLocation();
        if(location == null) {
            location = "";
        }
        locationView.setText(location);

        imageView.setImageBitmap(createAttendeesMosaic(event.getAttendees()));
        titleView.setText(event.getTitle());
        timeView.setText(String.format("%d:%d - %d:%d", start.getHours(), start.getMinutes(), end.getHours(), end.getMinutes()));

        return rowView;
    }

    private Bitmap createAttendeesMosaic(List<Person> attendees) {
        List<Bitmap> thumbs = new ArrayList<Bitmap>();
        for(Person attendee : attendees) {
            Bitmap thumb = attendee.getThumb();
            if(thumb != null) {
                thumbs.add(thumb);
            }
        }
        int size = thumbs.size();
        if(size > 0) {
            if(size > 3) {

            } else if(size > 1) {

            }
            return ImageHelper.getRoundedCornerBitmap(thumbs.get(0), 200);
        }

        // TODO: Change this icon
        return BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_menu_today);
    }
}
