package com.anyfetch.companion.api.android;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.anyfetch.companion.android.Person;
import com.anyfetch.companion.api.helpers.AndroidServicesMockInjecter;

public class PersonTest  extends InstrumentationTestCase {
    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        AndroidServicesMockInjecter.injectContact(mContext);
    }

    public void test_getPerson_email() throws Exception {
        Person person = Person.getPerson(mContext, "sarcher@gmail.com");

        assertEquals("Sterling Archer", person.getName());
    }
}