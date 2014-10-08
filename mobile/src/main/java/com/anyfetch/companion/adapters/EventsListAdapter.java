package com.anyfetch.companion.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.android.Event;
import com.anyfetch.companion.commons.android.EventsList;
import com.anyfetch.companion.commons.android.Person;
import com.anyfetch.companion.ui.ImageHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Adapt events to a list
 */
public class EventsListAdapter extends GroupedListAdapter<Event> {

    private final Context mContext;

    /**
     * Creates a new events adapter
     *
     * @param context The app context
     * @param events  The events to show
     */
    public EventsListAdapter(Context context, EventsList events) {
        super(context, R.layout.row_event, events);
        mContext = context;
    }

    @Override
    protected String getSection(Event element) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar then = Calendar.getInstance();
        then.setTime(element.getStartDate());
        if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
            return getContext().getString(R.string.date_today);
        } else if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) + 1 == then.get(Calendar.DAY_OF_YEAR)) {
            return getContext().getString(R.string.date_tomorrow);
        } else {
            return then.get(Calendar.DAY_OF_MONTH) + "/" + (then.get(Calendar.MONTH));
        }
    }

    @Override
    protected View getView(Event event, View convertView, ViewGroup parent) {
        //if(convertView == null) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_event, parent, false);
        //}

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageBitmap(createAttendeesMosaic(event.getAttendees()));

        TextView titleView = (TextView) convertView.findViewById(R.id.titleView);
        titleView.setText(event.getTitle());

        TextView locationView = (TextView) convertView.findViewById(R.id.locationView);
        String location = event.getLocation();
        if(location == null) {
            location = "";
        }
        locationView.setText(location);

        TextView timeView = (TextView) convertView.findViewById(R.id.timeView);
        Calendar start = Calendar.getInstance();
        start.setTime(event.getStartDate());
        Calendar end = Calendar.getInstance();
        end.setTime(event.getEndDate());

        if (end.getTimeInMillis() - start.getTimeInMillis() != 1000 * 60 * 60 * 24) {
            timeView.setText(
                    String.format("%02d:%02d - %02d:%02d",
                            start.get(Calendar.HOUR_OF_DAY),
                            start.get(Calendar.MINUTE),
                            end.get(Calendar.HOUR_OF_DAY),
                            end.get(Calendar.MINUTE)));
        } else {
            timeView.setText("");
        }

        TextView attendeeView = (TextView) convertView.findViewById(R.id.attendeeView);
        int attendees = event.getAttendees().size();
        if(attendees == 1) {
            attendeeView.setText(event.getAttendees().get(0).getName());
        } else {
            attendeeView.setText(String.format(mContext.getString(R.string.multiple_attendees), attendees));
        }

        return convertView;
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
            return ImageHelper.getRoundedCornerBitmap(thumbs.get(0), 200);
        }

        // TODO: Change this icon
        return BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_menu_today);
    }
}
