package com.anyfetch.companion.android;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

/**
 * Represents a Contact/Attendee
 */
public class Person {
    private final String mName;
    private final List<String> mEmails;
    private final List<String> mNumbers;
    private final Map<String, String> mAccounts;
    private final Bitmap mPhoto;

    /**
     * Creates a new Person
     * @param name Their name
     * @param emails Their emails
     * @param numbers Their phone numbers
     * @param accounts Their web accounts (profiles)
     * @param photo Their photo
     */
    public Person(String name, List<String> emails, List<String> numbers, Map<String, String> accounts, Bitmap photo) {
        this.mName = name;
        this.mEmails = emails;
        this.mNumbers = numbers;
        this.mAccounts = accounts;
        this.mPhoto = photo;
    }

    /**
     * Gets the name
     * @return A name
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the emails
     * @return A list of emails
     */
    public List<String> getEmails() {
        return mEmails;
    }

    /**
     * Gets the phone numbers
     * @return A list of numbers
     */
    public List<String> getNumbers() {
        return mNumbers;
    }

    /**
     * Gets the internet profiles
     * @return A map of services - profile names
     */
    public Map<String, String> getAccounts() {
        return mAccounts;
    }

    /**
     * Gets the image
     * @return A Bitmap
     */
    public Bitmap getPhoto() {
        return mPhoto;
    }

}
