package com.anyfetch.companion.commons.api.builders;

import android.content.Context;
import android.test.InstrumentationTestCase;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.anyfetch.companion.commons.api.helpers.BaseRequest;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.anyfetch.companion.commons.api.requests.GetDocumentsListRequest;

import java.util.ArrayList;
import java.util.Date;

public class DocumentsListRequestBuilderTest extends InstrumentationTestCase {
    private Context mContext;
    private Event mEvent;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        mEvent = new Event(1, "a", "b", new Date(0), new Date(0), new ArrayList<Person>(), "c", 0);
    }

    public void test_build_simple() throws Exception {
        BaseRequest<DocumentsList> req = new DocumentsListRequestBuilder(mContext)
                .setContextualObject(mEvent)
                .build();
        assertTrue(req instanceof GetDocumentsListRequest);
    }

    // TODO: other tests with the important & not important when implemented
}
