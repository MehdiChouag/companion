package com.anyfetch.companion.commons.api.builders;

import java.util.Map;
import java.util.Set;

/**
 * Represents an object that can be used as a context for a search
 */
public interface ContextualObject {
    /**
     * Gets the title of the context
     *
     * @return A title
     */
    public String getTitle();

    /**
     * Gets the search query
     *
     * @return An Anyfetch search query
     * @param tailedEmails The emails ignored by the system
     */
    public String getSearchQuery(Set<String> tailedEmails);

    /**
     * Get additional search queries
     *
     * @param tailedEmails The emails ignored by the system
     * @return A map between the additional query names and their actual search phrases
     * or null if there is no additional context
     */
    public Map<String, String> getAdditionalSearchQueries(Set<String> tailedEmails);
}
