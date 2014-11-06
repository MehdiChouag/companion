package com.anyfetch.companion.adapters;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import com.anyfetch.companion.ui.ImageHelper;

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
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.row_document, parent, false);
        }

        final Document document = mDocuments.get(position);

        ImageView providerIcon = (ImageView) convertView.findViewById(R.id.providerIcon);
        providerIcon.setImageResource(ImageHelper.matchResourceForProvider(document.getProvider()));
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        providerIcon.setColorFilter(new ColorMatrixColorFilter(cm));
        providerIcon.setContentDescription(document.getProvider());

        View dtBand = convertView.findViewById(R.id.dtBand);
        dtBand.setBackgroundColor(mContext.getResources().getColor(ImageHelper.matchColorForDocumentType(document.getType())));

        ImageView dtIcon = (ImageView) convertView.findViewById(R.id.dtIcon);
        dtIcon.setImageResource(ImageHelper.matchIconForDocumentType(document.getType()));

        WebView webView = (WebView) convertView.findViewById(R.id.webView);
        webView.loadUrl("about:blank");
        String htmlString = HtmlUtils.renderDocument(convertView.getContext(), document.getSnippet());
        webView.getSettings().setJavaScriptEnabled(true);
		webView.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "UTF-8", null);

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




}
