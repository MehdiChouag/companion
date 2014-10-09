package com.anyfetch.companion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anyfetch.companion.R;

import java.util.Calendar;
import java.util.Date;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

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

        TextView title = (TextView) convertView.findViewById(R.id.titleView);
        String electedDate = "";
        if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
            if (now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
                electedDate = mContext.getString(R.string.date_today);
            } else if (now.get(Calendar.DAY_OF_YEAR) + 1 == then.get(Calendar.DAY_OF_YEAR)) {
                electedDate = mContext.getString(R.string.date_tomorrow);
            } else if (now.get(Calendar.DAY_OF_YEAR) - 1 == then.get(Calendar.DAY_OF_YEAR)) {
                electedDate = mContext.getString(R.string.date_yesterday);
            }
        }
        if (electedDate.equals("")) {
            electedDate = then.get(Calendar.DAY_OF_MONTH) + "/" + (then.get(Calendar.MONTH) + 1);
        }
        title.setText(electedDate);

        return convertView;
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
