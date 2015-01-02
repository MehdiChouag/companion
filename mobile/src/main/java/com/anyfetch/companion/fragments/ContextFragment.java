package com.anyfetch.companion.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.anyfetch.companion.R;
import com.anyfetch.companion.adapters.DocumentsListAdapter;
import com.anyfetch.companion.commons.api.HttpSpiceService;
import com.anyfetch.companion.commons.api.builders.ContextualObject;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.requests.GetDocumentsListRequest;
import com.anyfetch.companion.notifications.BuildNotificationStackTask;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Stores the context around an given context (Event, Person, …)
 */
public class ContextFragment extends Fragment implements RequestListener<DocumentsList>, DialogFragmentChangeListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, View.OnClickListener, AbsListView.OnScrollListener, TabHost.OnTabChangeListener {
    public static final String ARG_CONTEXTUAL_OBJECT = "contextualObject";
    private static final String TAB_ALL = "tab_all";
    private final SpiceManager mSpiceManager = new SpiceManager(HttpSpiceService.class);
    private DocumentsListAdapter mListAdapter;
    private StickyListHeadersListView mListView;
    private SwipeRefreshLayout mSwipeLayout;
    private Toolbar mToolbar;
    private TabHost mTabHost;
    private HorizontalScrollView mTabContainer;
    private View mContextTab;
    private final TabHost.TabContentFactory CONTEXT_TAB = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
            return mContextTab;
        }
    };
    private ContextualObject mRootContextualObject;
    private List<ContextualObject> mSubContexts;
    private ContextualObject mSelectedContextualObject;


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
            mRootContextualObject = getArguments().getParcelable(ARG_CONTEXTUAL_OBJECT);
            mSelectedContextualObject = mRootContextualObject;
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_context, container, false);
        View contextHeader = inflater.inflate(R.layout.row_context_header, mListView, false);
        mContextTab = inflater.inflate(R.layout.tab_context, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.inflateMenu(R.menu.context);
        mToolbar.setTitleTextColor(Color.TRANSPARENT);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.toolbar_elevation));

        mTabContainer = (HorizontalScrollView) contextHeader.findViewById(R.id.tabContainer);
        mTabHost = (TabHost) contextHeader.findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        ViewCompat.setElevation(mTabHost.getTabContentView(), getResources().getDimension(R.dimen.toolbar_elevation));
        ViewCompat.setElevation(mTabHost.getTabWidget(), getResources().getDimension(R.dimen.toolbar_elevation));

        mListView = (StickyListHeadersListView) view.findViewById(R.id.listView);
        mListView.addHeaderView(contextHeader);
        mListView.setOnScrollListener(this);
        mListView.setDivider(null);
        mListView.setAreHeadersSticky(false);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListAdapter = new DocumentsListAdapter(getActivity(), new DocumentsList(), mSelectedContextualObject);
        mListView.setAdapter(mListAdapter);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark);

        refreshTabs();
        startQuery(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ImageView thumbnail = (ImageView) view.findViewById(R.id.imageView);
            thumbnail.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @SuppressLint("NewApi")
                public boolean onPreDraw() {
                    // Once the image is ready, we can start the animation previously postponed (from activity.onCreate())
                    // See https://plus.google.com/u/1/+AlexLockwood/posts/FJsp1N9XNLS
                    // See http://stackoverflow.com/questions/26717515/weird-issue-when-transitioning-imageview-in-android-5-0
                    thumbnail.getViewTreeObserver().removeOnPreDrawListener(this);
                    getActivity().startPostponedEnterTransition();
                    return true;
                }
            });
        }
        return view;
    }


    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestSuccess(DocumentsList documents) {
        mSwipeLayout.setRefreshing(false);
        mListAdapter = new DocumentsListAdapter(getActivity(), documents, mSelectedContextualObject);
        mListView.setAdapter(mListAdapter);

        if(documents.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.context_has_no_match), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogFragmentChanged() {
        refreshTabs();
        startQuery(true);
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
                Toast.makeText(getActivity(), getString(R.string.sent_to_watch), Toast.LENGTH_LONG).show();
                new BuildNotificationStackTask(getActivity()).execute(mRootContextualObject, null, null);
                break;
            case R.id.action_improve_context:
                if (mRootContextualObject.getPersons() != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    PersonChooserFragment chooser = PersonChooserFragment.newInstance(mRootContextualObject.getPersons());
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
        float ratio = clamp((float) (scrollY + minHeaderSize) / headerSize);
        // ----- End of obscure calculations -----

        int textPrimary = Color.WHITE;
        int primaryDark = getActivity().getResources().getColor(R.color.primary_dark);
        if (mSelectedContextualObject.getColor() != -1) {
            primaryDark = mSelectedContextualObject.getColor();
        }

        mToolbar.setTitleTextColor(Color.argb(
                (int) (clamp(2 * ratio - 1) * 255),
                Color.red(textPrimary),
                Color.green(textPrimary),
                Color.blue(textPrimary)
        ));
        mToolbar.setBackgroundColor(Color.argb(
                (int) (clamp(4 * ratio - 2) * 255),
                Color.red(primaryDark),
                Color.green(primaryDark),
                Color.blue(primaryDark)
        ));
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(TAB_ALL)) {
            mSelectedContextualObject = mRootContextualObject;
        } else {
            mSelectedContextualObject = mSubContexts.get(Integer.parseInt(tabId.substring(4)));
        }
        mToolbar.setTitle(mSelectedContextualObject.getTitle());

        ImageView backgroundView = (ImageView) mContextTab.findViewById(R.id.backgroundView);
        int primaryDark = getActivity().getResources().getColor(R.color.primary_dark);
        // Override the color if needed
        if (mSelectedContextualObject.getColor() != -1) {
            primaryDark = mSelectedContextualObject.getColor();
        }
        backgroundView.setBackgroundColor(primaryDark);

        mContextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(mSelectedContextualObject.getIntent());
            }
        });

        TextView titleView = (TextView) mContextTab.findViewById(R.id.titleView);
        titleView.setText(mSelectedContextualObject.getTitle());
        TextView infoView = (TextView) mContextTab.findViewById(R.id.infoView);
        infoView.setText(mSelectedContextualObject.getInfo());
        ImageView icon = (ImageView) mContextTab.findViewById(R.id.imageView);
        icon.setImageDrawable(mSelectedContextualObject.getIcon(getActivity()));
        icon.setContentDescription(mSelectedContextualObject.getTitle());
        startQuery(true);
    }

    private void startQuery(boolean cached) {
        GetDocumentsListRequest request = (GetDocumentsListRequest) new DocumentsListRequestBuilder(getActivity())
                .setContextualObject(mSelectedContextualObject)
                .build();
        if (cached) {
            mSpiceManager.execute(request, request.createCacheKey(), DurationInMillis.ONE_DAY, this);
        } else {
            mSpiceManager.execute(request, null, 0, this);
        }
        mSwipeLayout.setRefreshing(true);
    }

    private void refreshTabs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> tailedEmails = prefs.getStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, new HashSet<String>());
        mTabHost.clearAllTabs();
        mSubContexts = mRootContextualObject.getSubContexts(tailedEmails);
        mSelectedContextualObject = mRootContextualObject;
        if (mSubContexts != null && !mSubContexts.isEmpty()) {
            // Object has subcontexts.
            TabHost.TabSpec allSpec = mTabHost.newTabSpec(TAB_ALL)
                    .setIndicator(getString(R.string.tab_all))
                    .setContent(CONTEXT_TAB);
            mTabHost.addTab(allSpec);
            for (int i = 0; i < mSubContexts.size(); i++) {
                ContextualObject contextualObject = mSubContexts.get(i);
                TabHost.TabSpec spec = mTabHost.newTabSpec("tab_" + i)
                        .setIndicator(contextualObject.getTitle(), contextualObject.getIcon(getActivity()))
                        .setContent(CONTEXT_TAB);
                mTabHost.addTab(spec);

            }
            for (int i = 0; i < mSubContexts.size() + 1; i++) {
                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(getResources().getColor(R.color.abc_primary_text_material_dark));
            }
            mTabContainer.setVisibility(View.VISIBLE);
        } else {
            // Object is lonely
            TabHost.TabSpec contextSpec = mTabHost.newTabSpec(TAB_ALL)
                    .setIndicator(mRootContextualObject.getTitle())
                    .setContent(CONTEXT_TAB);
            mTabHost.addTab(contextSpec);
            mTabContainer.setVisibility(View.GONE);
        }
        mTabHost.setCurrentTab(0);

        // Update the menu
        // Hide the "Refine context" button when the current context can't be refined
        mToolbar.getMenu().findItem(R.id.action_improve_context).setVisible(mRootContextualObject.getPersons() != null);
    }

    private float clamp(float value) {
        return clamp(value, 0, 1);
    }

    private float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }
}
