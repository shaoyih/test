package com.android.fablix;

import java.util.List;

public class Movie {
    private String title;
    private String director;
    private String year;
    private String rating;
    private String id;
    private List<String> genres;
    private List<String> stars;

    public Movie(String id, String title,String director, String year, String rating,List<String> stars, List<String> genres){
        this.id=id;
        this.title=title;
        this.director=director;
        this.year=year;
        this.rating=rating;
        this.genres=genres;
        this.stars=stars;
    }
    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDirector(){
        return this.director;
    }
    public String getYear(){
        return this.year;
    }
    public String getrating(){
        return this.rating;
    }
    public List<String> getGenres(){
        return this.genres;
    }
    public List<String> getStars() {
        return stars;
    }
}
