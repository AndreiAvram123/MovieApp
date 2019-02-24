package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.AppDatabase;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Utilities;
import com.example.movieapp.activities.adapters.MainAdapter;
import com.example.movieapp.activities.fragments.ExpandedMovieFragment;
import com.example.movieapp.activities.interfaces.DatabaseInterface;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity
        implements MainAdapter.AdapterInterface, ExpandedMovieFragment.ExpandedMovieFragmentInterface {

    private RecyclerView recyclerView;
    private TextView no_results_error;
    private DatabaseInterface databaseInterface;
    private List<Movie> savedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        makeActivityFullscreen();
        initializeUI();

        if (Utilities.isNetworkAvailable(this)) {
            initializeDatabase();
            getSavedMovies();
            //get the intent ,verify the action and retrieve the query
            Intent searchIntent = getIntent();
            if (Intent.ACTION_SEARCH.equals(searchIntent.getAction())) {
                String query = searchIntent.getStringExtra(SearchManager.QUERY);
                pushRequest(getUriForRequest(query.trim()));

            }
        } else {
            no_results_error.setVisibility(View.VISIBLE);
            no_results_error.setText(R.string.no_internet_connection);
        }

    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.recycler_view_search_activity);
        no_results_error = findViewById(R.id.error_message_search);
        ImageView back_image = findViewById(R.id.back_image_seach);
        back_image.setOnClickListener(view -> finish());
    }

    private void getSavedMovies() {
        Thread backgroundThread = new Thread(() -> savedMovies =
                databaseInterface.getAllMovies());

        backgroundThread.start();
    }

    private void saveMovieInDatabase(Movie movie) {
        savedMovies.add(movie);
        Thread backgroundThread = new Thread(() ->
                databaseInterface.insertMovie(movie));
        backgroundThread.start();
    }

    private void deleteMovieFromDatabase(Movie movie){

        savedMovies.remove(movie);
        Thread backgroundThread = new Thread(()->
                databaseInterface.deleteMovieById(movie.getMovieID()));
        backgroundThread.start();
    }




    /**
     * This method is used to get a reference to the current Room
     * database for this application (movies_database)
     * The method also assigns a value to the databaseInterface
     *
     * @databaseInterface IS CALLED DAO AND IT IS
     * USED TO COMMUNICATE WITH THE ROOM DATABASE
     */
    private void initializeDatabase() {
        //use getApplicationContext because data is related to the
        //whole application
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,
                "movies_database").build();
        databaseInterface = appDatabase.databaseInterface();
    }

    private void makeActivityFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void pushRequest(String uri) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest dataRequest = new StringRequest(StringRequest.Method.GET, uri,
                response -> runOnUiThread(() -> updateUI(response)),
                error -> {

                });

        requestQueue.add(dataRequest);
    }

    private void updateUI(String responseData) {
        ArrayList<Movie> searchedMovies = Utilities.processJSONFormat(responseData);

        if (searchedMovies.isEmpty()) {
            no_results_error.setVisibility(View.VISIBLE);
            no_results_error.setText(getString(R.string.error_no_result));
        } else {
            createRecyclerView(searchedMovies);
        }

    }

    private void createRecyclerView(ArrayList<Movie> searchedMovies) {
        MainAdapter mainAdapter = new MainAdapter(recyclerView, searchedMovies, this);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new CustomDivider(20));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /**
     * This method is used in order to replace empty spaces
     * from the query with the + sign
     * The request does not work if we use empty
     * spaces
     *
     * @param query - the query from the searchView
     * @return
     */
    private String getUriForRequest(String query) {
        String uriFormat = "https://api.themoviedb.org/3/search/movie?api_key=55398af9b60eda4997b848dd5ccf7d44&query=";
        return uriFormat + query.replace(' ', '+');
    }


    @Override
    public void onItemClicked(Movie movie) {
        if (savedMovies.contains(movie)) {
            movie.setSaved(true);
        } else {
            movie.setSaved(false);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeholder_seach_activity, ExpandedMovieFragment.newInstance(movie))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void saveMovie(Movie movie) {
        saveMovieInDatabase(movie);

    }

    @Override
    public void deleteMovie(Movie movie) {
        deleteMovieFromDatabase(movie);
    }
}

