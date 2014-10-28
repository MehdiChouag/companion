package com.anyfetch.companion.commons.api.builders;

import java.util.List;
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
     * Gets basic information about the context
     *
     * @return Infos
     */
    public String getInfo();

    /**
     * Gets the search query
     *
     * @return An Anyfetch search query
     * @param tailedEmails The emails ignored by the system
     */
    public String getSearchQuery(Set<String> tailedEmails);

    /**
     * Get nested contexts
     *
     * @param tailedEmails The emails ignored by the system
     * @return A list of subcontexts or null if there is no additional context
     */
    public List<ContextualObject> getSubContexts(Set<String> tailedEmails);
}
