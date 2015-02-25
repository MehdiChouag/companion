package com.anyfetch.companion.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.anyfetch.companion.R;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mehdichouag on 16/02/15.
 */
public class ContactCursorAdapter extends CursorAdapter {

    private int[] mColorsArray;

    private TextAppearanceSpan highlightTextSpan;
    private AlphabetIndexer mIndexer;
    private String mSearchTerm;


    public ContactCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mColorsArray = context.getResources().getIntArray(R.array.contact_colors);
        highlightTextSpan = new TextAppearanceSpan(context, R.style.searchTextHiglight);
        mIndexer = new AlphabetIndexer(null, PROJ_CONTACT_LIST.NAME, context.getString(R.string.alphabet));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_contact, parent, false);
        v.setTag(new ViewHolder(v));
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        final int columnName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int columnPicture = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);

        final String uriPic = cursor.getString(columnPicture);
        final String name = cursor.getString(columnName);

        final int startIndex = indexOfSearchQuery(name);

        if (startIndex == -1)
            holder.contactName.setText(name);
        else {
            final SpannableString highlightedName = new SpannableString(name);

            highlightedName.setSpan(highlightTextSpan, startIndex,
                    startIndex + mSearchTerm.length(), 0);

            holder.contactName.setText(highlightedName);
        }

        if (uriPic != null && !uriPic.isEmpty()) {
            holder.contactTextDrawable.setVisibility(View.GONE);

            holder.contactImage.setVisibility(View.VISIBLE);
            holder.contactImage.setImageURI(Uri.parse(uriPic));
        } else {
            holder.contactImage.setVisibility(View.GONE);

            holder.contactTextDrawable.setVisibility(View.VISIBLE);
            holder.contactTextDrawable.setImageDrawable(setLetterDrawable(name, cursor.getPosition()));
        }
    }

    private Drawable setLetterDrawable(String name, int pos) {
        return TextDrawable.builder()
                .buildRound(name.substring(0, 1).toUpperCase(), mColorsArray[pos % mColorsArray.length]);
    }

    private int indexOfSearchQuery(String displayName) {
        if (!TextUtils.isEmpty(mSearchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(
                    mSearchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }


    public Cursor swapCursor(Cursor newCursor, String search) {
        mSearchTerm = search;
        mIndexer.setCursor(newCursor);
        return super.swapCursor(newCursor);
    }

    /*@Override
    public Object[] getSections() {
        return mIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return getCursor() != null && !getCursor().isClosed() ? mIndexer.getPositionForSection(sectionIndex) : 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return getCursor() != null && !getCursor().isClosed() ? mIndexer.getSectionForPosition(position) : 0;
    } */

    @Override
    public int getCount() {
        return getCursor() != null ? super.getCount() : 0;
    }

    private class ViewHolder {
        CircleImageView contactImage;
        ImageView contactTextDrawable;
        TextView contactName;

        public ViewHolder(View view) {
            contactImage = (CircleImageView) view.findViewById(R.id.contact_picture);
            contactTextDrawable = (ImageView) view.findViewById(R.id.contact_picture_text);
            contactName = (TextView) view.findViewById(R.id.contact_name);
        }
    }

    public interface PROJ_CONTACT_LIST {
        static final String SORT = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        static final String SELECT = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND (" + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1))";

        int _ID = 0;
        int NAME = 1;
        int PHOTO_URI = 2;

        String COLS[] = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI
        };
    }
}
