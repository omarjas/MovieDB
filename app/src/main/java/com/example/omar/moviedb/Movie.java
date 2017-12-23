package com.example.omar.moviedb;

/**
 * Created by omar on 10/15/17.
 */

public class Movie {


    private String title;
    private String date;
    private String rating;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {

        return title;
    }

    public String getDate() {
        return date;
    }

    public String getRating() {
        return rating;
    }
}

