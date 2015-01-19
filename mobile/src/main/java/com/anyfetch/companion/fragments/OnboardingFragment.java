package com.anyfetch.companion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyfetch.companion.R;

/**
 * Created by neamar on 1/19/15.
 */
public class OnboardingFragment extends Fragment {
    private ViewGroup mRootView;
    private int mImageResource;
    private int mStringResource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_onboarding, container, false);

        ImageView image = (ImageView) mRootView.findViewById(R.id.onboardingImage);
        image.setImageResource(mImageResource);

        TextView text = (TextView) mRootView.findViewById(R.id.onboardingText);
        text.setText(mStringResource);
        return mRootView;
    }

    public void setResources(int imageResource, int stringResource) {
        mImageResource = imageResource;
        mStringResource = stringResource;
    }
}