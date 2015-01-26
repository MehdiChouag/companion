package com.anyfetch.companion.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anyfetch.companion.R;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by neamar on 1/19/15.
 */
public class OnboardingFragment extends Fragment {
    public final static String LAYOUT_RESOURCE = "layoutResource";
    public final static String PREF_ONBOARDING_DISPLAYED = "hasDisplayedOnboarding";

    private int mLayoutResource;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        final Bundle args = getArguments();
        mLayoutResource = args.getInt(LAYOUT_RESOURCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(mLayoutResource, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(PREF_ONBOARDING_DISPLAYED, true);
                    editor.apply();

                    getActivity().finish();
                }
            });
        }

        return rootView;
    }
}