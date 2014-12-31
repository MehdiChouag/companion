package com.anyfetch.companion.commons.api.helpers;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class AnyfetchHtmlUtilsTest extends InstrumentationTestCase {
    private Context mContext;

    public void test_stripNonImportantHtml() throws Exception {
        assertEquals(" Bob", AnyfetchHtmlUtils.stripNonImportantHtml("<span class=\"anyfetch-number anyfetch-message-count\">3</span> Bob"));
    }

    public void test_htmlToSimpleHtml() throws Exception {
        assertEquals("Bob <b>Beaver</b>", AnyfetchHtmlUtils.htmlToSimpleHtml("<span>Bob</span> <span class=anyfetch-hlt>Beaver</span>"));
    }

    public void test_htmlToSimpleHtmlTitleless() throws Exception {
        assertEquals(" <b>Beaver</b>", AnyfetchHtmlUtils.htmlToSimpleHtmlTitleless("<span>Bob</span> <span class=anyfetch-hlt>Beaver</span>", "Bob"));
    }
}
