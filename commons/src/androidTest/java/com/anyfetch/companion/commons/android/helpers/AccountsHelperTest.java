package com.anyfetch.companion.commons.android.helpers;

import android.test.InstrumentationTestCase;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountsHelperTest extends InstrumentationTestCase {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private AccountsHelper mHelper;
    private Pattern mPattern;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mHelper = new AccountsHelper();
        mPattern = Pattern.compile(EMAIL_PATTERN);
    }

    public void test_getOwnerEmails() throws Exception {
        Set<String> emails = mHelper.getOwnerEmails(this.getInstrumentation().getContext());
        for (String email : emails) {
            Matcher matcher = mPattern.matcher(email);
            assertTrue(matcher.matches());
        }
    }
}
