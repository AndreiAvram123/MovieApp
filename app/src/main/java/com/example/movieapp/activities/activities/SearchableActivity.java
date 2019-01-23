package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainActivityAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {
    private static final String TAG = SearchableActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView no_results_error;
    private ImageView back_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        makeAppFullscreen();
        recyclerView = findViewById(R.id.recycler_view_search_activity);
        no_results_error = findViewById(R.id.no_result_error_search);
        back_image = findViewById(R.id.back_image_seach);

        back_image.setOnClickListener(view -> finish());


        //get the intent ,verify the action and retrieve the query
        Intent searchIntent = getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            pushRequest(getUriForRequest(query.trim()));
        }
    }
    private void makeAppFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void pushRequest(String uri) {
        Log.d(TAG,uri);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest dataRequest = new StringRequest(StringRequest.Method.GET,uri,
                response-> runOnUiThread(()-> updateUI(response)),
                error -> {

                });
        requestQueue.add(dataRequest);
    }

    private void updateUI(String responseData) {
       recyclerView.setAdapter(new MainActivityAdapter(processResponse(responseData)));
       recyclerView.setHasFixedSize(true);
       recyclerView.addItemDecoration(new CustomDivider(15));
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This method return an ArrayList of maximum 5 movies
     * @param responseData - data from the servers
     * @return
     */
    private ArrayList<Movie> processResponse(String responseData) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(responseData);
            JSONArray resultsArray = object.getJSONArray("results");
            int position=0;
            while(position < resultsArray.length() && position <5){
                JSONObject movieJsonFormat = resultsArray.getJSONObject(position);
                Movie movie = new Movie(
                        movieJsonFormat.getString("overview"),
                        movieJsonFormat.getString("title"),
                        movieJsonFormat.getString("release_date"),
                       getString(R.string.request_format_image)+ movieJsonFormat.getString("poster_path")
                );
                position ++;
                movies.add(movie);
            }
        }catch (JSONException jsonE){
            jsonE.printStackTrace();
            no_results_error.setVisibility(View.VISIBLE);

        }
        if(movies.isEmpty()){
            no_results_error.setVisibility(View.VISIBLE);
        }
        return movies;
    }

    /**
     * This method is used in order to replace empty spaces
     * from the query with the + sign
     * The request does not work if we use empty
     * spaces
     * @param query - the query from the searchView
     * @return
     */
    private String getUriForRequest(String query){
        return query.replace(' ','+');
    }

}
