package com.anyfetch.companion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.anyfetch.companion.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Handle timed headers
 */
public abstract class TimedListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private final LayoutInflater mInflater;
    private final Context mContext;

    public TimedListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getHeaderView(int i, View convertView, ViewGroup viewGroup) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        then.setTime(getDate(i));

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_group_header, viewGroup, false);
        }

        TextView main = (TextView) convertView.findViewById(R.id.mainTitle);
        TextView secondary = (TextView) convertView.findViewById(R.id.secondaryTitle);
        int color = mContext.getResources().getColor(R.color.text_primary);
        if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
            if (now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
                color = mContext.getResources().getColor(android.R.color.holo_blue_dark);
            } else if (now.get(Calendar.DAY_OF_YEAR) + 1 == then.get(Calendar.DAY_OF_YEAR)) {
                color = mContext.getResources().getColor(android.R.color.holo_green_dark);
            } else if (now.get(Calendar.DAY_OF_YEAR) - 1 == then.get(Calendar.DAY_OF_YEAR)) {
                color = mContext.getResources().getColor(android.R.color.holo_green_dark);
            }
        }
        main.setTextColor(color);
        secondary.setTextColor(color);
        main.setText(String.format("%d", then.get(Calendar.DAY_OF_MONTH)));
        secondary.setText(matchDayName(then.get(Calendar.DAY_OF_WEEK)));

        return convertView;
    }

    private String matchDayName(int i) {
        switch (i) {
            case 1:
                return mContext.getString(R.string.sunday);
            case 2:
                return mContext.getString(R.string.monday);
            case 3:
                return mContext.getString(R.string.tuesday);
            case 4:
                return mContext.getString(R.string.wednesday);
            case 5:
                return mContext.getString(R.string.thursday);
            case 6:
                return mContext.getString(R.string.friday);
            default:
                return mContext.getString(R.string.saturday);
        }
    }

    @Override
    public long getHeaderId(int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate(i));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        return cal.getTimeInMillis();
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    public abstract Date getDate(int i);
}
