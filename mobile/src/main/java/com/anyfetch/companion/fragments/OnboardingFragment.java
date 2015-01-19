package com.anyfetch.companion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anyfetch.companion.R;

/**
 * Created by neamar on 1/19/15.
 */
public class OnboardingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_onboarding, container, false);

        return rootView;
    }

    public void setResources(int imageResource, int stringResource) {

    }
}