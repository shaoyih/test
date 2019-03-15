package com.example.project3;

import java.util.ArrayList;

public class Movie {
    private String title;
    private String director;
    private ArrayList<String> stars;
    private ArrayList<String> genres;
    private String year;
    private String id;
    public Movie(String title, String year, String director) {
        this.title = title;
        this.year = year;
        this.director=director;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDirector() {
        return this.director;
    }




    public String getYear() {
        return year;
    }


}
