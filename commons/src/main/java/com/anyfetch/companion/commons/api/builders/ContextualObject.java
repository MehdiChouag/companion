package com.anyfetch.companion.commons.api.builders;

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
     * @param tailedEmails
     */
    public String getSearchQuery(Set<String> tailedEmails);
}
