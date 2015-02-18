package com.anyfetch.companion.fragments;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.anyfetch.companion.ContextActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.adapters.EventsListAdapter;
import com.anyfetch.companion.commons.android.AndroidSpiceService;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.requests.GetUpcomingEventsRequest;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.meetings.ScheduleMeetingPreparationTask;
import com.anyfetch.companion.stats.MixPanel;
import com.melnykov.fab.FloatingActionButton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONObject;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by mehdichouag on 18/02/15.
 */
public class UpcomingEventsFragment extends Fragment implements RequestListener<EventsList>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener  {
    private final SpiceManager mSpiceManager = new SpiceManager(AndroidSpiceService.class);
    private final SpiceManager mHttpSpiceManager = new SpiceManager(HttpSpiceService.class);
    private StickyListHeadersListView mListView;
    private EventsListAdapter mListAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mEmptyView;
    private MixpanelAPI mixpanel;


    public UpcomingEventsFragment() {

    }

    public static UpcomingEventsFragment newInstance() {
        return new UpcomingEventsFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSpiceManager.start(getActivity());
        mHttpSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        mSpiceManager.shouldStop();
        mHttpSpiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mixpanel = MixPanel.getInstance(getActivity());
        mixpanel.track("UpcomingEventsFragment", new JSONObject());

        final View rootView = inflater.inflate(R.layout.fragment_upcoming_events, container, false);

        bindView(rootView);

        return rootView;
    }

    private void bindView(View rootView) {


        mListView = (StickyListHeadersListView) rootView.findViewById(R.id.listView);

        mEmptyView = (TextView) rootView.findViewById(android.R.id.empty);
        mEmptyView.setText(getString(R.string.loading));

        mListAdapter = new EventsListAdapter(getActivity().getApplicationContext(), new EventsList());
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setAreHeadersSticky(true);
        mListView.setDividerHeight(0);
        mListView.setEmptyView(mEmptyView);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(mListView.getWrappedList());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                startActivity(intent);
            }
        });

        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark);


        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getActivity().getApplicationContext());
        mSpiceManager.execute(request, request.createCacheKey(), 15 * DurationInMillis.ONE_MINUTE, this);
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Toast.makeText(getActivity(), getString(R.string.calendar_error), Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void onRequestSuccess(EventsList events) {
        MixPanel.getInstance(getActivity()).getPeople().set("EventsCount", events.size());
        mSwipeLayout.setRefreshing(false);
        if (events.size() > 0) {
            new ScheduleMeetingPreparationTask(getActivity()).execute(null, null, null);
        }
        mListAdapter = new EventsListAdapter(getActivity().getApplicationContext(), events);
        mListView.setAdapter(mListAdapter);

        mEmptyView.setText(getString(R.string.no_upcoming_shared_events));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Event event = mListAdapter.getEvent(position);
        View backgroundView = view.findViewById(R.id.main_container);
        View imageView = view.findViewById(R.id.imageView);
        View titleView = view.findViewById(R.id.titleView);
        View locationView = view.findViewById(R.id.locationView);

        Intent intent = new Intent(getActivity().getApplicationContext(), ContextActivity.class);
        intent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, event);
        intent.putExtra(ContextActivity.ORIGIN, "upcomingEventList");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions animation = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(imageView, "imageView"),
                    Pair.create(backgroundView, "backgroundView"),
                    Pair.create(locationView, "locationView"),
                    Pair.create(titleView, "titleView")
            );
            getActivity().startActivity(intent, animation.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    @Override
    public void onRefresh() {
        GetUpcomingEventsRequest request = new GetUpcomingEventsRequest(getActivity());
        mSpiceManager.execute(request, null, 0, this);
    }

    public void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }
}
