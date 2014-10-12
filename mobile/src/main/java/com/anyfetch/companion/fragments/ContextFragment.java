package com.anyfetch.companion.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anyfetch.companion.R;
import com.anyfetch.companion.adapters.DocumentsListAdapter;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.GetDocumentsListRequest;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Stores the context around an given context (Event, Person, …)
 */
public class ContextFragment extends Fragment implements RequestListener<DocumentsList> {
    public static final String ARG_TYPE = "type";
    public static final String ARG_PARCELABLE = "parcelable";

    public static final String TYPE_EVENT = "event";

    private SpiceManager mSpiceManager = new SpiceManager(HttpSpiceService.class);

    private String mType;
    private Object mContext;
    private DocumentsListAdapter mListAdapter;
    private StickyListHeadersListView mListView;
    private ProgressBar mProgress;


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
            mType = getArguments().getString(ARG_TYPE);
            mContext = getArguments().getParcelable(ARG_PARCELABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_context, container, false);
        mListView = (StickyListHeadersListView) view.findViewById(R.id.listView);
        mProgress = (ProgressBar) view.findViewById(R.id.progressBar);

        GetDocumentsListRequest request = new GetDocumentsListRequest(getActivity(), getContextQuery());
        mSpiceManager.execute(request, request.createCacheKey(), 15 * DurationInMillis.ONE_MINUTE, this);

        return view;
    }


    public String getContextQuery() {
        // TODO: move logic to commons
        if (mType.equals(TYPE_EVENT)) {
            Event event = (Event) mContext;
            String query = "(" + event.getTitle() + ")";
            for (Person attendee : event.getAttendees()) {
                if (attendee.getName() != null) {
                    query += " OR (" + attendee.getName() + ")";
                }
                for (String email : attendee.getEmails()) {
                    query += " OR (" + email + ")";
                }
            }
            return query;
        } else {
            return "";
        }
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        // TODO
    }

    @Override
    public void onRequestSuccess(DocumentsList documents) {
        mProgress.setVisibility(View.INVISIBLE);
        mListAdapter = new DocumentsListAdapter(getActivity(), documents);
        mListView.setAdapter(mListAdapter);
    }
}
