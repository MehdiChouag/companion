package com.anyfetch.companion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by neamar on 1/19/15.
 */
public class OnboardingFragment extends Fragment {
    public final static String LAYOUT_RESOURCE = "layoutResource";

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
        return rootView;
    }
}