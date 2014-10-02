package com.anyfetch.companion.mobile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyfetch.companion.api.pojo.Document;
import com.anyfetch.companion.mobile.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DocumentsListAdapter extends GroupedListAdapter<Document> {
    public DocumentsListAdapter(Context context, List<Document> elements) {
        super(context, R.layout.row_event, elements);
    }

    @Override
    protected String getSection(Document document) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar then = Calendar.getInstance();
        then.setTime(document.getDate());
        if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
            return getContext().getString(R.string.date_today);
        } else if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - 1 == then.get(Calendar.DAY_OF_YEAR)) {
            return getContext().getString(R.string.date_yesterday);
        } else {
            return then.get(Calendar.DAY_OF_MONTH) + "/" + (then.get(Calendar.MONTH));
        }
    }

    @Override
    protected View getView(Document document, View convertView, ViewGroup parent) {
        TextView v = new TextView(getContext());
        v.setText(document.getTitle());
        return v;
    }
}
