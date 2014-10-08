package com.anyfetch.companion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.anyfetch.companion.R;
import com.anyfetch.companion.commons.api.pojo.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DocumentsListAdapter extends GroupedListAdapter<Document> {
    private final String CSS = "article.anyfetch-document-snippet{margin:0;padding:.5em}article.anyfetch-document-snippet figure,article.anyfetch-document-snippet p,article.anyfetch-document-snippet>*{margin:.2em;padding:0}article.anyfetch-document-snippet main.anyfetch-content{font-weight:lighter}article.anyfetch-document-snippet h1.anyfetch-title{font-size:1em;font-weight:700;text-overflow:ellipsis;overflow:hidden;width:100%;white-space:nowrap;max-height:1.5em;margin:0}article.anyfetch-document-full header.anyfetch-header{background-color:#fbfbfb;border-bottom:solid 1px #d3d3d3;padding:.5em}article.anyfetch-document-full header.anyfetch-header .anyfetch-title-group h1.anyfetch-title{font-size:1.4em;color:#444;margin-top:0;margin-bottom:.4em}article.anyfetch-document-full header.anyfetch-header .anyfetch-title-group p.anyfetch-title-detail{margin-top:.4em;margin-bottom:.4em}article.anyfetch-document-full main.anyfetch-content{padding:.7em}.anyfetch-mobile-scroll article.anyfetch-document-full main.anyfetch-content{overflow-x:auto}article.anyfetch-document-full article.anyfetch-thread-part{margin:-.5em}header.anyfetch-header:after,header.anyfetch-header:before{content:\"\";display:table}header.anyfetch-header:after{clear:both}header.anyfetch-header{zoom:1}h1.anyfetch-title .anyfetch-number{display:inline-block;padding-left:.4em;padding-right:.4em;border-radius:1em;color:#646464;border:1px solid #646464;font-size:.8em;margin-right:.5em}.anyfetch-title-detail{color:gray}figure.anyfetch-aside-image{height:4em;float:left}figure.anyfetch-aside-image img{height:100%}ul.anyfetch-pill-list{display:inline-block;white-space:nowrap;overflow-x:hidden;width:100%;margin:0;padding:.1em 0;mask-image:linear-gradient(to right,rgba(2,255,255,1) 95%,rgba(255,255,255,0) 100%);-webkit-mask-image:linear-gradient(to right,rgba(2,255,255,1) 95%,rgba(255,255,255,0) 100%)}ul.anyfetch-pill-list li{display:inline-block;list-style:none}.anyfetch-mobile-scroll ul.anyfetch-pill-list{overflow-x:auto;overflow-y:hidden;padding-right:5%;width:95%}li.anyfetch-pill{font-size:.75em;padding:.2em .5em;margin:0;border-radius:1em;color:gray;border:1px solid #a9a9a9}span.anyfetch-hlt{background-color:#ffffe0}.anyfetch-right-arrow{display:inline}.anyfetch-right-arrow:before{color:gray;content:\"→\"}.anyfetch-email:before{content:\"<\"}.anyfetch-email:after{content:\">\"}.anyfetch-date{font-style:italic}article.anyfetch-document-snippet.anyfetch-type-contact h1.anyfetch-title,article.anyfetch-document-snippet.anyfetch-type-image h1.anyfetch-title{width:auto}.anyfetch-icon-people{display:inline-block;width:1em;height:1em;background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAAQCAYAAAAbBi9cAAAA+ElEQVQ4y83SMUvcQRQE8J931ukkrVdpFQjBfksFC7UWJHDfwD4MyZdIbxWwEBVL2eIQLLQQxM5CSLC0EBQUz+ognPc/7tLErR5vdue9mVne25lpApLMYBXraGE3yWHT/daYIT+wj6/YwkGSTLVRknlcj8Bf0ElyM+lGXxqGtPB5Gml/xkj+PQ3RKXoj+sc4G/WgPapZa+2XUvYwhw4esINuksf/9o8+4BM+oo9bXCS5n4goyRK+YRmzQ/ATjvA9yXmjR0m28QsLDUG0sYhuKeWu1nr6hijJJn6OkzuU9kop5arWejkc/9o/eLwxKP72oIfnKYlOBsUrMvs/CqmM4RAAAAAASUVORK5CYII=);background-repeat:no-repeat;background-position:bottom;background-size:1em;margin-right:.5em;margin-left:-.2em}.anyfetch-icon-related{padding-left:9px;background:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAKCAYAAABmBXS+AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3gkWEAQrSHQi+QAAAJ9JREFUGNNt0LEKgXEUxuEHszIYbFaXwKYMRgwG1yDFVZDIDVDKogyfUVFyBWY2N0AZDVj+6uvLqVPnnH6d97yH/9FChCWyqT/AADlscMExnQAa+CCPZ5i940Ada+xRRBpbFH6bKiGhjxuO2GEIpaA/QjmAE1Qx/8lMsUAt9Ds0cYjfEuEa6jE6WCUtz9DDI/znlAQyOKONe3DUxSsOfQEQAh3RFNkN8wAAAABJRU5ErkJggg==) no-repeat;padding-bottom:10px}";
    private final String HEADER =
            "<!doctype html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='utf-8'>" +
                    "<style type='text/css'>" + CSS + "</style>" +
                    "</head>" +
                    "<body style='font-family: Roboto; font-size: 12px; margin: 0; padding: 0;'>";
    private final String FOOTER =
            "</body>" +
                    "</html>";

    public DocumentsListAdapter(Context context, List<Document> elements) {
        super(context, R.layout.row_document, elements);
    }

    @Override
    protected String getSection(Document document) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar then = Calendar.getInstance();
        then.setTime(document.getDate());
        if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
            return getContext().getString(R.string.date_today);
        } else if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - 1 == then.get(Calendar.DAY_OF_YEAR)) {
            return getContext().getString(R.string.date_yesterday);
        } else {
            return then.get(Calendar.DAY_OF_MONTH) + "/" + (then.get(Calendar.MONTH));
        }
    }

    @Override
    protected View getView(Document document, View convertView, ViewGroup parent) {
        //if (convertView == null || convertView.findViewById(R.id.webView) == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_document, parent, false);
        //}
        ImageView dtIcon = (ImageView) convertView.findViewById(R.id.dtIcon);
        dtIcon.setImageResource(matchIcon(document.getType()));

        WebView webView = (WebView) convertView.findViewById(R.id.webView);
        webView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        webView.loadData(HEADER + document.getSnippet() + FOOTER, "text/html", "UTF-8");
        return convertView;
    }

    private int matchIcon(String dt) {
        // TODO: replace with generic doctype icons
        if (dt.equals("contact")) {
            return R.drawable.ic_sfdc;
        }
        if (dt.equals("document") || dt.equals("file") || dt.equals("image")) {
            return R.drawable.ic_gdrive;
        }
        if (dt.equals("email-thread") || dt.equals("email")) {
            return R.drawable.ic_gmail;
        }
        if (dt.equals("event")) {
            return R.drawable.ic_event;
        }
        return R.drawable.ic_gdrive;
    }
}
