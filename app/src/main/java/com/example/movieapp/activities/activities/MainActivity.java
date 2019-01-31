package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.AppDatabase;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.CustomDialog;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.fragments.SavedMoviesFragment;
import com.example.movieapp.activities.fragments.ViewPagerFragment;
import com.example.movieapp.activities.interfaces.DatabaseInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String currentFragment;
    private  ArrayList<Movie> popularMovies;
    private  ArrayList<Movie> upcomingMovies;
    private List<Movie> savedMovies;
    private static final String popularMoviesUri = "https://api.themoviedb.org/3/movie/popular?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1";
    private static final  String upcomingMoviesUri = "https://api.themoviedb.org/3/movie/upcoming?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1&region=GB";
    private DrawerLayout drawerLayout;
    private SearchView searchView;
    private ViewPagerFragment viewPagerFragment;
    private RequestQueue requestQueue;
    private FragmentManager fragmentManager;
    private SavedMoviesFragment savedMoviesFragment;
    private DatabaseInterface databaseInterface;
    private int numberOfMoviesSaved;
    private TextView errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        initializeDatabase();
        createSavedMoviesFragment();
        setUpToolbar();


        if(isNetworkAvailable())
        pushRequests();
        else {
            showNetworkUnavailableError();
        }

    }

    private void showNetworkUnavailableError() {
        errorMessage.setVisibility(View.VISIBLE);
        new CustomDialog(this,getString(R.string.please_connect))
        .show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Create a Thread object in order to
     * perform operations on the Room database
     * THIS IS REQUIRED BY ROOM : TO HAVE
     * A BACKGROUND THREAD FOR DATABASE
     * OPERATIONS
     * @return
     */
    private void createSavedMoviesFragment() {
       Thread backgroundThread = new Thread(
               () -> {
                  savedMovies = databaseInterface.selectAllMovies();
                  numberOfMoviesSaved = savedMovies.size();
                  savedMoviesFragment = SavedMoviesFragment.newInstance(savedMovies);
               }
       );
       //always use start
       backgroundThread.start();
    }

    /**
     * This method checks if the user
     * has removed or added a new movie to
     * the saved ones, if so update the
     * movies from the saved fragment
     */
    private void checkForSavedMovies(){
        Thread backgroundThread = new Thread(
                () -> {
                    int numberOfMoviesInDatabase = databaseInterface.countMovies();
                    if(numberOfMoviesInDatabase ==numberOfMoviesSaved-1
                    || numberOfMoviesInDatabase == numberOfMoviesSaved +1)
                        updateSavedMoviesFragment();

                }
        );
        //always use start
        backgroundThread.start();
    }

    private void updateSavedMoviesFragment() {
        //we are still in the background thread
        savedMovies = databaseInterface.selectAllMovies();
        savedMoviesFragment = SavedMoviesFragment.newInstance(
                savedMovies);
        numberOfMoviesSaved = savedMovies.size();

    }

    private void initializeDatabase() {
        //use getApplicationContext because data is related to the
        //whole application
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,
                "movies_database").build();
        databaseInterface = appDatabase.databaseInterface();
    }

    private void initializeUI() {
        errorMessage = findViewById(R.id.error_message_main);
        Useful.makeActivityFullscreen(getWindow());
        fragmentManager = getSupportFragmentManager();
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
                    if(!currentFragment.equals(Constraints.VIEW_PAGER_FRAGMENT))
                        showViewPagerFragment();
                    return true;

                case R.id.saved_movies_item :
                     drawerLayout.closeDrawers();
                     menuItem.setChecked(true);
                     uncheckItems(menuItem.getItemId(),navigationView.getMenu());
                     showSavedMoviesFragment();
                     currentFragment = Constraints.SAVED_MOVIES_FRAGMENT;
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

    private void showSavedMoviesFragment() {
        if(savedMoviesFragment != null){
        currentFragment = Constraints.SAVED_MOVIES_FRAGMENT;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_placeholder,savedMoviesFragment,
                Constraints.SAVED_MOVIES_FRAGMENT);
        fragmentTransaction.commit();
        }
    }

    /**
     * Use this to uncheck items in the
     * menu from the navigation drawer
     * @param itemId
     * @param menu
     */
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        toolbar.setOnClickListener(view -> searchView.setIconified(false));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
        currentFragment = Constraints.VIEW_PAGER_FRAGMENT;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_placeholder,viewPagerFragment,currentFragment);
        fragmentTransaction.commit();
    }

    private void updateUI() {
        currentFragment = Constraints.VIEW_PAGER_FRAGMENT;
        viewPagerFragment = ViewPagerFragment.newInstance(upcomingMovies,popularMovies);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_placeholder,viewPagerFragment,currentFragment);
        fragmentTransaction.commit();
    }

    /**
     * This method pushes requests  in order to get data
     * from the movie database
     *
     */
    private void pushRequests() {
        //initialize volley
        requestQueue = Volley.newRequestQueue(getApplicationContext());
         //request a string response from the url for popular movies

       StringRequest upcomingMoviesRequest = new StringRequest(Request.Method.GET,upcomingMoviesUri,
               //REMEMBER THAT OPERATIONS SHOULD BE DONE IN
               //THE MAIN THREAD
               response -> runOnUiThread(()-> {

                   upcomingMovies = Useful.getMovies(response);
                   /**
                    * SORT THE MOVIES BY DATE
                    * The comparable interface works this
                    * way
                    * IF YOU WANT TO change the position of movie1
                    * with the position of movie 2 then you must return 1(tell the
                    * comparator "YES CHANGE THEM")
                    * IF THE COMPARATOR RECEIVES 0 OR -1 DOES NOT DO ANYTHING
                    */
                   upcomingMovies.sort((movie1,movie2)-> movie1.getReleaseDate().compareTo(
                           movie2.getReleaseDate()
                   ));

                   //UPDATE THE UI WHEN REQUEST FINISHES
                   updateUI();
               }), error->{

       });

        StringRequest popularMoviesRequest = new StringRequest(Request.Method.GET,popularMoviesUri,
                response -> runOnUiThread(()-> {
                   popularMovies= Useful.getMovies(response);
                    //the call is made in the background, the second requests
                    //may not wait for the first one to finish
                    requestQueue.add(upcomingMoviesRequest);
                }), error ->{

        });


       requestQueue.add(popularMoviesRequest);

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //set the query to empty
        searchView.setQuery("",false);
        //there may be a new movies added or removed from the database
        checkForSavedMovies();



    }


}
