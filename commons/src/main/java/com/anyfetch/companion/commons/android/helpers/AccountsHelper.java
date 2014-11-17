package com.anyfetch.companion.commons.android.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Enables account management
 */
public class AccountsHelper {
    /**
     * Gets the email associated in the current phone
     *
     * @param context An Android Context
     * @return A list of the emails in the phone's accounts
     */
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
