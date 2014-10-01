package com.anyfetch.companion.helpers;

import java.util.Date;
import java.util.List;

/**
 * A section of Dated Items
 */
public class DatedSection {
    private final List<DatedItem> mItems;
    private final Date mSectionStart;

    public DatedSection(List<DatedItem> items, Date sectionStart) {
        mItems = items;
        mSectionStart = sectionStart;
    }

    public List<DatedItem> getItems() {
        return mItems;
    }

    public Date getSectionStart() {
        return mSectionStart;
    }
}
