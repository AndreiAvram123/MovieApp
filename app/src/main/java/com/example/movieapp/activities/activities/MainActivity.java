package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.fragments.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<Movie> popularMovies;
    private ArrayList<Movie> upcomingMovies;
    private static final String popularMoviesUri = "https://api.themoviedb.org/3/movie/popular?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1";
    private static final  String upcomingMoviesUri = "https://api.themoviedb.org/3/movie/upcoming?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1&region=GB";
    private DrawerLayout drawerLayout;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        setUpToolbar();

        makeAppFullscreen();

        pushRequests();
    }

    private void makeAppFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initializeViews() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        /**
         * The standard way of updating the header layout
         */
        NavigationView navigationView = findViewById(R.id.nav_view);
       View header_layout = navigationView.getHeaderView(0);
       TextView email = header_layout.findViewById(R.id.email_address_drawer);
       email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                  drawerLayout.openDrawer(Gravity.START);
                  return true;
        }
        return super.onOptionsItemSelected(item);
    }






    /**
     * This method pushed a request in order to get data
     * from the servers
     * @param url -
     */
    private void pushRequests() {
        //initialize the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
       //request a string response from the url for popular movies
       StringRequest popularMoviesRequest = new StringRequest(Request.Method.GET,popularMoviesUri,
               response -> runOnUiThread(()-> popularMovies = getMovies(response)), error ->{

       });

       StringRequest upcomingMoviesRequest = new StringRequest(Request.Method.GET,upcomingMoviesUri,
               //REMEMBER THAT OPERATIONS SHOULD BE DONE IN
               //THE MAIN THREAD
               response -> runOnUiThread(()-> {
                   upcomingMovies = getMovies(response);
                   //when we finish with the request we update the UI
                   updateUI();
               }), error->{

       });



       requestQueue.add(popularMoviesRequest);
       requestQueue.add(upcomingMoviesRequest);
    }




    private void updateUI() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0: return BaseFragment.newInstance(popularMovies);
                    case 1: return BaseFragment.newInstance(upcomingMovies);
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0 :return "Popular Movies";
                    case 1: return "Upcoming Movies";
                    default: return null;
                }
            }
        });
    }

    /**
     * This method processes the JSON data downloaded from
     * the servers and returns an ArrayList<Movie> of maximum
     * 10 movies
     * @param response-the JSON data as String format
     *
     *
     */
    private ArrayList<Movie> getMovies(String response)  {
        ArrayList<Movie> movies = new ArrayList<>();
       try{
       JSONObject requestObject = new JSONObject(response);
        JSONArray results = requestObject.getJSONArray("results");
        //parse the first 10 objects
        for(int i =0;i< 10 && i< response.length();i++){
            JSONObject currentMovieJSONFormat = results.getJSONObject(i);
            Movie currentMovie = new Movie(
                    currentMovieJSONFormat.getString("overview"),
                    currentMovieJSONFormat.getString("title"),
                    Useful.convertDate(currentMovieJSONFormat.getString("release_date")),
                    getString(R.string.request_format_image)+currentMovieJSONFormat.getString("poster_path")
            );
            movies.add(currentMovie);

        }
       }catch(JSONException jsonE){
         jsonE.printStackTrace();
       }
       return movies;
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
