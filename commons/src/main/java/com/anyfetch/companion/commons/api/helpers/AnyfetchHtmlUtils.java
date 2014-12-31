package com.anyfetch.companion.commons.api.helpers;

import java.util.regex.Pattern;

public class AnyfetchHtmlUtils {
    /**
     * TODO This is ugly :(
     * We use this function to "format" AnyFetch HTML code for stuff we now to be of little interest in raw text.
     *
     * In the best of world, this could be some markup class indicating what is useful and what is not.
     * In the best of world this would be done with DOM selectors instead of regexp,
     * but org.w3c.dom does not contain CSS selectors (or even class selector)
     * @param origin a HTML with anyfetch-namespacing
     * @return a html with less anyfetch-namespacing, ready to be use with stripHtml
     */
    public static String stripNonImportantHtml(String origin) {
        // Number of message in an email thread.
        // <span class="anyfetch-number anyfetch-message-count">3</span>
        origin = origin.replaceAll("<[^>]+anyfetch-message-count.+?</.+?>", "");

        // Number of participants in an email thread.
        // <li class=anyfetch-title-detail>2 <span class=anyfetch-icon-people></span></li>
        origin = origin.replaceAll("<li[^>]+anyfetch-title-detail[^/]+anyfetch-icon-people.+?</li>", "");

        // End date for time-span
        // <p class="anyfetch-title-detail anyfetch-date-span"><time class=anyfetch-date>2014-12-27T18:30:00.000Z</time> <span class=anyfetch-right-arrow></span> <time class=anyfetch-date>2014-12-27T22:00:00.000Z</time></p>
        origin = origin.replaceAll("(anyfetch-date-span.+?</time>).+?</p>", "$1</p>");

        // UTC dates
        // <time class=anyfetch-date>2014-12-27T18:30:00.000Z</time>
        origin = origin.replaceAll("\\.000Z</time>", "</time>");

        return origin;
    }

    /**
     * Replace all HTML, only keeps .hlt markup and replace it with bold
     * @param origin
     * @return
     */
    public static String htmlToSimpleHtml(String origin) {
        // Preserve .hlt markup
        origin = origin.replaceAll("<span[^>]+?anyfetch-hlt[^>]*?>(.+?)</span>", "{{[$1]}}");

        origin = AnyfetchHtmlUtils.stripNonImportantHtml(origin);
        origin = HtmlUtils.stripHtmlKeepLineFeed(origin);

        // Restore .hlt markup
        origin = origin.replaceAll(Pattern.quote("{{[") + "(.+?)" + Pattern.quote("]}}"), "<b>$1</b>");
        return origin;
    }

    /**
     * Replace all HTML, only keeps .hlt markup and replace it with bold
     * @param snippet
     * @return
     */
    public static String htmlToSimpleHtmlTitleless(String snippet, String title) {
        // Remove the first appearance of the title (thus the "titleless"), assuming the text will be displayed with the title above (in notifications for instance)
        snippet = snippet.replaceFirst(Pattern.quote(title), "");

        return htmlToSimpleHtml(snippet);
    }
}
