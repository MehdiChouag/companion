package com.anyfetch.companion.fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anyfetch.companion.R;
import com.anyfetch.companion.adapters.PeopleListAdapter;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonChooserFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PEOPLE = "people";

    // TODO: Rename and change types of parameters
    private List<Person> mPeople;
    private ListView mListView;
    private PeopleListAdapter mPeopleAdapter;
    private DialogFragmentChangeListener mDialogFragmentChangeListener = null;


    public PersonChooserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param people The people attending the event
     * @return A new instance of fragment PersonChooserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonChooserFragment newInstance(List<Person> people) {
        PersonChooserFragment fragment = new PersonChooserFragment();
        Bundle args = new Bundle();
        Person[] peopleArray = new Person[people.size()]; // you like it java ? Is it typesafe enough ?
        for (int i = 0; i < people.size(); i++) {
            peopleArray[i] = people.get(i);
        }
        args.putParcelableArray(ARG_PEOPLE, peopleArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPeople = new ArrayList<Person>();
            Collections.addAll(mPeople, (Person[]) getArguments().getParcelableArray(ARG_PEOPLE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getActivity().getString(R.string.choose_context_attendees));
        getDialog().setCancelable(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_chooser, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mPeopleAdapter = new PeopleListAdapter(getActivity(), mPeople);
        mListView.setAdapter(mPeopleAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> emails = prefs.getStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, new HashSet<String>());
        Person person = mPeople.get(position);

        if (person.isExcluded(emails)) {
            for (String email : person.getEmails()) {
                emails.remove(email);
            }
        } else {
            for (String email : person.getEmails()) {
                emails.add(email);
            }
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, emails);
        editor.commit();
        if (mDialogFragmentChangeListener != null) {
            mDialogFragmentChangeListener.onDialogFragmentChanged();
        }
        mPeopleAdapter.notifyDataSetChanged();
    }

    public void setFragmentChangeListener(DialogFragmentChangeListener dialogFragmentChangeListener) {
        mDialogFragmentChangeListener = dialogFragmentChangeListener;
    }
}
