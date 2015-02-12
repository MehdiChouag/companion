package com.anyfetch.companion.commons.ui;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.anyfetch.companion.commons.R;

/**
 * Various helpers to work with bitmaps
 */
public class ImageHelper {
    /**
     * Round the corners of an image
     *
     * @param bitmap The image
     * @param pixels The border radius
     * @return A new bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static int matchResourceForProvider(String provider) {
        if (provider.equals("52bff114c8318c29e9000005")) {
            return R.drawable.ic_provider_52bff114c8318c29e9000005;
        } else if (provider.equals("53047faac8318c2d65000097")) {
            return R.drawable.ic_provider_53047faac8318c2d65000097;
        } else if (provider.equals("53047faac8318c2d65000099")) {
            return R.drawable.ic_provider_53047faac8318c2d65000099;
        } else if (provider.equals("52bff1eec8318cb228000001")) {
            return R.drawable.ic_provider_52bff1eec8318cb228000001;
        } else if (provider.equals("539ef7289f240405465a2e1f")) {
            return R.drawable.ic_provider_539ef7289f240405465a2e1f;
        } else if (provider.equals("53047faac8318c2d65000096")) {
            return R.drawable.ic_provider_53047faac8318c2d65000096;
        } else if (provider.equals("53047faac8318c2d65000100")) {
            return R.drawable.ic_provider_53047faac8318c2d65000100;
        } else if (provider.equals("53047faac8318c2d650001a1")) {
            return R.drawable.ic_provider_53047faac8318c2d650001a1;
        } else {
            return R.drawable.ic_launcher;
        }
    }

    public static int matchColorForDocumentType(String dt) {
        if (dt.equals("5252ce4ce4cfcd16f55cfa3a")) {
            return R.color.dt_contact;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3c")) {
            return R.color.dt_document;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3f") || dt.equals("656d61696c2d746872656164")) {
            return R.color.dt_email;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa40")) {
            return R.color.dt_event;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3c")) {
            return R.color.dt_file;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3d")) {
            return R.color.dt_image;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa4a")) {
            return R.color.dt_note;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3e")) {
            return R.color.dt_trello;
        } else {
            return android.R.color.darker_gray;
        }
    }

    public static int matchIconForDocumentType(String dt) {
        if (dt.equals("5252ce4ce4cfcd16f55cfa3a")) {
            // Contacts
            return R.drawable.type_5252ce4ce4cfcd16f55cfa3a;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3c")) {
            // Document
            return R.drawable.type_5252ce4ce4cfcd16f55cfa3c;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3f") || dt.equals("656d61696c2d746872656164")) {
            // Email / email-thread
            return R.drawable.type_5252ce4ce4cfcd16f55cfa3f;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa40")) {
            // Event
            return R.drawable.type_5252ce4ce4cfcd16f55cfa40;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa3d")) {
            // Image
            return R.drawable.type_5252ce4ce4cfcd16f55cfa3d;
        } else if (dt.equals("5252ce4ce4cfcd16f55cfa4a")) {
            // Note
            return R.drawable.type_5252ce4ce4cfcd16f55cfa4a;
        } else {
            // Default (file)
            return R.drawable.type_5252ce4ce4cfcd16f55cfa3c;
        }
    }

    public static Bitmap toBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}