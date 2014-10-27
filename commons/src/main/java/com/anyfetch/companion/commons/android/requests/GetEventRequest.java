package com.anyfetch.companion.commons.android.requests;

import android.content.Context;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Event;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Gets an event
 */
public class GetEventRequest extends SpiceRequest<Event> {
    private static final String TAG = "GetEventRequest";
    private final long mId;
    private final Context mContext;

    public GetEventRequest(Context context, long id) {
        super(Event.class);
        Log.i(TAG, "Create Single Event Request");
        Log.i(TAG, "ID: " + id);
        mContext = context;
        mId = id;
    }

    @Override
    public Event loadDataFromNetwork() throws Exception {
        Log.i(TAG, "Load: " + mId);
        return Event.getEvent(mContext, mId);
    }

    public String createCacheKey() {
        return "event." + mId;
    }
}

