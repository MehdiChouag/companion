package com.anyfetch.companion.helpers;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilitiesTest extends InstrumentationTestCase {
    public void test_classifyInDatedSections() throws Exception {
        List<DatedItem> items = new ArrayList<DatedItem>();
        items.add(new DatedItem() {
            @Override
            public Date getDate() {
                return new Date();
            }
        });
        items.add(new DatedItem() {
            @Override
            public Date getDate() {
                return new Date(new Date().getTime() + 1000 * 60 * 60);
            }
        });
        items.add(new DatedItem() {
            @Override
            public Date getDate() {
                return new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 2);
            }
        });

        List<DatedSection> sections = Utilities.classifyInDatedSections(items);

        assertEquals(2, sections.size());
        assertEquals(2, sections.get(0).getItems().size());
        assertEquals(1, sections.get(1).getItems().size());
    }
}
