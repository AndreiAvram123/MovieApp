package com.example.movieapp.activities.Model;

public class Movie {
    private String overview;
    private String title;

    private String releaseDate;
    private String posterPath;

    public Movie(String overview, String title, String releaseDate,String posterPath) {
        if(overview.length() >125) {
            this.overview = overview.substring(0, 125) + "...";
        }else {
            this.overview = overview;
        }
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
