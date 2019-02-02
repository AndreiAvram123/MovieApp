package com.example.movieapp.activities.Model;

public class Constraints {
    public static final String KEY_UPCOMING_MOVIES_ARRAY = "UPCOMING_MOVIES_ARRAY";
    public static final String KEY_POPULAR_MOVIES_ARRAY = "POPULAR_MOVIES_ARRAY";
    public static final String KEY_EMAIL_CREDENTIAL ="EMAIL";
    public static final String KEY_PASSWORD_CREDENTIAL = "PASSWORD";
    public static final String KEY_NICKNAME_CREDENTIAL = "NICKNAME";
    public static final String KEY_CREDENTIAL = "CREDENTIAL";
    public static final String KEY_MOVIE = "KEY_MOVIE";
    public static final String KEY_SAVED_MOVIES = "SAVED_MOVIES_ARRAY";
    public static final String SAVED_MOVIES_FRAGMENT = "SAVED_MOVIES_FRAGMENT";
    public static final String VIEW_PAGER_FRAGMENT = "VIEW_PAGER_FRAGMENT";
    public static final String KEY_NICKNAME_SETTINGS = "KEY_NICKNAME_SETTING";


    public static String getGenre(int genreID){
        switch (genreID){
            case 28 : return "Action";
            case 12 : return "Adventure";
            case 16 : return "Animation";
            case 35 : return "Comedy";
            case 80 : return "Crime";
            case 99 : return "Documentary";
            case 18 : return "Drama";
            case 10751 : return "Family";
            case 14 : return "Fantasy";
            case 36 : return "History";
            case 27: return "Horror";
            case 10402: return "Music";
            case 9648: return "Mystery";
            case 10749: return "Romance";
            case 878: return "Science Fiction";
            case 10770: return "Tv Movie";
            case 53: return "Thriller";
            case 37: return "Western";

        }
        return "";
    }
}
