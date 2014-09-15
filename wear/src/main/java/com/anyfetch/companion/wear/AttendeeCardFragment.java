package com.anyfetch.companion.wear;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AttendeeCardFragment extends CardFragment implements View.OnClickListener {
    public static AttendeeCardFragment create(CharSequence title, CharSequence text) {
        return AttendeeCardFragment.create(title, text, 0);
    }

    public static AttendeeCardFragment create(CharSequence title, CharSequence text, int icon) {
        AttendeeCardFragment fragment = new AttendeeCardFragment();
        Bundle args = new Bundle();
        args.putCharSequence(CardFragment.KEY_TITLE, title);
        args.putCharSequence(CardFragment.KEY_TEXT, text);
        if(icon != 0) {
            args.putInt(CardFragment.KEY_ICON_RESOURCE, icon);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateContentView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateContentView(inflater, container, savedInstanceState);
        view.setClickable(true);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent menuIntent = new Intent(getActivity(), OpenAttendeeMenuActivity.class);
        // TODO: Add some extras to open the right urls on phone
        startActivity(menuIntent);
    }
}
