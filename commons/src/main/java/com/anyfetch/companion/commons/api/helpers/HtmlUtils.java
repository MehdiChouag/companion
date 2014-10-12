package com.anyfetch.companion.commons.api.helpers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tools for helping with HTML-Related stuff
 */
public class HtmlUtils {
    public static final String CSS = "article.anyfetch-document-snippet{margin:0;padding:.5em}article.anyfetch-document-snippet figure,article.anyfetch-document-snippet p,article.anyfetch-document-snippet>*{margin:.2em;padding:0}article.anyfetch-document-snippet main.anyfetch-content{font-weight:lighter}article.anyfetch-document-snippet h1.anyfetch-title{font-size:1em;font-weight:700;text-overflow:ellipsis;overflow:hidden;width:100%;white-space:nowrap;max-height:1.5em;margin:0}article.anyfetch-document-full header.anyfetch-header{background-color:#fbfbfb;border-bottom:solid 1px #d3d3d3;padding:.5em}article.anyfetch-document-full header.anyfetch-header .anyfetch-title-group h1.anyfetch-title{font-size:1.4em;color:#444;margin-top:0;margin-bottom:.4em}article.anyfetch-document-full header.anyfetch-header .anyfetch-title-group p.anyfetch-title-detail{margin-top:.4em;margin-bottom:.4em}article.anyfetch-document-full main.anyfetch-content{padding:.7em}.anyfetch-mobile-scroll article.anyfetch-document-full main.anyfetch-content{overflow-x:auto}article.anyfetch-document-full article.anyfetch-thread-part{margin:-.5em}header.anyfetch-header:after,header.anyfetch-header:before{content:\"\";display:table}header.anyfetch-header:after{clear:both}header.anyfetch-header{zoom:1}h1.anyfetch-title .anyfetch-number{display:inline-block;padding-left:.4em;padding-right:.4em;border-radius:1em;color:#646464;border:1px solid #646464;font-size:.8em;margin-right:.5em}.anyfetch-title-detail{color:gray}figure.anyfetch-aside-image{height:4em;float:left}figure.anyfetch-aside-image img{height:100%}ul.anyfetch-pill-list{display:inline-block;white-space:nowrap;overflow-x:hidden;width:100%;margin:0;padding:.1em 0;mask-image:linear-gradient(to right,rgba(2,255,255,1) 95%,rgba(255,255,255,0) 100%);-webkit-mask-image:linear-gradient(to right,rgba(2,255,255,1) 95%,rgba(255,255,255,0) 100%)}ul.anyfetch-pill-list li{display:inline-block;list-style:none}.anyfetch-mobile-scroll ul.anyfetch-pill-list{overflow-x:auto;overflow-y:hidden;padding-right:5%;width:95%}li.anyfetch-pill{font-size:.75em;padding:.2em .5em;margin:0;border-radius:1em;color:gray;border:1px solid #a9a9a9}span.anyfetch-hlt{background-color:#ffffe0}.anyfetch-right-arrow{display:inline}.anyfetch-right-arrow:before{color:gray;content:\"→\"}.anyfetch-email:before{content:\"<\"}.anyfetch-email:after{content:\">\"}.anyfetch-date{font-style:italic}article.anyfetch-document-snippet.anyfetch-type-contact h1.anyfetch-title,article.anyfetch-document-snippet.anyfetch-type-image h1.anyfetch-title{width:auto}.anyfetch-icon-people{display:inline-block;width:1em;height:1em;background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAAQCAYAAAAbBi9cAAAA+ElEQVQ4y83SMUvcQRQE8J931ukkrVdpFQjBfksFC7UWJHDfwD4MyZdIbxWwEBVL2eIQLLQQxM5CSLC0EBQUz+ognPc/7tLErR5vdue9mVne25lpApLMYBXraGE3yWHT/daYIT+wj6/YwkGSTLVRknlcj8Bf0ElyM+lGXxqGtPB5Gml/xkj+PQ3RKXoj+sc4G/WgPapZa+2XUvYwhw4esINuksf/9o8+4BM+oo9bXCS5n4goyRK+YRmzQ/ATjvA9yXmjR0m28QsLDUG0sYhuKeWu1nr6hijJJn6OkzuU9kop5arWejkc/9o/eLwxKP72oIfnKYlOBsUrMvs/CqmM4RAAAAAASUVORK5CYII=);background-repeat:no-repeat;background-position:bottom;background-size:1em;margin-right:.5em;margin-left:-.2em}.anyfetch-icon-related{padding-left:9px;background:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAKCAYAAABmBXS+AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3gkWEAQrSHQi+QAAAJ9JREFUGNNt0LEKgXEUxuEHszIYbFaXwKYMRgwG1yDFVZDIDVDKogyfUVFyBWY2N0AZDVj+6uvLqVPnnH6d97yH/9FChCWyqT/AADlscMExnQAa+CCPZ5i940Ada+xRRBpbFH6bKiGhjxuO2GEIpaA/QjmAE1Qx/8lMsUAt9Ds0cYjfEuEa6jE6WCUtz9DDI/znlAQyOKONe3DUxSsOfQEQAh3RFNkN8wAAAABJRU5ErkJggg==) no-repeat;padding-bottom:10px}";
    public static final String HEADER =
            "<!doctype html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='utf-8'>" +
                    "<style type='text/css'>" + CSS + "</style>" +
                    "</head>" +
                    "<body style='font-family: Roboto; font-size: 13px; margin: 0; padding: 0;'>";
    public static final String FOOTER =
            "</body>" +
                    "</html>";

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
