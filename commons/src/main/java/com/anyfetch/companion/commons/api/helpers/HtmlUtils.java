package com.anyfetch.companion.commons.api.helpers;

import android.content.Context;
import android.util.Log;

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
     * @param document
     * @return
     */
    public static Boolean requireJavascript(String document) {
        return document.contains("anyfetch-date");
    }

	public static String renderDocument(Context context, String document) {
		if(baseDocumentHtml == null) {
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

    public static String convertHlt(String origin) {
        return origin.replaceAll("<span[^>]+?anyfetch-hlt[^>]+?>(.+?)</span>", "<b>$1</b>");
    }

    public static String selectTag(String origin, String tag) {
        InputStream is = new ByteArrayInputStream(origin.getBytes());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(is);
            return doc.getElementsByTagName(tag).item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String stripHtml(String origin) {
        return origin.replaceAll("</*[^>]+?/*>", "");
    }

}
