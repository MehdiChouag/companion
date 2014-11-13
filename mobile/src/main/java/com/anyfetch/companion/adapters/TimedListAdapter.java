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
        String appendMonth = "";
        if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
            if (now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
                color = mContext.getResources().getColor(android.R.color.holo_blue_dark);
            } else if (now.get(Calendar.DAY_OF_YEAR) + 1 == then.get(Calendar.DAY_OF_YEAR)) {
                color = mContext.getResources().getColor(android.R.color.holo_green_dark);
            } else if (now.get(Calendar.DAY_OF_YEAR) - 1 == then.get(Calendar.DAY_OF_YEAR)) {
                color = mContext.getResources().getColor(android.R.color.holo_green_dark);
            }
            if (now.get(Calendar.MONTH) == then.get(Calendar.MONTH)) {
                secondary.setText(matchDayName(then.get(Calendar.DAY_OF_WEEK)));
            } else {
                secondary.setText(matchMonthName(then.get(Calendar.MONTH)));
            }
        }
        main.setTextColor(color);
        secondary.setTextColor(color);
        main.setText(then.get(Calendar.DAY_OF_MONTH) + appendMonth);

        return convertView;
    }

    private String matchMonthName(int i) {
        switch (i) {
            case Calendar.JANUARY:
                return mContext.getString(R.string.january);
            case Calendar.FEBRUARY:
                return mContext.getString(R.string.february);
            case Calendar.MARCH:
                return mContext.getString(R.string.march);
            case Calendar.APRIL:
                return mContext.getString(R.string.april);
            case Calendar.MAY:
                return mContext.getString(R.string.may);
            case Calendar.JUNE:
                return mContext.getString(R.string.june);
            case Calendar.JULY:
                return mContext.getString(R.string.july);
            case Calendar.AUGUST:
                return mContext.getString(R.string.august);
            case Calendar.SEPTEMBER:
                return mContext.getString(R.string.september);
            case Calendar.OCTOBER:
                return mContext.getString(R.string.october);
            case Calendar.NOVEMBER:
                return mContext.getString(R.string.november);
            default:
                return mContext.getString(R.string.december);
        }
    }

    private String matchDayName(int i) {
        switch (i) {
            case Calendar.SUNDAY:
                return mContext.getString(R.string.sunday);
            case Calendar.MONDAY:
                return mContext.getString(R.string.monday);
            case Calendar.TUESDAY:
                return mContext.getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return mContext.getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return mContext.getString(R.string.thursday);
            case Calendar.FRIDAY:
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
