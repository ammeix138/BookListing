package com.example.ammei.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * URL to obtain book data from Google Books website
     */
    private static final String GOOGLE_BOOKS_URL =
            "https://www.googleapis.com/books/v1/volumes?maxResults=2&q=books";
    EditText mSearchTerm;
    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;
    /**
     * TextView that is displayed when the list is empty and no items have been searched
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        DownloadTask task = new DownloadTask();
        task.execute();

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list_item);

        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Books>());

        // List that will be populated with the books searched
        bookListView.setAdapter(mAdapter);

        EditText searchText = (EditText) findViewById(R.id.search_editText);
        searchText.getText().toString();




        // Item click listener on the ListView, send an intent to a web browser
        // to open a website with more information about the searched book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Button searchButton = (Button) findViewById(R.id.button);
                searchButton.getText().toString();


                // Find the current earthquake that was clicked on
                Books currentBook = mAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getURL());
                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    private void updateUi(List<Books> books) {
        //Display the Book Title within the UI
        mAdapter.clear();
        mAdapter.addAll(books);
        mAdapter.notifyDataSetChanged();
    }

    private class DownloadTask extends AsyncTask<String, Void, List<Books>> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected List<Books> doInBackground(String... urls) {

            URL url = createUrl(GOOGLE_BOOKS_URL);

            //Perform HTTP request to the URL and receives a JSON response back.
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "There was a problem retrieving the search query", e);
            }

            List<Books> books = extractFeatureFromJson(jsonResponse);

            return books;
        }

        /*
         *Updates the screen with a new list of searched books
         */
        @Override
        protected void onPostExecute(List<Books> books) {
            if (books == null){
                return;
            }

            updateUi(books);
        }

        private URL createUrl(String stringUrl) {
            URL url;

            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }

            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000/* time in milliseconds*/);
                urlConnection.setConnectTimeout(15000/* time in milliseconds*/);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the searched books", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }

            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }

            return output.toString();
        }

        /**
         * Return a list of {@link Books} objects that has been built up from
         * parsing a JSON response.
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private List<Books> extractFeatureFromJson(String googleBookJSON) {

            if (TextUtils.isEmpty(googleBookJSON)) {
                return null;
            }

            List<Books> books = new ArrayList<>();

            try {

                JSONObject baseJsonResponse = new JSONObject(googleBookJSON);
                JSONArray bookArray = baseJsonResponse.getJSONArray("items");


                for (int i = 0; i < bookArray.length(); i++) {
                    JSONObject currentBook = bookArray.getJSONObject(i);
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    //Extract out the title, author, and description
                    String title = volumeInfo.getString("title");
                    String authors = volumeInfo.getString("authors");
                    String description = volumeInfo.getString("description");
                    // Extract the value for the key called "url"
                    String url = volumeInfo.getString("previewLink");

                    //Create a new object
                    Books booksList = new Books(title, authors, description, url);
                    books.add(booksList);
                }

            } catch (JSONException e) {
                // Print a log message
                // with the message from the exception.
                Log.e(LOG_TAG, "Problem parsing the Google Books JSON results", e);
            }

            // Return the list of the book searched
            return books;
        }

    }
}




