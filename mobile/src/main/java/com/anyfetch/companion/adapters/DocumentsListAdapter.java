package com.anyfetch.companion.adapters;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.anyfetch.companion.FullActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.fragments.FullFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DocumentsListAdapter extends GroupedListAdapter<Document> {


    public DocumentsListAdapter(Context context, List<Document> elements) {
        super(context, R.layout.row_document, elements);
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
    protected View getView(final Document document, View convertView, final ViewGroup parent) {
        //if (convertView == null || convertView.findViewById(R.id.webView) == null) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_document, parent, false);
        //}
        ImageView dtIcon = (ImageView) convertView.findViewById(R.id.dtIcon);
        dtIcon.setImageResource(matchIcon(document.getType()));

        WebView webView = (WebView) convertView.findViewById(R.id.webView);

        webView.loadData(HtmlUtils.HEADER + document.getSnippet() + HtmlUtils.FOOTER, "text/html", "utf-8");

        GestureOverlayView overlay = (GestureOverlayView) convertView.findViewById(R.id.gestureOverlayView);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FullActivity.class);
                intent.putExtra(FullFragment.ARG_DOCUMENT, document);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    private int matchIcon(String dt) {
        // TODO: replace with generic doctype icons
        if (dt.equals("contact")) {
            return R.drawable.ic_sfdc;
        }
        if (dt.equals("document") || dt.equals("file") || dt.equals("image")) {
            return R.drawable.ic_gdrive;
        }
        if (dt.equals("email-thread") || dt.equals("email")) {
            return R.drawable.ic_gmail;
        }
        if (dt.equals("event")) {
            return R.drawable.ic_event;
        }
        return R.drawable.ic_gdrive;
    }
}
