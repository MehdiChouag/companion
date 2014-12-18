package com.anyfetch.companion.commons.api.builders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import java.util.List;
import java.util.Set;

/**
 * Represents an object that can be used as a context for a search
 */
public interface ContextualObject extends Parcelable {
    /**
     * Gets the title of the context
     *
     * @return A title
     */
    public String getTitle();

    /**
     * Gets the associated icon
     *
     * @param context The context for resources
     * @return The icon
     */
    public Drawable getIcon(Context context);

    /**
     * Gets basic information about the context
     *
     * @return Infos
     */
    public String getInfo();

    /**
     * Color for the contextual object (e.g. event color)
     *
     * @return a color, or -1 to use the default primaryDark color
     */
    public int getColor();

    /**
     * Gets the search query
     *
     * @param tailedEmails The emails ignored by the system
     * @return An Anyfetch search query
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
