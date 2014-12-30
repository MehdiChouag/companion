package com.anyfetch.companion.commons.api.helpers;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class AnyfetchHtmlUtilsTest extends InstrumentationTestCase {
    private Context mContext;

    public void test_stripNonImportantHtml() throws Exception {
        assertEquals(" Bob", AnyfetchHtmlUtils.stripNonImportantHtml("<span class=\"anyfetch-number anyfetch-message-count\">3</span> Bob"));
    }
}
