package com.anyfetch.companion.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.helpers.HtmlUtils;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.requests.GetDocumentRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class FullFragment extends Fragment implements RequestListener<Document> {
    public static final String ARG_DOCUMENT = "arg_parcelable";

    private SpiceManager mSpiceManager = new SpiceManager(HttpSpiceService.class);

    private Document mDocument;
    private WebView mFullWebView;
    private ProgressBar mProgress;


    public FullFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param document The document to pass into the full projection
     * @return A new instance of fragment FullFragment.
     */
    public static FullFragment newInstance(Document document) {
        FullFragment fragment = new FullFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DOCUMENT, document);
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

            if (mDocument.getFull().equals("")) {
                // TODO: bring ctx query
                GetDocumentRequest request = new GetDocumentRequest(getActivity(), mDocument.getDocumentId(), "");
                mSpiceManager.execute(request, request.createCacheKey(), 15 * DurationInMillis.ONE_MINUTE, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full, container, false);

        mFullWebView = (WebView) view.findViewById(R.id.fullWebView);
        mProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        showFull();

        return view;
    }

    private void showFull() {
        if (mFullWebView != null && !mDocument.getFull().equals("")) {
            mProgress.setVisibility(View.INVISIBLE);
            // TODO: integrate html generation inside the document
            String htmlString = HtmlUtils.HEADER + mDocument.getFull() + HtmlUtils.FOOTER;
            mFullWebView.loadData(htmlString, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        // TODO
    }

    @Override
    public void onRequestSuccess(Document document) {
        mDocument = document;
        showFull();
    }
}