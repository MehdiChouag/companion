package com.anyfetch.companion.wear;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.models.Event;

import java.util.List;

public class UpcomingEventsAdapter extends RecyclerView.Adapter {
    private final Activity context;
    private final List<Event> list;

    public UpcomingEventsAdapter(Activity context, List<Event> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflator = context.getLayoutInflater();
        View view = inflator.inflate(R.layout.item_upcoming_event, null);
        Event e = list.get(i);

        ((TextView) view.findViewById(R.id.event_title)).setText(e.getTitle());
        // TODO: Better date formatting
        ((TextView) view.findViewById(R.id.event_date)).setText(e.getStart().getHours() + ":" +e.getStart().getMinutes());

        return new WearableListView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
