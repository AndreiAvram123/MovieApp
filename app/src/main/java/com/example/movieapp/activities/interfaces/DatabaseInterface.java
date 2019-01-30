package com.example.movieapp.activities.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.movieapp.activities.Model.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DatabaseInterface {

  @Insert
  void insertMovie(Movie movie);

  @Query("SELECT * FROM movies")
  List<Movie> selectAllMovies();


    /**
     * USE THIS METHOD INSTEAD OF PASSING AN OBJECT
     * IT IS MORE EFFICIENT TO DO THAT
     * THIS DOES NOT REQUIRE YOU TO OVERRIDE THE
     * EQUALS() METHOD FOR THE MOVIE CLASS
     * @param movieId
     */
  @Query("DELETE FROM movies WHERE movieID = :movieId ")
  void deleteMovieById(int movieId);


 @Query("SELECT * FROM movies WHERE movieID = :movieId")
  Movie getMovie(int movieId);
}
