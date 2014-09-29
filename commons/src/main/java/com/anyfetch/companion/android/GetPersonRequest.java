package com.anyfetch.companion.android;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * Gets a person
 */
public class GetPersonRequest extends SpiceRequest<Person> {

    private String mEmail = "";
    private long mId = 0;
    private Context mContext;

    public GetPersonRequest(Class<Person> klass, Context context, long id) {
        super(klass);
        mContext = context;
        mId = id;
    }

    public GetPersonRequest(Class<Person> klass, Context context, String email) {
        super(klass);
        mContext = context;
        mEmail = email;
    }

    @Override
    public Person loadDataFromNetwork() throws Exception {
        if(mId > 0) {
            return Person.getPerson(mContext, mId);
        } else {
            return Person.getPerson(mContext, mEmail);
        }
    }
}