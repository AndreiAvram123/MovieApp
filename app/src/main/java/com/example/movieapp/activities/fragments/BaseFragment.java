package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainActivityAdapter;

import java.util.ArrayList;

/**
 * This is a base fragment used
 * to create other fragments
 */
public class BaseFragment extends Fragment {
     public static final String KEY_MOVIES_ARRAY ="KEY_MOVIES_ARRAY";
     public RecyclerView recyclerView;
     public  MainActivityAdapter mainActivityAdapter;
     public ArrayList<Movie> movies;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base_fragment,container,false);
        recyclerView = view.findViewById(R.id.recycler_view_base_fragment);
        movies = getArguments().getParcelableArrayList(KEY_MOVIES_ARRAY);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.addItemDecoration( new CustomDivider(20));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public void loadMoreMovies(int currentSize){
        mainActivityAdapter.addMovie(null);
        Handler handler = new Handler();
        handler.postDelayed(()->{
            mainActivityAdapter.removeMovie(currentSize);
            int currentPosition = currentSize;
            int endPosition = currentPosition +6;
            while(currentPosition < endPosition &&  endPosition < movies.size() ){
                //is less or equal to 6
                mainActivityAdapter.addMovie(movies.get(currentPosition));
                currentPosition++;
            }
            mainActivityAdapter.setLoading(false);

        },2000);
    }


}
