package com.anyfetch.companion.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Various utility functions
 */
public class Utilities {
    /**
     * Split a <strong>sorted</strong> list of items by days
     * @param items The dated items to process
     * @return Dated sections
     */
    public static List<DatedSection> classifyInDatedSections(List<DatedItem> items) {
        List<DatedSection> sections = new ArrayList<DatedSection>();
        List<DatedItem> currentSectionBuffer = new ArrayList<DatedItem>();
        DatedItem lastItem;
        if(items.size() > 0) {
            lastItem = items.get(0);
            for (DatedItem currentItem : items) {
                Date last = lastItem.getDate();
                Date current = currentItem.getDate();
                if(last.getYear() != current.getYear() ||
                   last.getMonth() != current.getMonth() ||
                   last.getDate() != current.getDate()) {
                    sections.add(new DatedSection(currentSectionBuffer, currentSectionBuffer.get(0).getDate()));
                    currentSectionBuffer = new ArrayList<DatedItem>();

                }
                currentSectionBuffer.add(currentItem);
                lastItem = currentItem;
            }
            sections.add(new DatedSection(currentSectionBuffer, currentSectionBuffer.get(0).getDate()));
        }
        return sections;
    }
}
