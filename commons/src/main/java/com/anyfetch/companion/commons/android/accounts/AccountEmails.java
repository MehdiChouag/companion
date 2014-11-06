package com.anyfetch.companion.commons.android.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by neamar on 11/6/14.
 */
public class AccountEmails {
    public HashSet<String> getOwnerEmails(Context context) {
        // http://stackoverflow.com/questions/2112965/how-to-get-the-android-devices-primary-e-mail-address
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccounts();

        HashSet<String> ownerEmails = new HashSet<String>();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                ownerEmails.add(account.name);
            }
        }

        return ownerEmails;
    }
}
