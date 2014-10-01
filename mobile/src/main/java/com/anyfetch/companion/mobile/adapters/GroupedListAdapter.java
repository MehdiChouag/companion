package com.anyfetch.companion.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anyfetch.companion.mobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: doc
 */
public abstract class GroupedListAdapter<T> extends ArrayAdapter<T> {
    private final Context mContext;
    private HashMap<String, List<T>> mSections;
    private List<String> mSectionsOrder;

    @Override
    public boolean isEnabled(int position) {
        int counted = 0;
        for(String sectionName : mSectionsOrder) {
            List<T> elements = mSections.get(sectionName);
            if(counted == position) { // header
                return false;
            } else if(counted < position && position <= counted + elements.size()) { // element
                return true;
            }
            counted += 1 + elements.size();
        }
        return false;
    }

    public GroupedListAdapter(Context context, int resource, List<T> elements) {
        super(context, resource);
        mContext = context;
        mSections = new HashMap<String, List<T>>();
        mSectionsOrder = new ArrayList<String>();
        for(T element : elements) {
            String sectionName = getSection(element);
            if(!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, new ArrayList<T>());
                mSectionsOrder.add(sectionName);
            }
            mSections.get(sectionName).add(element);
        }
    }

    protected abstract String getSection(T element);
    protected abstract View getView(T element, View convertView, ViewGroup parent);

    @Override
    public int getCount() {
        int count = 0;
        for(String sectionName : mSectionsOrder) {
            count ++;
            count += mSections.get(sectionName).size();
        }
        return count;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T element = null;
        String section = "";

        int counted = 0;
        for(String sectionName : mSectionsOrder) {
            List<T> elements = mSections.get(sectionName);
            if(counted == position) { // header
                section = sectionName;
                break;
            } else if(counted < position && position <= counted + elements.size()) { // element
                element = elements.get(position - 1 - counted);
                break;
            }
            counted += 1 + elements.size();
        }

        if(element == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View headerView = inflater.inflate(R.layout.row_group_header, parent, false);
            TextView titleView = (TextView) headerView.findViewById(R.id.titleView);
            titleView.setText(section);
            return headerView;
        } else {
            return getView(element, convertView, parent);
        }



    }
}
