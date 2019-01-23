package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.adapters.MainActivityAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<Movie> popularMovies;
    private static final String popularMoviesUri = "https://api.themoviedb.org/3/movie/popular?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        makeAppFullscreen();


        pushRequest(popularMoviesUri);
    }

    private void makeAppFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view_main);
        setUpToolbar();
        setUpSearch();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //set this to false if you want the searchView
        //to work properly
        searchView.setIconifiedByDefault(false);

        return true;
    }

    /**
     * This method is used in order to
     * set up the searchBar in the app
     */
     private void setUpSearch() {


    }

    /**
     * This method pushed a request in order to get data
     * from the servers
     * @param url -
     */
    private void pushRequest(String url) {
        //initialize the queue
       RequestQueue requestQueue =  Volley.newRequestQueue(this);
       Log.d(TAG,url);
       //request a string response from the url
       StringRequest dataRequest = new StringRequest(Request.Method.GET,url,
               response -> {
                processResponse(response);
               runOnUiThread(()->updateUI());
               }, error ->{

       });
       requestQueue.add(dataRequest);
    }

    private void updateUI() {
        createRecyclerView();
    }

    private void createRecyclerView() {
        MainActivityAdapter mainAdapter = new MainActivityAdapter(popularMovies);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CustomDivider(15));
    }

    /**
     * This method processes the JSON data downloaded from
     * the servers and adds to the popularMovies ArrayList
     * 10 movies .The number of movies can be changed any time we
     * want
     * @param response-the JSON data as String format
     */
    private void processResponse(String response)  {
       popularMovies = new ArrayList<>();
       try{
       JSONObject resultObject = new JSONObject(response);
        JSONArray results = resultObject.getJSONArray("results");
        //parse the first 10 objects
        for(int i =0;i< 10;i++){
            JSONObject currentMovieJSONFormat = results.getJSONObject(i);
            Movie currentMovie = new Movie(
                    currentMovieJSONFormat.getString("overview"),
                    currentMovieJSONFormat.getString("title"),
                    Useful.convertDate(currentMovieJSONFormat.getString("release_date")),
                    getString(R.string.request_format_image)+currentMovieJSONFormat.getString("poster_path")
            );
            popularMovies.add(currentMovie);

        }
       }catch(JSONException jsonE){
         jsonE.printStackTrace();
       }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * It is called when we return to the
     * activity .We need to collapse the searchView
     * and to set the query text
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();

    }



}
