package com.anyfetch.companion.commons.android.pojo;

import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import com.anyfetch.companion.commons.android.testhelpers.AndroidServicesMockInjecter;

import java.util.ArrayList;
import java.util.HashSet;

public class PersonTest extends InstrumentationTestCase {
    private Context mContext;
    private Person mPerson;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        mPerson = new Person(1, "a", "b", "c", new ArrayList<String>(), new ArrayList<String>(), null, 2);
    }

    public void test_getPerson_email() throws Exception {
        AndroidServicesMockInjecter.injectContact(mContext);
        Person person = Person.getPerson(mContext, "sarcher@example.com");

        assertEquals("Sterling Archer", person.getName());
        assertEquals(2, person.getEmails().size());
        assertEquals(1, person.getNumbers().size());
        assertEquals("Example", person.getCompany());
        assertEquals("Secret Agent", person.getJob());
        assertEquals("sterling@example.com", person.getEmails().get(0));
    }

    public void test_getPerson_id() throws Exception {
        long id = AndroidServicesMockInjecter.injectContact(mContext);
        Person person = Person.getPerson(mContext, id);

        assertEquals("Sterling Archer", person.getName());
    }

    public void testParcels() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcel", mPerson);
        Person target = bundle.getParcelable("parcel");
        assertEquals(mPerson.getId(), target.getId());
        assertEquals(mPerson.getName(), target.getName());
        assertEquals(mPerson.getCompany(), target.getCompany());
    }

    public void test_getTitle() throws Exception {
        assertEquals(mPerson.getTitle(), mPerson.getName());
    }

    public void test_getId() throws Exception {
        assertEquals(mPerson.getId(), 1);
    }

    public void test_getInfo() throws Exception {
        assertEquals(mPerson.getInfo(), "c, b");
    }

    public void test_getSearchQuery() throws Exception {
        assertEquals(mPerson.getSearchQuery(new HashSet<String>()), "(a)");
    }
}