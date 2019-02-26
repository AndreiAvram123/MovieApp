package com.example.movieapp.activities.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class is used to perform some static operations
 * which do not depends on a specific activity such as
 * checking if an email is valid
 */
public class Utilities {

    /**
     * This method is used to check weather or not there
     * is internet connection available in the given context
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }


    /**
     * This method uses a regex with the matches() method in
     * order to determine if the email address is valid or
     * not
     *
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        return email.matches("[a-zA-Z0-9]+@[a-z]+\\.[a-z]+");
    }


    /**
     * The date format from the servers is as follows
     * yyyy/mm/dd
     * This method converts this format into dd/mm/yyyy
     */
    public static String reverseDate(String date) {

        String[] strings = date.split("-");
        if (strings.length < 3) {
            return "UNKNOWN";
        } else {
            /* strings[0] = yyyy
           strings[1] = mm
           strings[2] = dd
            */
            return strings[2] + "/" + strings[1] + "/" + strings[0];
        }


    }

    /**
     * Get one string that contains all the genres of a current movie in
     * JSON format separated by a comma
     */
    public static String getGenresAsString(JSONObject currentMovieJSONFormat) throws JSONException {
        StringBuilder stringBuilder = new StringBuilder();

        JSONArray genresArrayJson = currentMovieJSONFormat.getJSONArray("genre_ids");
        if (genresArrayJson.length() == 0) {
            return "UNKNOWN";
        } else {
            for (int i = 0; i < genresArrayJson.length(); i++) {
                String genre = getGenre(genresArrayJson.getInt(i));
                stringBuilder.append(genre).append(",");
            }
            //remove comma after the last genre
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
    }

    /**
     * This method processes the JSON data passed to the parameter
     * and returns an ArrayList<Movie>
     *
     * @param response-the JSON data downloaded from the
     *                     servers
     */
    public static ArrayList<Movie> processJSONFormat(String response) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject requestObject = new JSONObject(response);
            JSONArray results = requestObject.getJSONArray("results");


            for (int i = 0; i < results.length(); i++) {
                JSONObject currentMovieJSONFormat = results.getJSONObject(i);
                Movie currentMovie = new Movie(
                        currentMovieJSONFormat.getString("overview"),
                        currentMovieJSONFormat.getString("title"),
                        Utilities.reverseDate(currentMovieJSONFormat.getString("release_date")),
                        "https://image.tmdb.org/t/p/w300" + currentMovieJSONFormat.getString("poster_path"),
                        currentMovieJSONFormat.getDouble("vote_average"),
                        currentMovieJSONFormat.getInt("id"),
                        //get the genres array
                        Utilities.getGenresAsString(currentMovieJSONFormat)
                );
                movies.add(currentMovie);

            }
        } catch (JSONException jsonE) {
            jsonE.printStackTrace();
        }
        return movies;
    }

    public static String getGenre(int genreID) {
        switch (genreID) {
            case 28:
                return "Action";
            case 12:
                return "Adventure";
            case 16:
                return "Animation";
            case 35:
                return "Comedy";
            case 80:
                return "Crime";
            case 99:
                return "Documentary";
            case 18:
                return "Drama";
            case 10751:
                return "Family";
            case 14:
                return "Fantasy";
            case 36:
                return "History";
            case 27:
                return "Horror";
            case 10402:
                return "Music";
            case 9648:
                return "Mystery";
            case 10749:
                return "Romance";
            case 878:
                return "Science Fiction";
            case 10770:
                return "Tv Movie";
            case 53:
                return "Thriller";
            case 37:
                return "Western";
            default:
                return "";
        }
    }
}
