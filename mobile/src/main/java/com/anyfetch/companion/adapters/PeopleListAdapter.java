package com.anyfetch.companion.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.builders.DocumentsListRequestBuilder;
import com.anyfetch.companion.ui.ImageHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PeopleListAdapter extends BaseAdapter {
    private final List<Person> mPeople;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private Set<String> mTailedEmails;

    public PeopleListAdapter(Context context, List<Person> people) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPeople = people;

        loadTailed();
    }

    @Override
    public int getCount() {
        return mPeople.size();
    }

    @Override
    public Object getItem(int position) {
        return mPeople.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_person, parent, false);
        }
        Person person = mPeople.get(position);

        TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
        if ((person.getName() == null || person.getName().equals("")) && person.getEmails().size() > 0) {
            nameView.setText(person.getEmails().get(0));
        } else {
            nameView.setText(person.getName());
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        if (person.getThumb() != null) {
            imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(person.getThumb(), 200));
        } else {
            imageView.setImageResource(R.drawable.ic_person);
        }

        if (person.isExcluded(mTailedEmails)) {
            nameView.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            imageView.setColorFilter(new ColorMatrixColorFilter(cm));
        } else {
            nameView.setTextColor(mContext.getResources().getColor(android.R.color.black));
            imageView.clearColorFilter();
        }

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        loadTailed();
        super.notifyDataSetChanged();
    }

    private void loadTailed() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mTailedEmails = prefs.getStringSet(DocumentsListRequestBuilder.TAILED_EMAILS, new HashSet<String>());
    }
}
