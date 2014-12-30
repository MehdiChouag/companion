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
    private final ColorMatrixColorFilter colorMatrixColorFilter;

    public DocumentsListAdapter(Activity activity, DocumentsList documents, ContextualObject contextualObject) {
        super(activity);
        mActivity = activity;
        mDocuments = documents;
        mContextualObject = contextualObject;

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        colorMatrixColorFilter = new ColorMatrixColorFilter(cm);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.row_document, parent, false);

            // Generate a new holder
            holder = new ViewHolder();
            holder.dtBand = convertView.findViewById(R.id.dtBand);
            holder.dtIcon = (ImageView) convertView.findViewById(R.id.dtIcon);
            holder.webView = (WebView) convertView.findViewById(R.id.webView);
            holder.providerIcon = (ImageView) convertView.findViewById(R.id.providerIcon);
            holder.overlay = convertView.findViewById(R.id.gestureOverlayView);

            convertView.setTag(holder);

            // Initialize components
            holder.providerIcon.setColorFilter(colorMatrixColorFilter);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Document document = mDocuments.get(position);

        holder.providerIcon.setImageResource(ImageHelper.matchResourceForProvider(document.getProviderId()));
        holder.providerIcon.setContentDescription(document.getProviderId());

        holder.dtBand.setBackgroundColor(mActivity.getResources().getColor(ImageHelper.matchColorForDocumentType(document.getTypeId())));
        holder.dtIcon.setImageResource(ImageHelper.matchIconForDocumentType(document.getTypeId()));

        holder.webView.getSettings().setJavaScriptEnabled(document.snippetRequireJavascript());
        holder.webView.loadUrl("about:blank");

        String documentSnippet = document.getSnippet();
        String htmlString = HtmlUtils.renderDocument(holder.webView.getContext(), documentSnippet);

        holder.webView.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "UTF-8", null);

        holder.overlay.setOnClickListener(new View.OnClickListener() {
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

    private static class ViewHolder {
        public View dtBand;
        public ImageView dtIcon;
        public WebView webView;
        public View overlay;
        public ImageView providerIcon;
    }

    @Override
    public Date getDate(int i) {
        return mDocuments.get(i).getDate();
    }




}
