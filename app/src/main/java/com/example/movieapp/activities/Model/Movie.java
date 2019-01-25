package com.example.movieapp.activities.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String overview;
    private String title;

    private String releaseDate;
    private String posterPath;

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
    }

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
