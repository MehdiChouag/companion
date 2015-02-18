package com.anyfetch.companion.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anyfetch.companion.ContextActivity;
import com.anyfetch.companion.R;
import com.anyfetch.companion.adapters.ContactCursorAdapter;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.stats.MixPanel;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

/**
 * Created by mehdichouag on 16/02/15.
 */
public class ContactPickerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final int LOADER_CONTACT_ID = 16021346;

    private MixpanelAPI mixpanel;

    private ContactCursorAdapter mAdapter;

    private String mCurFilter;

    // Ui
    private ListView mListView;
    private SearchView mSearchView;
    private OnSearchView mListener;

    public interface OnSearchView {
        public void showSlidingTabLayout();
        public void hideSlidingTabLayout();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnSearchView) {
            mListener = (OnSearchView) activity;
        }
    }

    public ContactPickerFragment() {

    }

    public static Fragment newInstance() {
        ContactPickerFragment fragment = new ContactPickerFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_CONTACT_ID, null, this);
        mixpanel = MixPanel.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_contact_picker, container, false);

        bindView(rootView);

        return rootView;
    }

    private void bindView(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.contact_list);

        mAdapter = new ContactCursorAdapter(getActivity(), null, 0);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        inflater.inflate(R.menu.contact, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_contact_hint));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(mClose);
        mSearchView.setOnSearchClickListener(mSearchClikListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mSearchView.onActionViewCollapsed();
                mSearchView.setQuery("", false);
                mCurFilter = null;
                mListView.setSelection(0);
                if (mListener != null)
                    mListener.showSlidingTabLayout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_CONTACT_ID: {
                Uri baseUri;
                if (mCurFilter != null) {
                    baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                            Uri.encode(mCurFilter));
                } else {
                    baseUri = ContactsContract.Contacts.CONTENT_URI;
                }
                return new CursorLoader(getActivity(), baseUri,
                        ContactCursorAdapter.PROJ_CONTACT_LIST.COLS,
                        ContactCursorAdapter.PROJ_CONTACT_LIST.SELECT,
                        null,
                        ContactCursorAdapter.PROJ_CONTACT_LIST.SORT);
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_CONTACT_ID:
                mAdapter.swapCursor(data, mCurFilter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor cursor = mAdapter.getCursor();

        if (cursor.moveToPosition(position)) {
            final int columnID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            final long personId = cursor.getLong(columnID);

            Log.i("PersonPicker", "User picked contact " + personId);
            mixpanel.track("Pick contact", new JSONObject());
            Person person = Person.getPerson(getActivity(), personId);

            Intent intent = new Intent(getActivity().getApplicationContext(), ContextActivity.class);
            intent.putExtra(ContextFragment.ARG_CONTEXTUAL_OBJECT, person);
            intent.putExtra(ContextActivity.ORIGIN, "contactPicker");
            startActivity(intent);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        setFastScroll(mCurFilter == null);
        getLoaderManager().restartLoader(LOADER_CONTACT_ID, null, this);
        return true;
    }

    SearchView.OnCloseListener mClose = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            mSearchView.setQuery("", false);
            mCurFilter = null;
            return true;
        }
    };

    SearchView.OnClickListener mSearchClikListener = new SearchView.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (mListener != null)
                mListener.hideSlidingTabLayout();
        }
    };

    private void setFastScroll(boolean value) {
        mListView.setFastScrollEnabled(value);
        mListView.setFastScrollAlwaysVisible(value);
    }
}
