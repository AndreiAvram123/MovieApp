package com.example.movieapp.activities.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String overview;
    private String title;
    private String releaseDate;
    private String posterPath;
    private double rating;
    private int movieID;
    private int [] genres;

    /**
     * REQUIRED CONSTRUCTOR IN ORDER
     * TO USE THE PARCELABLE INTERFACE
     * @param in- the parcel used to
     *          build the object
     */
    public Movie(Parcel in){
         overview = in.readString();
         title = in.readString();
         releaseDate = in.readString();
         posterPath = in.readString();
         rating = in.readDouble();
         movieID = in.readInt();
         genres = in.createIntArray();
    }

    public Movie(String overview, String title, String releaseDate,
                 String posterPath,double rating,int movieID,int [] genres) {

        this.overview = overview;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.rating = rating;
        this.movieID = movieID;
        this.genres = genres;
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
    public String getShortOverview(){
        if(overview.length()>125){
            return overview.substring(0,125);
        }else {
            return overview;
        }
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(overview);
         dest.writeString(title);
         dest.writeString(releaseDate);
         dest.writeString(posterPath);
         dest.writeDouble(rating);
         dest.writeInt(movieID);
         dest.writeIntArray(genres);
    }

    /**
     * REQUIRED CREATOR IN ORDER TO USE THE PARCELABLE INTERFACE
     * IF NOT PROVIDED AN ERROR IS THROWN
     */
    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Movie[size];
        }
    };
}
