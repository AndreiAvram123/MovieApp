package com.example.movieapp.activities.Model;

import android.view.Window;
import android.view.WindowManager;

import com.example.movieapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class is used to perform some static operations
 * which do not depends on a specific activity
 */
public class Useful {
    /**
     * This method uses a regex with the matches() method in
     * order to determine if the email address is valid or
     * not
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        return email.matches("[a-zA-Z0-9]+@[a-z]+\\.[a-z]+");
    }


    public static String reverseDate(String date) {
        String newDate ="";
        String [] strings = date.split("-");
        for(int i =strings.length -1;i >= 1;i-- )
            newDate = newDate +strings[i] + "-";
        //remember to add the last one
          newDate = newDate +strings[0];
        return newDate;
    }

    public  static  String getGenres(JSONObject currentMovieJSONFormat) throws JSONException {
         StringBuilder stringBuilder = new StringBuilder();
        JSONArray genresArrayJson = currentMovieJSONFormat.getJSONArray("genre_ids");
        for(int i =0;i< genresArrayJson.length();i++) {
            String genre = Constraints.getGenre(genresArrayJson.getInt(i));
            if(i != genresArrayJson.length() - 1){
                stringBuilder.append(genre + " , ");
            }else {
                stringBuilder.append(genre);
            }
        }
        return stringBuilder.toString();
    }

    public static void  makeActivityFullscreen(Window window){
      window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    /**
     * This method processes the JSON data downloaded from
     * the servers and returns an ArrayList<Movie>
     * @param response-the JSON data downloaded from the
     *                servers
     */
    public static  ArrayList<Movie> getMovies(String response)  {
        ArrayList<Movie> movies = new ArrayList<>();
        try{
            JSONObject requestObject = new JSONObject(response);
            JSONArray results = requestObject.getJSONArray("results");


            for(int i=0;  i< response.length();i++){
                JSONObject currentMovieJSONFormat = results.getJSONObject(i);
                Movie currentMovie = new Movie(
                        currentMovieJSONFormat.getString("overview"),
                        currentMovieJSONFormat.getString("title"),
                        Useful.reverseDate(currentMovieJSONFormat.getString("release_date")),
                        "https://image.tmdb.org/t/p/w300"+currentMovieJSONFormat.getString("poster_path"),
                        currentMovieJSONFormat.getDouble("vote_average"),
                        currentMovieJSONFormat.getInt("id"),
                        //get the genres array
                        Useful.getGenres(currentMovieJSONFormat)
                );
                movies.add(currentMovie);

            }
        }catch(JSONException jsonE){
            jsonE.printStackTrace();
        }
        return movies;
    }
}
