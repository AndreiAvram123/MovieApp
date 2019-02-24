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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.AppDatabase;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Utilities;
import com.example.movieapp.activities.adapters.MainAdapter;
import com.example.movieapp.activities.fragments.ExpandedMovieFragment;
import com.example.movieapp.activities.fragments.SavedMoviesFragment;
import com.example.movieapp.activities.fragments.ViewPagerFragment;
import com.example.movieapp.activities.interfaces.DatabaseInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainAdapter.AdapterInterface, ExpandedMovieFragment.ExpandedMovieFragmentInterface {

    private static final String popularMoviesUri = "https://api.themoviedb.org/3/movie/popular?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1";
    private static final String upcomingMoviesUri = "https://api.themoviedb.org/3/movie/upcoming?api_key=55398af9b60eda4997b848dd5ccf7d44&language=en-US&page=1&region=GB";
    private ArrayList<Movie> popularMovies;
    private ArrayList<Movie> upcomingMovies;
    private List<Movie> savedMovies;
    private String nickname;
    private DrawerLayout drawerLayout;
    private android.support.v7.widget.SearchView searchView;
    private ViewPagerFragment viewPagerFragment;
    private FragmentManager fragmentManager;
    private DatabaseInterface databaseInterface;
    private boolean isBackArrowShown = false;
    private ImageView homeButton;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();
        getSavedMoviesFromDatabase();

        initializeUI();
        setUpToolbar();


        if (Utilities.isNetworkAvailable(this)) {
            pushRequests();
        } else {
            /*because the ViewPagerFragment is initialized in the method
              initializeUI() with null values,it will display an internet
              error message when shown
            */
            showViewPagerFragment();
        }


    }


    /**
     * This method obtains data from the Room database
     * using the DAO interface
     * <p>
     * Create a Thread object in order to
     * perform operations on the Room database
     * THIS IS REQUIRED BY ROOM : TO HAVE
     * A BACKGROUND THREAD FOR DATABASE
     * OPERATIONS
     */
    private void getSavedMoviesFromDatabase() {
        Thread backgroundThread = new Thread(
                () -> savedMovies = databaseInterface.getAllMovies()
        );
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


    private void initializeUI() {
        nickname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        fragmentManager = getSupportFragmentManager();
        viewPagerFragment = ViewPagerFragment.newInstance(null, null);

        drawerLayout = findViewById(R.id.drawer_layout);
        homeButton = findViewById(R.id.home_back);
        homeButton.setOnClickListener(view -> toggleHomeButton());

        makeActivityFullscreen();
        configureNavigationView();
        updateViewHeader();
        showViewPagerFragment();
    }

    private void makeActivityFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /**
     * This method is used to update the ViewHeader of the
     * NavigationView with the current user name
     */
    private void updateViewHeader() {
        View header_layout = navigationView.getHeaderView(0);
        TextView nicknameTextView = header_layout.findViewById(R.id.nickname_drawer);
        nicknameTextView.setText(nickname);
    }


    /**
     * This method is used to define actions when the user
     * clicks on each of the item inside the navigationView
     * First, it gets a reference to the current navigation
     * view and then uses a NavigationItemSelectedListener to
     * listen to user touch events
     * <p>
     * When the profile_item is selected - we start EditProfileActivity
     * When the home_item is selected - we show the ViewPagerFragment
     * When the saved_movies item is selected we show the saved movies fragment
     */
    private void configureNavigationView() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {

                case R.id.edit_profile_item:
                    drawerLayout.closeDrawers();
                    uncheckItems(menuItem.getItemId(), navigationView.getMenu());
                    startActivity(new Intent(this, EditProfileActivity.class));
                    return true;

                case R.id.home_item:
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(true);
                    uncheckItems(menuItem.getItemId(), navigationView.getMenu());
                    showViewPagerFragment();
                    return true;

                case R.id.saved_movies_item:
                    drawerLayout.closeDrawers();
                    menuItem.setChecked(true);
                    uncheckItems(menuItem.getItemId(), navigationView.getMenu());
                    showSavedMoviesFragment();
                    return true;

                case R.id.settings_item:
                    drawerLayout.closeDrawers();
                    uncheckItems(menuItem.getItemId(), navigationView.getMenu());
                    startSettingsActivity();
                    return true;

            }
            return false;
        });
    }


    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(SettingsActivity.KEY_NICKNAME, nickname);
        startActivity(intent);
    }

    /**
     * Call this method in order to show
     * the SavedMoviesFragment
     */
    private void showSavedMoviesFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.placeholder_fragment_main, SavedMoviesFragment.newInstance(savedMovies))
                .commit();
    }

    /**
     * Use this method to uncheck all items
     * except from the one passed as the parameter
     *
     * @param itemId
     * @param menu
     */
    private void uncheckItems(int itemId, Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            if (itemId != menu.getItem(i).getItemId()) {
                menu.getItem(i).setChecked(false);
            }
        }
    }

    /**
     * This method is used to configure the toolbar
     * as the ActionBar
     * We want to open the searchView every time
     * the user touches the toolbar,so we set
     * a listener on the toolbar and use the method
     * setIconified(false) on the searchView reference
     */
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        toolbar.setOnClickListener(view -> {
                    searchView.setIconified(false);
                    toggleHomeButton();
                }


        );
    }

    /**
     * This method is called each time the
     * user presses the "home button"
     * IT IS NOT ACTUALLY A REAL home button
     * IT IS just an image that plays an
     * an animation every time it changes its
     * image resource
     * <p>
     * If the searchView is iconified we open the navigation
     * drawer
     * If the searchView is not iconified and we are  currently
     * displaying the back arrow as the home button then call
     * the method showHomeIcon()
     * If the searchView is not iconified and we are
     * currently displaying the home icon, call showBackIcon()
     */
    private void toggleHomeButton() {
        if (searchView.isIconified()) {
            drawerLayout.openDrawer(Gravity.START);
        } else {
            if (isBackArrowShown) {
                showHomeIcon();
            } else {
                showBackIcon();
            }
        }
    }

    private void showBackIcon() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_first_stage);
        homeButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        homeButton.startAnimation(animation);
        isBackArrowShown = true;
        searchView.setIconified(false);
    }

    private void showHomeIcon() {
        homeButton.setImageResource(R.drawable.ic_menu_black_24dp);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_second_stage);
        homeButton.startAnimation(animation);
        isBackArrowShown = false;
        searchView.setIconified(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    /**
     * Call this method if you want to show the ViewPager fragment
     */
    private void showViewPagerFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.placeholder_fragment_main, viewPagerFragment)
                .commit();

    }

    /**
     * This method creates one  ViewFragment object and fills
     * it with data from @upcomingMovies and @popularMovies
     */
    private void updateUI() {
        viewPagerFragment = ViewPagerFragment.newInstance(upcomingMovies, popularMovies);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.placeholder_fragment_main, viewPagerFragment);
        fragmentTransaction.commit();
    }

    /**
     * This method used the Volley class in order to perform
     * network operation for getting data
     * First, we initialize a requestQueue that is going
     * to be used for executing requests
     * <p>
     * We create the following requests
     *
     * @request1 - upcomingMoviesRequest that gets data
     * about upcomingMovies in a JSON format
     * @request2 - popularMoviesRequest that gets data about
     * popular movies in a JSON format
     * <p>
     * !!!!!!VOLLEY DOES NOT WAIT FOR ONE REQUEST TO FINISH UNTIL IT START
     * ANOTHER ONE !!!!!!
     * Volley will take the request queue and start a new request right after another
     * IF YOU WANT FOR EXAMPLE,TO EXECUTE request2 after request1 has finished
     * call requestQueue.add(request2) in the response body of the request1
     */
    private void pushRequests() {
        //initialize volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //request a string response from the url for popular movies

        StringRequest upcomingMoviesRequest = new StringRequest(Request.Method.GET, upcomingMoviesUri,
                //REMEMBER THAT OPERATIONS SHOULD BE DONE IN
                //THE MAIN THREAD
                response -> runOnUiThread(() -> {

                    upcomingMovies = Utilities.processJSONFormat(response);
                    /*
                      SORT THE MOVIES BY DATE
                      The comparable interface works this
                      way
                      IF YOU WANT TO change the position of movie1
                      with the position of movie 2 then you must return 1(tell the
                      comparator "YES CHANGE THEM")
                      IF THE COMPARATOR RECEIVES 0 OR -1 DOES NOT DO ANYTHING
                     */
                    upcomingMovies.sort((movie1, movie2) -> movie1.getReleaseDate().compareTo(
                            movie2.getReleaseDate()
                    ));

                    //UPDATE THE UI WHEN REQUEST FINISHES
                    updateUI();
                }), error -> {

        });
        StringRequest popularMoviesRequest = new StringRequest(Request.Method.GET, popularMoviesUri,
                response -> runOnUiThread(() -> {
                    popularMovies = Utilities.processJSONFormat(response);
                    //the call is made in the background, the second requests
                    //may not wait for the first one to finish
                    requestQueue.add(upcomingMoviesRequest);
                }), error -> {

        });
        requestQueue.add(popularMoviesRequest);

    }


    @Override
    protected void onRestart() {
        showViewPagerFragment();
        //TODO
        //REFACTOR ...
        //the user has pressed back in the login activity after
        //he has signed out
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        } else {
            super.onRestart();
            //set the query to empty
            searchView.setQuery("", false);


        }

    }


    @Override
    public void onItemClicked(Movie movie) {
        if (savedMovies.contains(movie)) {
            movie.setSaved(true);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.placeholder_layout_main, ExpandedMovieFragment.newInstance(movie))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void saveMovie(Movie movie) {
        savedMovies.add(movie);
        Thread backgroundThread = new Thread(() ->
                databaseInterface.insertMovie(movie));
        backgroundThread.start();
    }

    @Override
    public void deleteMovie(Movie movie) {
        savedMovies.remove(movie);
        Thread backgroundThread = new Thread(() ->
                databaseInterface.deleteMovieById(movie.getMovieID()));
        backgroundThread.start();
    }
}