package com.anyfetch.companion.commons.api.helpers;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class HtmlUtilsTest extends InstrumentationTestCase {
    private Context mContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
    }

    public void test_requireJavascript() throws Exception {
        assertTrue(HtmlUtils.requireJavascript("hello <span bob class='anyfetch-date'>date</span>"));
        assertFalse(HtmlUtils.requireJavascript("hello date"));
    }

    public void test_renderDocument() throws Exception {
        String render = HtmlUtils.renderDocument(mContext, "Hello");
        assertTrue(render.length() > "Hello".length());
    }

    public void test_convertHlt() throws Exception {
        assertEquals("<b>Hello</b> world", HtmlUtils.convertHlt("<span class='anyfetch-hlt'>Hello</span> world"));
    }

    public void test_selectTag() throws Exception {
        assertEquals("inside", HtmlUtils.selectTag("<html>out<a>inside</a>side</html>", "a"));
    }

    public void test_stripHtml() throws Exception {
        assertEquals("outinsideside", HtmlUtils.stripHtml("<html bob>out<a class='bob'>inside</a>side</html>"));
    }

    public void test_stripHtmlKeepLineFeed() throws Exception {
        assertEquals("line1<br><br>line2", HtmlUtils.stripHtmlKeepLineFeed("<p class='bob'>line1</p>line2"));
    }
}
