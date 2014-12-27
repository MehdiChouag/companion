package com.anyfetch.companion.commons.api.helpers;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tools for helping with HTML-Related stuff
 */
public class HtmlUtils {
    public static final String DOCUMENT_PLACEHOLDER = "{{document}}";
    public static final String LOCALE_PLACEHOLDER = "{{locale}}";
    public static String baseDocumentHtml = null;

    /**
     * Will return true if the specified document require some JS to be formatted.
     *
     * @param document The document's string
     * @return Whether it will need JS or not
     */
    public static Boolean requireJavascript(String document) {
        return document.contains("anyfetch-date");
    }

    /**
     * Renders a document in it's proper context
     *
     * @param context  The android context
     * @param document The document to inject
     * @return A complete HTML page with CSS(+JS)
     */
    public static String renderDocument(Context context, String document) {
        if (baseDocumentHtml == null) {
            // Preload the HTML file from assets
            try {
                InputStream fin = context.getAssets().open("document.html");
                byte[] buffer = new byte[fin.available()];
                fin.read(buffer);
                fin.close();

                baseDocumentHtml = new String(buffer);
            } catch (IOException e) {
                Log.e("WTF", e.toString());
                baseDocumentHtml = DOCUMENT_PLACEHOLDER;
            }
        }

        String languageCode = Locale.getDefault().getLanguage();
        return baseDocumentHtml.replace(DOCUMENT_PLACEHOLDER, document).replace(LOCALE_PLACEHOLDER, languageCode);
    }

    /**
     * Convert highlights into simple bold text (for wear)
     *
     * @param origin The original highlighted text
     * @return The bolded text
     */
    public static String convertHlt(String origin) {
        return origin.replaceAll("<span[^>]+?anyfetch-hlt[^>]+?>(.+?)</span>", "<b>$1</b>");
    }

    /**
     * Selects a specific tag in the DOM
     *
     * @param origin The HTML to be selected from
     * @param tag    The tag to select
     * @return The content of the tag
     */
    public static String selectTag(String origin, String tag) {
        InputStream is = new ByteArrayInputStream(origin.getBytes());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(is);
            Node node = doc.getElementsByTagName(tag).item(0);
            if (node != null) {
                return node.getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Strip HTML tags from a string
     *
     * @param origin An HTML string
     * @return A text
     */
    public static String stripHtml(String origin) {
        return origin.replaceAll("</*[^>]+?/*>", "");
    }

    /**
     * Try to basically format the raw text with line feeds
     * @param origin
     * @return a text whose only markup is <br> tag
     */
    public static String stripHtmlKeepLineFeed(String origin) {
        origin = origin.replace("</p>", "</p>\n\n");
        origin = origin.replace("</ul>", "</ul>\n\n");

        // Strip all html
        origin = stripHtml(origin);

        // Restore line breaks
        origin = origin.replace("\r", "");
        origin = origin.replace("\n", "<br>");
        return origin;
    }

    /**
     * TODO This is ugly :(
     * We use this function to "format" AnyFetch HTML code for stuff we now to be of little interest in raw text.
     *
     * In the best of worlds, this could be some markup class indicating what is useful and what is not.
     * In the best of world this would be done with DOM selectors instead of regexp,
     * but org.w3c.dom does not contain CSS selectors (or even class selector)
     * @param origin a HTML with anyfetch-namespacing
     * @return a html with less anyfetch-namespacing, ready to be use with stripHtml
     */
    public static String stripNonImportantAnyfetchHtml(String origin) {
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
}
