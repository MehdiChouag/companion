package com.anyfetch.companion.commons.android.pojo;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.Suppress;

import com.anyfetch.companion.commons.android.helpers.AndroidServicesMockInjecter;

public class PersonTest extends InstrumentationTestCase {
    private Context mContext;
    private long mId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        mId = AndroidServicesMockInjecter.injectContact(mContext);
    }

    public void test_getPerson_email() throws Exception {
        Person person = Person.getPerson(mContext, "sarcher@gmail.com");
        mId = person.getId();

        assertEquals("Sterling Archer", person.getName());
        assertEquals(2, person.getEmails().size());
        assertEquals(1, person.getNumbers().size());
        assertEquals("ISIS", person.getCompany());
        assertEquals("Secret Agent", person.getJob());
        assertEquals("sterling@isis.org", person.getEmails().get(0));
    }

    @Suppress
    public void test_getPerson_id() throws Exception {
        Person person = Person.getPerson(mContext, mId);

        assertEquals("Sterling Archer", person.getName());
    }
}