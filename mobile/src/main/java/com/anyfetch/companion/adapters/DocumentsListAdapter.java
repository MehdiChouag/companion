package com.anyfetch.companion.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import com.anyfetch.companion.FullActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.ui.ImageHelper;
import com.anyfetch.companion.fragments.FullFragment;

import java.util.Date;

public class DocumentsListAdapter extends TimedListAdapter {
    private final DocumentsList mDocuments;
    private final Activity mActivity;
    private final ContextualObject mContextualObject;

    public DocumentsListAdapter(Activity activity, DocumentsList documents, ContextualObject contextualObject) {
        super(activity);
        mActivity = activity;
        mDocuments = documents;
        mContextualObject = contextualObject;
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
        dtBand.setBackgroundColor(mActivity.getResources().getColor(ImageHelper.matchColorForDocumentType(document.getType())));

        ImageView dtIcon = (ImageView) convertView.findViewById(R.id.dtIcon);
        dtIcon.setImageResource(ImageHelper.matchIconForDocumentType(document.getType()));

        WebView webView = (WebView) convertView.findViewById(R.id.webView);
        webView.loadUrl("about:blank");

        String documentSnippet = document.getSnippet();
        String htmlString = HtmlUtils.renderDocument(webView.getContext(), documentSnippet);

        webView.getSettings().setJavaScriptEnabled(document.snippetRequireJavascript());
        webView.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "UTF-8", null);

        View overlay = convertView.findViewById(R.id.gestureOverlayView);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FullActivity.class);
                intent.putExtra(FullFragment.ARG_DOCUMENT, document);
                intent.putExtra(FullFragment.ARG_CONTEXTUAL_OBJECT, mContextualObject);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity).toBundle());
                } else {
                    mActivity.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    @Override
    public Date getDate(int i) {
        return mDocuments.get(i).getDate();
    }




}
