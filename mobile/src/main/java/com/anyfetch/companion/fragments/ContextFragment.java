package com.anyfetch.companion.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anyfetch.companion.R;
import com.anyfetch.companion.adapters.DocumentsListAdapter;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.requests.GetDocumentsListRequest;
import com.anyfetch.companion.commons.notifications.MeetingPreparationAlarm;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Stores the context around an given context (Event, Person, …)
 */
public class ContextFragment extends Fragment implements RequestListener<DocumentsList>, DialogFragmentChangeListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    public static final String ARG_CONTEXTUAL_OBJECT = "contextualObject";

    private SpiceManager mSpiceManager = new SpiceManager(HttpSpiceService.class);

    private ContextualObject mContextualObject;
    private DocumentsListAdapter mListAdapter;
    private StickyListHeadersListView mListView;
    private SwipeRefreshLayout mSwipeLayout;
    private Toolbar mToolbar;


    public ContextFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment.
     *
     * @param parcelable The context itself
     * @return A new instance of the fragment
     */
    public static ContextFragment newInstance(Parcelable parcelable) {
        ContextFragment fragment = new ContextFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTEXTUAL_OBJECT, parcelable);
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
            mContextualObject = (ContextualObject) getArguments().getParcelable(ARG_CONTEXTUAL_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_context, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(mContextualObject.getTitle());
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.inflateMenu(R.menu.context);

        mListView = (StickyListHeadersListView) view.findViewById(R.id.listView);
        mListView.setDivider(null);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark);

        startQuery(true);

        return view;
    }


    @Override
    public void onRequestFailure(SpiceException spiceException) {
        // TODO
    }

    @Override
    public void onRequestSuccess(DocumentsList documents) {
        mSwipeLayout.setRefreshing(false);
        mListAdapter = new DocumentsListAdapter(getActivity(), documents);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onDialogFragmentChanged() {
        startQuery(true);
    }

    private void startQuery(boolean cached) {
        GetDocumentsListRequest request = (GetDocumentsListRequest) new DocumentsListRequestBuilder(getActivity())
                .setContextualObject(mContextualObject)
                .build();
        if (cached) {
            mSpiceManager.execute(request, request.createCacheKey(), 15 * DurationInMillis.ONE_MINUTE, this);
        } else {
            mSpiceManager.execute(request, null, 0, this);
        }
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        startQuery(false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_prepare_on_wear:
                if (mContextualObject instanceof Event) {
                    Intent i = new Intent();
                    i.setAction("com.anyfetch.companion.SHOW_NOTIFICATION");
                    i.putExtra(MeetingPreparationAlarm.ARG_EVENT, (Event) mContextualObject);
                    getActivity().sendBroadcast(i);
                }
                break;
            case R.id.action_improve_context:
                if (mContextualObject instanceof Event) {
                    Event event = (Event) mContextualObject;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    PersonChooserFragment chooser = PersonChooserFragment.newInstance(event.getAttendees());
                    chooser.setFragmentChangeListener(this);
                    chooser.show(ft, "dialog");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) { // on navigation item
        getActivity().finish();
    }
}
