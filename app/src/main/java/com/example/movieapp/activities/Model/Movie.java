package com.example.movieapp.activities.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movies")
public class Movie implements Parcelable {
    @PrimaryKey
    private int movieID;
    private String overview;
    private String title;
    private String releaseDate;
    private String posterPath;
    private double rating;
    private String  genres;
    private boolean isSaved ;

    /**
     * REQUIRED CONSTRUCTOR IN ORDER
     * TO USE THE PARCELABLE INTERFACE
     * @param in- the parcel used to
     *          build the object
     */
    @Ignore
    public Movie(Parcel in){
         overview = in.readString();
         title = in.readString();
         releaseDate = in.readString();
         posterPath = in.readString();
         rating = in.readDouble();
         movieID = in.readInt();
         genres = in.readString();
         isSaved =false;
    }

    @Ignore
    public Movie(String overview, String title, String releaseDate,
                 String posterPath,double rating,int movieID,String genres) {

        this.overview = overview;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.rating = rating;
        this.movieID = movieID;
        this.genres = genres;
        isSaved =false;
    }
    //default constructor for Room database
    public Movie (){

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
        if(overview.length()>110){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i =0;i < 110;i++){
                stringBuilder.append(overview.charAt(i));
            }
            int currentPosition = stringBuilder.length()- 1;
            while(stringBuilder.charAt(currentPosition) !=' '){
                stringBuilder.deleteCharAt(currentPosition);
                 currentPosition--;
            }
            return stringBuilder.toString() + "...";

        }else {
            return overview;
        }
    }



    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
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
         dest.writeString(genres);
    }

    /**
     * REQUIRED CREATOR IN ORDER TO USE THE PARCELABLE INTERFACE
     * IF NOT PROVIDED AN ERROR IS THROWN
     * ANNOTATE IT IN ORDER FOR THE ROOM DATABASE TO IGNORE IT
     */
    @Ignore
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
