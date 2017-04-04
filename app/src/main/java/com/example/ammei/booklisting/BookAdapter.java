package com.example.ammei.booklisting;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ammei on 12/3/2016.
 */

public class BookAdapter extends ArrayAdapter<Books> {

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Context context, List<Books> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        //Get the current book at the given position
        Books currentBook = getItem(position);

        //Get the TextView with the ID author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        //Display the book with the correct author the user searched.
        authorView.setText(currentBook.getAuthor());
        //Get the TExtView with the ID bookTitle
        TextView titleView = (TextView) listItemView.findViewById(R.id.bookTitle);
        //Display the title of the searched book.
        titleView.setText(currentBook.getTitle());
        //Get the TextView with the ID publisher
        TextView pubView = (TextView) listItemView.findViewById(R.id.description);
        //Display the publisher of the searched book.
        pubView.setText(currentBook.getDescription());


        return listItemView;
    }

}
