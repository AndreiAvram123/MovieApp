package com.example.movieapp.activities.activities;

import android.app.SearchManager;
import android.content.Intent;
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
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.adapters.MainAdapter;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity{
    private static final String TAG = SearchableActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView no_results_error;
    private ImageView back_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Useful.makeActivityFullscreen(getWindow());

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

    private void pushRequest(String uri) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest dataRequest = new StringRequest(StringRequest.Method.GET,uri,
                response-> runOnUiThread(()-> updateUI(response)),
                error -> {

                });
        requestQueue.add(dataRequest);
    }

    private void updateUI(String responseData) {
        ArrayList<Movie> searchedMovies = Useful.getMovies(responseData);
        if(searchedMovies.isEmpty()){
            no_results_error.setVisibility(View.VISIBLE);
        }else {
            MainAdapter mainAdapter = new MainAdapter(recyclerView,searchedMovies);
            recyclerView.setAdapter(mainAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new CustomDivider(20));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

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
        String uriFormat = "https://api.themoviedb.org/3/search/movie?api_key=55398af9b60eda4997b848dd5ccf7d44&query=";
        return uriFormat + query.replace(' ','+');
    }


}

