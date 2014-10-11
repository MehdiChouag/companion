package com.anyfetch.companion.adapters;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.anyfetch.companion.FullActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.fragments.FullFragment;

import java.util.Date;

public class DocumentsListAdapter extends TimedListAdapter {
    private final DocumentsList mDocuments;
    private final Context mContext;

    public DocumentsListAdapter(Context context, DocumentsList documents) {
        super(context);
        mContext = context;
        mDocuments = documents;
    }

    @Override
    public int getCount() {
        return mDocuments.size();
    }

    @Override
    public Object getItem(int position) {
        return mDocuments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getInflater().inflate(R.layout.row_document, parent, false);

        final Document document = mDocuments.get(position);

        ImageView dtIcon = (ImageView) convertView.findViewById(R.id.dtIcon);
        dtIcon.setImageResource(matchIcon(document.getType()));

        WebView webView = (WebView) convertView.findViewById(R.id.webView);
        String htmlString = HtmlUtils.HEADER + document.getSnippet() + HtmlUtils.FOOTER;
        webView.loadData(htmlString, "text/html; charset=UTF-8", null);

        GestureOverlayView overlay = (GestureOverlayView) convertView.findViewById(R.id.gestureOverlayView);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FullActivity.class);
                intent.putExtra(FullFragment.ARG_DOCUMENT, document);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public Date getDate(int i) {
        return mDocuments.get(i).getDate();
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
