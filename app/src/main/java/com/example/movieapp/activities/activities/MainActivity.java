package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.fragments.EditProfileFragment;
import com.example.movieapp.activities.fragments.ViewPagerFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String VIEW_PAGER_FRAGMENT = "VIEW_PAGER_FRAGMENT";
    private String currentFragment;
    private  ArrayList<Movie> popularMovies;
    private  ArrayList<Movie> upcomingMovies;
    private static final String popularMoviesUri = "https://api.themoviedb.org/3/movie/popular?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1";
    private static final  String upcomingMoviesUri = "https://api.themoviedb.org/3/movie/upcoming?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1&region=GB";
    private DrawerLayout drawerLayout;
    private SearchView searchView;
    private ViewPagerFragment viewPagerFragment;
    private RequestQueue requestQueue;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initializeViews();

        pushRequests();




        setUpToolbar();

        makeAppFullscreen();



    }



    private void makeAppFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.edit_profile_item:
                    drawerLayout.closeDrawers();
                    uncheckItems(menuItem.getItemId(),navigationView.getMenu());
                    startActivity(new Intent(this,EditProfileActivity.class));
                    return true;

                case R.id.home_item:
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(true);
                    uncheckItems(menuItem.getItemId(),navigationView.getMenu());
                    if(!currentFragment.equals(VIEW_PAGER_FRAGMENT))
                        showViewPagerFragment();

                    return true;



            }
            return false;
        });
/**
 * The standard way of updating the header layout
 */
        View header_layout = navigationView.getHeaderView(0);
        TextView email = header_layout.findViewById(R.id.email_address_drawer);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    private void uncheckItems(int itemId, Menu menu) {
        for(int i = 0;i < menu.size();i++){
            if(itemId != menu.getItem(i).getItemId()){
                menu.getItem(i).setChecked(false);
            }
        }
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
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //use Gravity.START in order to optimize for right to left text
                  drawerLayout.openDrawer(Gravity.START);
                  return true;


        }
        return super.onOptionsItemSelected(item);
    }


    private void showViewPagerFragment() {
        currentFragment = VIEW_PAGER_FRAGMENT;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_placeholder,viewPagerFragment,VIEW_PAGER_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void updateUI() {
        currentFragment = VIEW_PAGER_FRAGMENT;
        viewPagerFragment = ViewPagerFragment.newInstance(upcomingMovies,popularMovies);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_placeholder,viewPagerFragment,VIEW_PAGER_FRAGMENT);
        fragmentTransaction.commit();
    }

    /**
     * This method pushes requests  in order to get data
     * from the movie database
     */
    private void pushRequests() {
        //initialize volley
        requestQueue = Volley.newRequestQueue(getApplicationContext());
         //request a string response from the url for popular movies

       StringRequest upcomingMoviesRequest = new StringRequest(Request.Method.GET,upcomingMoviesUri,
               //REMEMBER THAT OPERATIONS SHOULD BE DONE IN
               //THE MAIN THREAD
               response -> runOnUiThread(()-> {
                  upcomingMovies = getMovies(response);
                   //when we finish with the request we update the UI
                   updateUI();
               }), error->{

       });

        StringRequest popularMoviesRequest = new StringRequest(Request.Method.GET,popularMoviesUri,
                response -> runOnUiThread(()-> {
                    popularMovies = getMovies(response);
                    //the call is made in the background, the second requests
                    //may not wait for the first one to finish
                    requestQueue.add(upcomingMoviesRequest);
                }), error ->{

        });


       requestQueue.add(popularMoviesRequest);

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

    @Override
    protected void onRestart() {
        super.onRestart();
        searchView.setQuery("",false);
    }
}
