package com.anyfetch.companion.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.anyfetch.companion.android.AndroidSpiceService;
import com.anyfetch.companion.android.Event;
import com.anyfetch.companion.api.HttpSpiceService;
import com.anyfetch.companion.mobile.R;
import com.octo.android.robospice.SpiceManager;

/**
 * Stores the context around an given context (Event, Person, …)
 */
public class ContextFragment extends Fragment {
    public static final String ARG_TYPE = "type";
    public static final String ARG_PARCELABLE = "parcelable";

    public static final String TYPE_EVENT = "event";

    private SpiceManager mAndroidSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private SpiceManager mApiSpiceManager = new SpiceManager(HttpSpiceService.class);

    private String mType;
    private Object mContext;
    private WebView mWebView;


    public ContextFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment.
     *
     * @param type       The type of context
     * @param parcelable The context itself
     * @return A new instance of the fragment
     */
    public static ContextFragment newInstance(String type, Parcelable parcelable) {
        ContextFragment fragment = new ContextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putParcelable(ARG_PARCELABLE, parcelable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAndroidSpiceManager.start(getActivity());
        mApiSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        mAndroidSpiceManager.shouldStop();
        mApiSpiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mContext = getArguments().getParcelable(ARG_PARCELABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_context, container, false);

        mWebView = (WebView) view.findViewById(R.id.webView);

        if (mType.equals(TYPE_EVENT)) {
            Event event = (Event) mContext;
        }

        return view;
    }


}
