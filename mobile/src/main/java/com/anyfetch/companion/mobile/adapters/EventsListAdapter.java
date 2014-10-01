package com.anyfetch.companion.mobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
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
public class EventsListAdapter extends ArrayAdapter<Event> implements SectionIndexer {
    private final EventsList mEvents;
    private final Context mContext;

    public EventsListAdapter(Context context, EventsList events) {
        super(context, R.layout.row_event);
        mContext = context;
        mEvents = events;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_event, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView titleView = (TextView) rowView.findViewById(R.id.titleView);
        TextView infoView = (TextView) rowView.findViewById(R.id.infoView);

        Event event = mEvents.get(position);
        Date start = event.getStartDate();

        imageView.setImageBitmap(createAttendeesMosaic(event.getAttendees()));
        titleView.setText(event.getTitle());
        infoView.setText(String.format("%d:%d, %s", start.getHours(), start.getMinutes(), event.getLocation()));

        return rowView;
    }

    private Bitmap createAttendeesMosaic(List<Person> attendees) {
        // TODO: Change this icon
        return BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.ic_menu_today);
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
