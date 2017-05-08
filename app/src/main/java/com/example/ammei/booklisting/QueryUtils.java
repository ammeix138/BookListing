package com.example.ammei.booklisting;

import android.text.TextUtils;
import android.util.Log;

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

/**
 * Created by ammei on 12/3/2016.
 */

public final class QueryUtils {

    /*Tag for the log messages*/
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * This class is only meant to hold static variables.
     */
    private QueryUtils() {
    }

    public static List<Books> extractBookList(String requestURL) {
        //Create URL object
        URL url = createURL(requestURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "There was an issue retrieving Book List", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "There was an issue retrieving the book search");
        }


        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/* time in milliseconds*/);
            urlConnection.setConnectTimeout(15000/* time in milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }

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


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));

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
    private static List<Books> extractFeatureFromJson(String googleBookJSON) {

        if (TextUtils.isEmpty(googleBookJSON)) {
            return null;
        }

        List<Books> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(googleBookJSON);
            String array = baseJsonResponse.getString("items");

            JSONArray bookArray = new JSONArray(array);

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                String authors = "";
                JSONArray authorsAry = null;
                //Checks to ensure there is an author value prior to returning request
                if (volumeInfo.has("authors")) {
                    for (int j = 0; j < authorsAry.length(); j++) {
                        authors += authorsAry.getString(j) + ";";

                        //Return the String w/out ";" in the statement
                        authors = authors.substring(0, authors.length() - 2);
                    }
                } else {
                    authorsAry.put(0, "No Authors Available");
                }
                String description = volumeInfo.getString("description");
                // Extract the value for the key called "url"
                String url = volumeInfo.getString("url");

                Books booksList = new Books(title, authors, description, url);
                books.add(booksList);
            }

        } catch (JSONException e) {
            // Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Google Books JSON results", e);
        }

        // Return the list of the book searched
        return books;
    }

}

