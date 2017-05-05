package com.example.ammei.booklisting;

/**
 * Created by ammei on 12/3/2016.
 */

public class Books {


    /*Title of the book*/
    private String mTitle;

    /*Author of the book*/
    private String mAuthor;

    /*Publisher of the book*/
    private String mDescription;

    /** Website URL of the earthquake */
    private String mUrl;



    public Books(String author, String title, String description, String url) {
        mTitle = title;
        mAuthor = author;
        mDescription = description;
        mUrl = url;

    }

    /**
     * Return the Title of the book
     */
    public String getTitle() {
        return mTitle;
    }


    /**
     * Returns the Author of the book
     */
    public String getAuthor() {
        return mAuthor;
    }

    /*
     * Return the name of the publisher for the book
     */
    public String getDescription() {
        return mDescription;
    }

    /*
     * Returns a URL object
     */
    public String getURL(){
        return mUrl;
    }
}

