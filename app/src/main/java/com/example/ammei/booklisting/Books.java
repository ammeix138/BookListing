package com.example.ammei.booklisting;

/**
 * Created by ammei on 12/3/2016.
 */

public class Books {


    /*Author of the book*/
    private String mAuthor;

    /*Title of the book*/
    private String mTitle;

    /*Publisher of the book*/
    private String mDescription;

    /** Website URL of the earthquake */
    private String mUrl;



    public Books(String author, String title, String description, String url) {
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mUrl = url;

    }

    /**
     * Returns the Author of the book
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Return the Title of the book
     */
    public String getTitle() {
        return mTitle;
    }

    /*
     * Return the name of the publisher for the book
     */
    public String getDescription() {
        return mDescription;
    }

    public String getURL(){
        return mUrl;
    }
}

