package com.anyfetch.companion.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Stores the context around an given context (Event, Person, …)
 */
public class ContextFragment extends Fragment implements RequestListener<DocumentsList>, DialogFragmentChangeListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, View.OnClickListener, AbsListView.OnScrollListener, TabHost.OnTabChangeListener {
    public static final String ARG_CONTEXTUAL_OBJECT = "contextualObject";
    private static final String TAB_ALL = "tab_all";
    private final TabHost.TabContentFactory EMPTY_TAB = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
            return new View(getActivity());
        }
    };

    private SpiceManager mSpiceManager = new SpiceManager(HttpSpiceService.class);

    private ContextualObject mContextualObject;
    private DocumentsListAdapter mListAdapter;
    private StickyListHeadersListView mListView;
    private SwipeRefreshLayout mSwipeLayout;
    private Toolbar mToolbar;
    private View mContextHeader;
    private TabHost mTabHost;
    private String mCurrentSubcontext;


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
        mToolbar.setTitleTextColor(Color.alpha(0));
        mToolbar.setBackgroundColor(Color.alpha(0));

        mContextHeader = inflater.inflate(R.layout.row_context_header, mListView, false);
        TextView headerTitle = (TextView) mContextHeader.findViewById(R.id.headerTitle);
        headerTitle.setText(mContextualObject.getTitle());
        ImageView headerImage = (ImageView) mContextHeader.findViewById(R.id.headerImage);
        headerImage.setImageResource(R.drawable.bg_sf); // TODO: change it with something contextual
        headerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mTabHost = (TabHost) mContextHeader.findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        mListView = (StickyListHeadersListView) view.findViewById(R.id.listView);
        mListView.addHeaderView(mContextHeader);
        mListView.setOnScrollListener(this);
        mListView.setDivider(null);
        mListView.setAreHeadersSticky(false);
        mListAdapter = new DocumentsListAdapter(getActivity(), new DocumentsList());
        mListView.setAdapter(mListAdapter);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark);

        refreshTabs();
        startQuery(true);

        return view;
    }

    private void refreshTabs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> tailedEmails = prefs.getStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, new HashSet<String>());
        mTabHost.clearAllTabs();
        Map<String, String> tabQueries = mContextualObject.getAdditionalSearchQueries(tailedEmails);
        if (tabQueries != null && !tabQueries.isEmpty()) {
            TabHost.TabSpec allSpec = mTabHost.newTabSpec(TAB_ALL)
                    .setIndicator(getString(R.string.tab_all))
                    .setContent(EMPTY_TAB);
            mTabHost.addTab(allSpec);
            for (String tabName : tabQueries.keySet()) {
                TabHost.TabSpec spec = mTabHost.newTabSpec(tabName)
                        .setIndicator(tabName)
                        .setContent(EMPTY_TAB);
                mTabHost.addTab(spec);
            }
            for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(getResources().getColor(R.color.abc_primary_text_material_dark));
            }
            mTabHost.setCurrentTab(0);
        }
        mCurrentSubcontext = null;
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
        refreshTabs();
        startQuery(true);
    }

    private void startQuery(boolean cached) {
        GetDocumentsListRequest request = (GetDocumentsListRequest) new DocumentsListRequestBuilder(getActivity())
                .selectSubContext(mCurrentSubcontext)
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

    private float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // ----- Obscure calculations -----
        View c = listView.getChildAt(0);
        int scrollY = 0;
        if (c != null) {
            scrollY = -c.getTop() + listView.getFirstVisiblePosition() * c.getHeight();
        }
        int headerSize = getActivity().getResources().getDimensionPixelSize(R.dimen.context_header_height);
        int minHeaderSize = getActivity().getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
        float ratio = clamp((float) (scrollY + minHeaderSize) / headerSize, 0, 1);
        // ----- End of obscure calculations -----
        float shiftedRatio = clamp(2 * ratio - 1, 0, 1);

        int textPrimary = Color.WHITE;
        int primary = getActivity().getResources().getColor(R.color.primary);
        mToolbar.setTitleTextColor(Color.argb(
                (int) (shiftedRatio * 255),
                Color.red(textPrimary),
                Color.green(textPrimary),
                Color.blue(textPrimary)
        ));
        mToolbar.setBackgroundColor(Color.argb(
                (int) (shiftedRatio * 255),
                Color.red(primary),
                Color.green(primary),
                Color.blue(primary)
        ));
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(TAB_ALL)) {
            mCurrentSubcontext = null;
        } else {
            mCurrentSubcontext = tabId;
        }
        startQuery(true);
    }
}
