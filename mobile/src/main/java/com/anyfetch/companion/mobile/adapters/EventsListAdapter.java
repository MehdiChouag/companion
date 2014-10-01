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
           now.getDay() == then.getDay() &&
           now.getMonth() == then.getMonth()) {
            return getContext().getString(R.string.date_today);
        } else if(now.getYear() == then.getYear() &&
                  now.getDay() + 1 == then.getDay() &&
                  now.getMonth() == then.getMonth()) {
            return getContext().getString(R.string.date_tomorrow);
        } else {
            return then.getDay() + "/" + (then.getMonth() + 1);
        }
    }

    @Override
    protected View getView(Event event, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_event, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView titleView = (TextView) rowView.findViewById(R.id.titleView);
        TextView infoView = (TextView) rowView.findViewById(R.id.infoView);

        Date start = event.getStartDate();

        imageView.setImageBitmap(createAttendeesMosaic(event.getAttendees()));
        titleView.setText(event.getTitle());
        infoView.setText(String.format("%d:%d, %s", start.getHours(), start.getMinutes(), event.getLocation()));

        return rowView;
    }

    private Bitmap createAttendeesMosaic(List<Person> attendees) {
        // TODO: Change this icon
        return BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_menu_today);
    }
}
