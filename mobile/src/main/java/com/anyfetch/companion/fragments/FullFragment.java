package com.anyfetch.companion.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.commons.api.builders.DocumentRequestBuilder;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.requests.GetDocumentRequest;
import com.anyfetch.companion.commons.ui.ImageHelper;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class FullFragment extends Fragment implements RequestListener<Document>, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    public static final String ARG_DOCUMENT = "arg_parcelable";
    public static final String ARG_CONTEXTUAL_OBJECT = "arg_query";

    private final SpiceManager mSpiceManager = new SpiceManager(HttpSpiceService.class);

    private Document mDocument;
    private WebView mFullWebView;
    private ProgressBar mProgress;
    private ContextualObject mContextualObject;


    public FullFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param document The document to pass into the full projection
     * @param contextualObject The contextualObject for highlighting purposes
     * @return A new instance of fragment FullFragment.
     */
    public static FullFragment newInstance(Document document, ContextualObject contextualObject) {
        FullFragment fragment = new FullFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DOCUMENT, document);
        args.putParcelable(ARG_CONTEXTUAL_OBJECT, contextualObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDocument = getArguments().getParcelable(ARG_DOCUMENT);
            mContextualObject = getArguments().getParcelable(ARG_CONTEXTUAL_OBJECT);

            if (mDocument.getFull().equals("")) {
                // TODO: bring ctx query
                GetDocumentRequest request = (GetDocumentRequest) new DocumentRequestBuilder(getActivity())
                        .setDocument(mDocument)
                        .actionGet()
                        .setContextualObject(mContextualObject)
                        .build();
                mSpiceManager.execute(request, request.createCacheKey(), DurationInMillis.ONE_MINUTE, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(HtmlUtils.stripHtml(mDocument.getTitle()));
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setLogo(ImageHelper.matchResourceForProvider(mDocument.getProvider()));
        toolbar.inflateMenu(R.menu.full);
        ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));

        mFullWebView = (WebView) view.findViewById(R.id.fullWebView);
        mProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        showFull();

        return view;
    }

    private void showFull() {
        if (mFullWebView != null && !mDocument.getFull().equals("")) {
            mProgress.setVisibility(View.INVISIBLE);
            String htmlString = HtmlUtils.renderDocument(mFullWebView.getContext(), mDocument.getFull());
            mFullWebView.getSettings().setJavaScriptEnabled(mDocument.fullRequireJavascript());
            mFullWebView.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html", "UTF-8", null);
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestSuccess(Document document) {
        mDocument = document;
        showFull();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_open_in_app:
                Log.i("LeavingApp", "Leaving app to " + mDocument.getLink());
                String url = mDocument.getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}