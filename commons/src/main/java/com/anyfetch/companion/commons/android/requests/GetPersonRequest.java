package com.anyfetch.companion.commons.android.requests;

import android.content.Context;
import android.util.Log;

import com.anyfetch.companion.commons.android.pojo.Person;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Gets a person
 */
public class GetPersonRequest extends SpiceRequest<Person> {

    private static final String TAG = "GetPersonRequest";
    private final Context mContext;
    private String mEmail = "";
    private long mId = 0;

    public GetPersonRequest(Class<Person> klass, Context context, long id) {
        super(klass);
        Log.i(TAG, "Create Person Event Request");
        Log.i(TAG, "ID: " + id);
        mContext = context;
        mId = id;
    }

    public GetPersonRequest(Class<Person> klass, Context context, String email) {
        super(klass);
        Log.i(TAG, "Create Person Event Request");
        Log.i(TAG, "Email: " + email);
        mContext = context;
        mEmail = email;
    }

    @Override
    public Person loadDataFromNetwork() throws Exception {
        if (mId > 0) {
            Log.i(TAG, "Load: " + mId);
            return Person.getPerson(mContext, mId);
        } else {
            Log.i(TAG, "Load: " + mEmail);
            return Person.getPersonByEmail(mContext, mEmail);
        }
    }

    public String createCacheKey() {
        return "person." + mId;
    }
}